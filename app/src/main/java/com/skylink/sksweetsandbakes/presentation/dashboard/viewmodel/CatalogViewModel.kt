package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.SearchProductsUseCase
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CatalogUiState(
    val categories: List<Category> = SeedData.categories,
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val selectedCategoryId: String? = null,
    val selectedCategoryTitle: String = "All Sweets & Bakes",
    val isLoading: Boolean = false
)

class CatalogViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getProductsUseCase.getCategories().collect { cats ->
                val finalCats = cats.ifEmpty { SeedData.categories }
                _uiState.value = _uiState.value.copy(categories = finalCats)
            }
        }
    }

    fun loadProductsForCategory(categoryId: String?, title: String?) {
        val cleanCatId =
            if (categoryId.isNullOrBlank() || categoryId == "{categoryId}" || categoryId.equals(
                    "all",
                    ignoreCase = true
                )
            ) null else categoryId
        val resolvedTitle =
            if (!title.isNullOrBlank() && title != "{title}") title else "All Sweets & Bakes"

        // Compute instant filtered products from SeedData to prevent any wrong category display or delay
        val instantFiltered = if (cleanCatId == null) {
            SeedData.products
        } else {
            val clean = cleanCatId.trim().lowercase()
            SeedData.products.filter {
                it.categoryId.trim().equals(clean, ignoreCase = true) ||
                        it.categoryName.trim().equals(clean, ignoreCase = true) ||
                        it.categoryId.trim().contains(clean, ignoreCase = true) ||
                        clean.contains(it.categoryId.trim(), ignoreCase = true) ||
                        it.categoryName.trim().contains(clean, ignoreCase = true) ||
                        clean.contains(it.categoryName.trim(), ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(
            selectedCategoryId = cleanCatId,
            selectedCategoryTitle = resolvedTitle,
            products = instantFiltered,
            isLoading = false
        )
        viewModelScope.launch {
            getProductsUseCase(cleanCatId).collect { prods ->
                val finalProds = prods.ifEmpty { instantFiltered }
                _uiState.value = _uiState.value.copy(products = finalProds, isLoading = false)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        viewModelScope.launch {
            val results = searchProductsUseCase(query)
            _uiState.value = _uiState.value.copy(searchResults = results)
        }
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            getProductsUseCase(null).collect { prods ->
                _uiState.value = _uiState.value.copy(products = prods.ifEmpty { SeedData.products })
            }
        }
    }
}
