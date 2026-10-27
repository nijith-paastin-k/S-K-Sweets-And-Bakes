package com.skylink.sksweetsandbakes.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.cart.usecase.ApplyCouponUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.ClearCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.GetCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.RemoveCouponUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.RemoveFromCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.UpdateCartQuantityUseCase
import com.skylink.sksweetsandbakes.domain.model.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val cart: Cart = Cart(),
    val couponInput: String = "",
    val couponMessage: String? = null,
    val isCouponError: Boolean = false,
    val isLoading: Boolean = false
)

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val applyCouponUseCase: ApplyCouponUseCase,
    private val removeCouponUseCase: RemoveCouponUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCartUseCase().collect { cart ->
                _uiState.value = _uiState.value.copy(cart = cart)
            }
        }
    }

    fun onQuantityChanged(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            updateCartQuantityUseCase(productId, newQuantity)
        }
    }

    fun onRemoveItem(productId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
        }
    }

    fun onCouponInputChanged(code: String) {
        _uiState.value = _uiState.value.copy(couponInput = code, couponMessage = null)
    }

    fun applyCoupon() {
        val code = _uiState.value.couponInput.trim()
        if (code.isBlank()) return

        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val res = applyCouponUseCase(code)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        couponMessage = "Coupon ${res.data.code} applied successfully!",
                        isCouponError = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        couponMessage = res.message,
                        isCouponError = true
                    )
                }

                is Result.Loading -> {}
            }
        }
    }

    fun removeCoupon() {
        viewModelScope.launch {
            removeCouponUseCase()
            _uiState.value = _uiState.value.copy(couponInput = "", couponMessage = null)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
        }
    }
}
