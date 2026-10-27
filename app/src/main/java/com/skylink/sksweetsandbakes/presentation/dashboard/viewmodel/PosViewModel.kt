package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculatePricingUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.ProcessPosSaleUseCase
import com.skylink.sksweetsandbakes.domain.model.PosSale
import com.skylink.sksweetsandbakes.domain.model.PosSaleItem
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PosUiState(
    val products: List<Product> = emptyList(),
    val cartItems: List<PosSaleItem> = emptyList(),
    val subtotalPaise: Long = 0L,
    val discountPaise: Long = 0L,
    val totalPaise: Long = 0L,
    val selectedPaymentMethod: String = "UPI_QR",
    val completedSale: PosSale? = null,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

class PosViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val processPosSaleUseCase: ProcessPosSaleUseCase,
    private val pricingUseCase: CalculatePricingUseCase = CalculatePricingUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect { list ->
                _uiState.value = _uiState.value.copy(products = list)
            }
        }
    }

    fun addItemToSale(product: Product, quantity: Int) {
        val lineTotal = pricingUseCase.calculateLineTotalPaise(
            measurementType = product.measurementType,
            unitPricePaise = product.unitPricePaise,
            quantity = quantity
        )

        val item = PosSaleItem(
            productId = product.productId,
            name = product.name,
            quantity = quantity,
            unit = product.baseUnit,
            unitPricePaise = product.unitPricePaise,
            lineTotalPaise = lineTotal
        )

        val current = _uiState.value.cartItems.toMutableList()
        current.add(item)
        recalculateTotals(current)
    }

    fun removeItemFromSale(index: Int) {
        val current = _uiState.value.cartItems.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            recalculateTotals(current)
        }
    }

    private fun recalculateTotals(items: List<PosSaleItem>) {
        val subtotal = items.sumOf { it.lineTotalPaise }
        val discount = _uiState.value.discountPaise
        val total = Math.max(0L, subtotal - discount)
        _uiState.value = _uiState.value.copy(
            cartItems = items,
            subtotalPaise = subtotal,
            totalPaise = total
        )
    }

    fun setPaymentMethod(method: String) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
    }

    fun completeSale() {
        val items = _uiState.value.cartItems
        if (items.isEmpty()) return

        _uiState.value = _uiState.value.copy(isProcessing = true, errorMessage = null)
        viewModelScope.launch {
            val sale = PosSale(
                cashierId = "cashier_01",
                cashierName = "Counter Staff",
                items = items,
                subtotalPaise = _uiState.value.subtotalPaise,
                discountPaise = _uiState.value.discountPaise,
                totalPaise = _uiState.value.totalPaise,
                paymentMethod = _uiState.value.selectedPaymentMethod
            )

            when (val res = processPosSaleUseCase(sale)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        completedSale = sale.copy(
                            saleId = res.data,
                            saleNumber = "POS-${System.currentTimeMillis() % 100000}"
                        ),
                        cartItems = emptyList(),
                        subtotalPaise = 0L,
                        totalPaise = 0L
                    )
                }

                is Result.Error -> {
                    _uiState.value =
                        _uiState.value.copy(isProcessing = false, errorMessage = res.message)
                }

                is Result.Loading -> {}
            }
        }
    }

    fun dismissReceipt() {
        _uiState.value = _uiState.value.copy(completedSale = null)
    }
}
