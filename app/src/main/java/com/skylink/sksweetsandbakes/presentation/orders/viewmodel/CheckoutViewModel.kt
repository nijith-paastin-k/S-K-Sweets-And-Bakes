package com.skylink.sksweetsandbakes.presentation.orders.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.payment.RazorpayPaymentManager
import com.skylink.sksweetsandbakes.data.orders.requestmodel.RazorpayPaymentRequestModel
import com.skylink.sksweetsandbakes.domain.cart.usecase.GetCartUseCase
import com.skylink.sksweetsandbakes.domain.model.Cart
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.FulfillmentType
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.model.OrderPayment
import com.skylink.sksweetsandbakes.domain.model.OrderPricing
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import com.skylink.sksweetsandbakes.domain.model.PaymentMethod
import com.skylink.sksweetsandbakes.domain.model.PickupSlot
import com.skylink.sksweetsandbakes.domain.orders.usecase.DeleteAddressUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetSavedAddressesUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.PlaceOrderUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.SaveAddressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val cart: Cart = Cart(),
    val fulfillmentType: FulfillmentType = FulfillmentType.DELIVERY,
    val savedAddresses: List<DeliveryAddress> = emptyList(),
    val selectedAddress: DeliveryAddress? = null,
    val selectedPickupDate: String = "Today",
    val selectedPickupSlot: String = "Express Delivery",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.RAZORPAY,
    val isPlacingOrder: Boolean = false,
    val placedOrderId: String? = null,
    val errorMessage: String? = null
)

class CheckoutViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val getSavedAddressesUseCase: GetSavedAddressesUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCartUseCase().collect { cart ->
                _uiState.value = _uiState.value.copy(cart = cart)
            }
        }
        viewModelScope.launch {
            getSavedAddressesUseCase("usr_demo_01").collect { addresses ->
                _uiState.value = _uiState.value.copy(
                    savedAddresses = addresses,
                    selectedAddress = addresses.firstOrNull { it.isDefault }
                        ?: addresses.firstOrNull()
                )
            }
        }
    }

    fun onFulfillmentTypeChanged(type: FulfillmentType) {
        _uiState.value = _uiState.value.copy(fulfillmentType = type)
    }

    fun onAddressSelected(address: DeliveryAddress) {
        _uiState.value = _uiState.value.copy(selectedAddress = address)
    }

    fun saveOrUpdateAddress(address: DeliveryAddress) {
        viewModelScope.launch {
            saveAddressUseCase("usr_demo_01", address)
            _uiState.value = _uiState.value.copy(selectedAddress = address)
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            deleteAddressUseCase("usr_demo_01", addressId)
        }
    }

    fun onPickupSlotSelected(date: String, slot: String) {
        _uiState.value = _uiState.value.copy(selectedPickupDate = date, selectedPickupSlot = slot)
    }

    fun onPaymentMethodChanged(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
    }

    fun placeOrder(onOrderConfirmed: (orderId: String) -> Unit) {
        val cart = _uiState.value.cart
        if (cart.isEmpty) {
            _uiState.value = _uiState.value.copy(errorMessage = "Cart cannot be empty")
            return
        }

        if (_uiState.value.fulfillmentType == FulfillmentType.DELIVERY && _uiState.value.selectedAddress == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please select a delivery address")
            return
        }

        _uiState.value = _uiState.value.copy(isPlacingOrder = true, errorMessage = null)
        viewModelScope.launch {
            val order = Order(
                customerId = "usr_demo_01",
                customerName = "Ramesh Kumar",
                customerPhone = "+91 98765 43210",
                fulfillmentType = _uiState.value.fulfillmentType,
                status = OrderStatus.CONFIRMED,
                items = cart.items,
                deliveryAddress = if (_uiState.value.fulfillmentType == FulfillmentType.DELIVERY) _uiState.value.selectedAddress else null,
                pickupSlot = if (_uiState.value.fulfillmentType == FulfillmentType.PICKUP) PickupSlot(
                    slotDate = _uiState.value.selectedPickupDate,
                    timeWindow = _uiState.value.selectedPickupSlot
                ) else null,
                pricing = OrderPricing(
                    subtotalPaise = cart.subtotalPaise,
                    discountPaise = cart.discountPaise,
                    couponCode = cart.appliedCouponCode,
                    deliveryFeePaise = if (_uiState.value.fulfillmentType == FulfillmentType.DELIVERY) cart.deliveryFeePaise else 0L,
                    packagingFeePaise = cart.packagingFeePaise,
                    taxPaise = cart.taxPaise,
                    grandTotalPaise = cart.grandTotalPaise
                ),
                payment = OrderPayment(
                    paymentId = "pay_gateway_${System.currentTimeMillis()}",
                    gatewayOrderId = "order_rcptid_${System.currentTimeMillis() % 10000}",
                    method = _uiState.value.selectedPaymentMethod,
                    status = "SUCCESS",
                    paidAt = System.currentTimeMillis()
                )
            )

            when (val res = placeOrderUseCase(order)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isPlacingOrder = false,
                        placedOrderId = res.data
                    )
                    onOrderConfirmed(res.data)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isPlacingOrder = false,
                        errorMessage = res.message
                    )
                }

                is Result.Loading -> {}
            }
        }
    }

    /**
     * Initiates native Razorpay Checkout gateway
     * Upon payment completion, records real Razorpay payment ID and creates confirmed order.
     */
    fun placeOrderWithRazorpay(
        activity: Activity,
        customerName: String = "Ramesh Kumar",
        customerEmail: String = "customer@sksweets.com",
        customerPhone: String = "9876543210",
        onOrderConfirmed: (orderId: String) -> Unit,
        onPaymentFailed: (String) -> Unit
    ) {
        val cart = _uiState.value.cart
        if (cart.isEmpty) {
            _uiState.value = _uiState.value.copy(errorMessage = "Cart cannot be empty")
            return
        }

        if (_uiState.value.fulfillmentType == FulfillmentType.DELIVERY && _uiState.value.selectedAddress == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please select a delivery address")
            return
        }

        val request = RazorpayPaymentRequestModel(
            amountPaise = cart.grandTotalPaise,
            currency = "INR",
            orderId = "rcpt_${System.currentTimeMillis() % 100000}",
            customerName = customerName,
            customerEmail = customerEmail,
            customerPhone = customerPhone
        )

        _uiState.value = _uiState.value.copy(isPlacingOrder = true, errorMessage = null)

        RazorpayPaymentManager.startPayment(activity, request) { result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    val order = Order(
                        customerId = "usr_demo_01",
                        customerName = customerName,
                        customerPhone = customerPhone,
                        fulfillmentType = _uiState.value.fulfillmentType,
                        status = OrderStatus.CONFIRMED,
                        items = cart.items,
                        deliveryAddress = if (_uiState.value.fulfillmentType == FulfillmentType.DELIVERY) _uiState.value.selectedAddress else null,
                        pickupSlot = if (_uiState.value.fulfillmentType == FulfillmentType.PICKUP) PickupSlot(
                            slotDate = _uiState.value.selectedPickupDate,
                            timeWindow = _uiState.value.selectedPickupSlot
                        ) else null,
                        pricing = OrderPricing(
                            subtotalPaise = cart.subtotalPaise,
                            discountPaise = cart.discountPaise,
                            couponCode = cart.appliedCouponCode,
                            deliveryFeePaise = if (_uiState.value.fulfillmentType == FulfillmentType.DELIVERY) cart.deliveryFeePaise else 0L,
                            packagingFeePaise = cart.packagingFeePaise,
                            taxPaise = cart.taxPaise,
                            grandTotalPaise = cart.grandTotalPaise
                        ),
                        payment = OrderPayment(
                            paymentId = result.paymentId ?: "pay_rzp_${System.currentTimeMillis()}",
                            gatewayOrderId = result.orderId
                                ?: "order_rzp_${System.currentTimeMillis() % 10000}",
                            method = PaymentMethod.RAZORPAY,
                            status = "PAID",
                            paidAt = System.currentTimeMillis()
                        )
                    )

                    when (val res = placeOrderUseCase(order)) {
                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isPlacingOrder = false,
                                placedOrderId = res.data
                            )
                            onOrderConfirmed(res.data)
                        }

                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isPlacingOrder = false,
                                errorMessage = res.message
                            )
                            onPaymentFailed(res.message)
                        }

                        is Result.Loading -> {}
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isPlacingOrder = false,
                    errorMessage = result.errorMessage
                )
                onPaymentFailed(result.errorMessage ?: "Payment cancelled")
            }
        }
    }
}
