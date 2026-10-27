package com.skylink.sksweetsandbakes.presentation.wishlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.domain.cart.usecase.AddToCartUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.WishlistUseCase
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class WishlistUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class WishlistViewModel(
    private val wishlistUseCase: WishlistUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()

    init {
        loadWishlist()
    }

    private fun loadWishlist() {
        viewModelScope.launch {
            combine(
                wishlistUseCase.getWishlistProductIds("usr_demo_01"),
                getProductsUseCase()
            ) { ids, allProducts ->
                allProducts.filter { ids.contains(it.productId) }
            }.collect { wishlistedList ->
                _uiState.value = _uiState.value.copy(
                    products = wishlistedList,
                    isLoading = false
                )
            }
        }
    }

    fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            wishlistUseCase.toggleWishlist("usr_demo_01", productId)
            _uiState.value = _uiState.value.copy(userMessage = "Removed from Wishlist")
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val defaultQty = if (product.measurementType == MeasurementType.WEIGHT) 500 else 1
            val item = CartItem(
                productId = product.productId,
                productName = product.name,
                productImageUrl = product.images.firstOrNull() ?: "",
                measurementType = product.measurementType,
                selectedQuantity = defaultQty,
                baseUnit = product.baseUnit,
                displayUnit = product.displayUnit,
                unitPricePaise = product.unitPricePaise,
                lineTotalPaise = product.unitPricePaise
            )
            addToCartUseCase(item)
            _uiState.value = _uiState.value.copy(userMessage = "Added ${product.name} to Cart 🛒")
        }
    }

    fun clearUserMessage() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}
