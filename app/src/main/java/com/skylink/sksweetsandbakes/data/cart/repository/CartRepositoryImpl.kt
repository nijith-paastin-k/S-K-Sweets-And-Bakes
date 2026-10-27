package com.skylink.sksweetsandbakes.data.cart.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.cart.usecase.ValidateOrderUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculatePricingUseCase
import com.skylink.sksweetsandbakes.domain.model.Cart
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.Coupon
import com.skylink.sksweetsandbakes.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepositoryImpl(
    private val pricingUseCase: CalculatePricingUseCase = CalculatePricingUseCase(),
    private val validateOrderUseCase: ValidateOrderUseCase = ValidateOrderUseCase(pricingUseCase)
) : CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    private var appliedCoupon: Coupon? = null

    private val _cart = MutableStateFlow(Cart())
    override val cart: Flow<Cart> = _cart.asStateFlow()

    private fun recomputeCart() {
        val currentItems = _items.value
        val pricing = validateOrderUseCase.calculateCartPricing(
            items = currentItems,
            appliedCoupon = appliedCoupon
        )

        _cart.value = Cart(
            items = currentItems,
            subtotalPaise = pricing.subtotalPaise,
            discountPaise = pricing.discountPaise,
            appliedCouponCode = appliedCoupon?.code,
            deliveryFeePaise = pricing.deliveryFeePaise,
            packagingFeePaise = pricing.packagingFeePaise,
            taxPaise = pricing.taxPaise,
            grandTotalPaise = pricing.grandTotalPaise
        )
    }

    override suspend fun addItem(item: CartItem) {
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.productId == item.productId }
        val lineTotal = pricingUseCase.calculateLineTotalPaise(
            measurementType = item.measurementType,
            unitPricePaise = item.unitPricePaise,
            quantity = item.selectedQuantity
        )
        val computedItem = item.copy(lineTotalPaise = lineTotal)

        if (index >= 0) {
            current[index] = computedItem
        } else {
            current.add(computedItem)
        }
        _items.value = current
        recomputeCart()
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeItem(productId)
            return
        }
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.productId == productId }
        if (index >= 0) {
            val item = current[index]
            val lineTotal = pricingUseCase.calculateLineTotalPaise(
                measurementType = item.measurementType,
                unitPricePaise = item.unitPricePaise,
                quantity = quantity
            )
            current[index] = item.copy(selectedQuantity = quantity, lineTotalPaise = lineTotal)
            _items.value = current
            recomputeCart()
        }
    }

    override suspend fun removeItem(productId: String) {
        _items.value = _items.value.filterNot { it.productId == productId }
        recomputeCart()
    }

    override suspend fun applyCoupon(couponCode: String): Result<Coupon> {
        val coupon = SeedData.coupons.find {
            it.code.equals(couponCode.trim(), ignoreCase = true) && it.isActive
        }
        return if (coupon != null) {
            appliedCoupon = coupon
            recomputeCart()
            Result.Success(coupon)
        } else {
            Result.Error(IllegalArgumentException("Invalid or expired coupon code: $couponCode"))
        }
    }

    override suspend fun removeCoupon() {
        appliedCoupon = null
        recomputeCart()
    }

    override suspend fun clearCart() {
        _items.value = emptyList()
        appliedCoupon = null
        recomputeCart()
    }
}
