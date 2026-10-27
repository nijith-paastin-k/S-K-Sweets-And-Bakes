package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.domain.cart.usecase.GetCartUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.WishlistUseCase
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val categories: List<Category> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val wishlistIds: Set<String> = emptySet(),
    val cartCount: Int = 0,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class HomeViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val wishlistUseCase: WishlistUseCase,
    private val getCartUseCase: GetCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            getProductsUseCase.getCategories().collect { cats ->
                _uiState.value = _uiState.value.copy(categories = cats)
            }
        }
        viewModelScope.launch {
            getProductsUseCase(null).collect { prods ->
                _uiState.value = _uiState.value.copy(allProducts = prods)
            }
        }
        viewModelScope.launch {
            getProductsUseCase.getFeatured().collect { featured ->
                _uiState.value = _uiState.value.copy(featuredProducts = featured, isLoading = false)
            }
        }
        viewModelScope.launch {
            wishlistUseCase.getWishlistProductIds("usr_demo_01").collect { ids ->
                _uiState.value = _uiState.value.copy(wishlistIds = ids)
            }
        }
        viewModelScope.launch {
            getCartUseCase().collect { cart ->
                _uiState.value = _uiState.value.copy(cartCount = cart.totalItemCount)
            }
        }
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            val wasWishlisted = _uiState.value.wishlistIds.contains(productId)
            wishlistUseCase.toggleWishlist("usr_demo_01", productId)
            _uiState.value = _uiState.value.copy(
                userMessage = if (!wasWishlisted) "Added to your Wishlist ❤️" else "Removed from Wishlist"
            )
        }
    }

    fun clearUserMessage() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}
