package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.cart.usecase.AddToCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.GetCartUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculatePricingUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductDetailUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.WishlistUseCase
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val product: Product? = null,
    val selectedQuantity: Int = 250,
    val calculatedPricePaise: Long = 0L,
    val reviews: List<Review> = emptyList(),
    val isWishlisted: Boolean = false,
    val isLoading: Boolean = false,
    val isAddedToCart: Boolean = false,
    val cartItemCount: Int = 0,
    val errorMessage: String? = null
)

class ProductDetailViewModel(
    private val productId: String,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val wishlistUseCase: WishlistUseCase,
    private val pricingUseCase: CalculatePricingUseCase = CalculatePricingUseCase()
) : ViewModel() {

    private val initialProduct: Product? = run {
        val cleanId = productId.trim()
        SeedData.products.find {
            it.productId.equals(cleanId, ignoreCase = true) ||
                    it.sku.equals(cleanId, ignoreCase = true) ||
                    it.name.contains(cleanId, ignoreCase = true) ||
                    cleanId.contains(it.productId, ignoreCase = true)
        } ?: SeedData.products.firstOrNull()
    }

    private val initialPrice: Long = initialProduct?.let {
        pricingUseCase.calculateLineTotalPaise(
            measurementType = it.measurementType,
            unitPricePaise = it.unitPricePaise,
            quantity = it.minQuantity
        )
    } ?: 0L

    private val _uiState = MutableStateFlow(
        ProductDetailUiState(
            product = initialProduct,
            selectedQuantity = initialProduct?.minQuantity ?: 250,
            calculatedPricePaise = initialPrice,
            isLoading = initialProduct == null
        )
    )
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
        observeCart()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            when (val res = getProductDetailUseCase(productId)) {
                is Result.Success -> {
                    val prod = res.data
                    val defaultQty =
                        _uiState.value.selectedQuantity.takeIf { it > 0 } ?: prod.minQuantity
                    val currentPrice = pricingUseCase.calculateLineTotalPaise(
                        measurementType = prod.measurementType,
                        unitPricePaise = prod.unitPricePaise,
                        quantity = defaultQty
                    )
                    _uiState.value = _uiState.value.copy(
                        product = prod,
                        selectedQuantity = defaultQty,
                        calculatedPricePaise = currentPrice,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is Result.Error -> {
                    if (_uiState.value.product == null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = res.message
                        )
                    }
                }

                is Result.Loading -> {}
            }
        }
        viewModelScope.launch {
            getProductDetailUseCase.getReviews(productId).collect { revs ->
                _uiState.value = _uiState.value.copy(reviews = revs)
            }
        }
        viewModelScope.launch {
            wishlistUseCase.getWishlistProductIds("usr_demo_01").collect { ids ->
                _uiState.value = _uiState.value.copy(isWishlisted = ids.contains(productId))
            }
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartUseCase().collect { cart ->
                _uiState.value = _uiState.value.copy(
                    cartItemCount = cart.items.size,
                    isAddedToCart = cart.items.any { it.productId == productId }
                )
            }
        }
    }

    fun onQuantitySelected(quantity: Int) {
        val prod = _uiState.value.product ?: return
        val price = pricingUseCase.calculateLineTotalPaise(
            measurementType = prod.measurementType,
            unitPricePaise = prod.unitPricePaise,
            quantity = quantity
        )
        _uiState.value = _uiState.value.copy(
            selectedQuantity = quantity,
            calculatedPricePaise = price
        )
    }

    fun addToCart(onSuccess: () -> Unit) {
        val prod = _uiState.value.product ?: return
        viewModelScope.launch {
            val item = CartItem(
                productId = prod.productId,
                productName = prod.name,
                productImageUrl = prod.images.firstOrNull() ?: "",
                measurementType = prod.measurementType,
                selectedQuantity = _uiState.value.selectedQuantity,
                baseUnit = prod.baseUnit,
                displayUnit = prod.displayUnit,
                unitPricePaise = prod.unitPricePaise,
                lineTotalPaise = _uiState.value.calculatedPricePaise
            )
            addToCartUseCase(item)
            _uiState.value = _uiState.value.copy(isAddedToCart = true)
            onSuccess()
        }
    }

    fun toggleWishlist() {
        viewModelScope.launch {
            wishlistUseCase.toggleWishlist("usr_demo_01", productId)
        }
    }
}
