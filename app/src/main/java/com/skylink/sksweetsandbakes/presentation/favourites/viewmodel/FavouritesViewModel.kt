package com.skylink.sksweetsandbakes.presentation.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.domain.cart.usecase.AddToCartUseCase
import com.skylink.sksweetsandbakes.domain.favourites.usecase.GetFavouritesUseCase
import com.skylink.sksweetsandbakes.domain.favourites.usecase.ToggleFavouriteUseCase
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavouritesUiState(
    val favouriteProducts: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class FavouritesViewModel(
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState: StateFlow<FavouritesUiState> = _uiState.asStateFlow()

    init {
        loadFavourites()
    }

    fun loadFavourites() {
        viewModelScope.launch {
            getFavouritesUseCase("usr_demo_01").collect { list ->
                _uiState.value = _uiState.value.copy(
                    favouriteProducts = list,
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavourite(productId: String) {
        viewModelScope.launch {
            toggleFavouriteUseCase(productId, "usr_demo_01")
            _uiState.value = _uiState.value.copy(userMessage = "Updated Favourites")
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
