package com.skylink.sksweetsandbakes.presentation.orders.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.orders.usecase.CancelOrderUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GenerateInvoicePdfUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetCustomerOrdersUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetOrderDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val currentOrder: Order? = null,
    val isLoadingOrders: Boolean = true,
    val isLoadingDetail: Boolean = false,
    val isGeneratingInvoice: Boolean = false,
    val errorMessage: String? = null,
    val invoiceFile: File? = null
) {
    val isLoading: Boolean get() = isLoadingOrders || isLoadingDetail
}

class OrdersViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val generateInvoicePdfUseCase: GenerateInvoicePdfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            getCustomerOrdersUseCase("usr_demo_01").collect { list ->
                _uiState.value = _uiState.value.copy(
                    orders = list,
                    isLoadingOrders = false,
                    currentOrder = _uiState.value.currentOrder ?: list.firstOrNull()
                )
            }
        }
    }

    fun loadOrderDetail(orderId: String) {
        val cleanId = orderId.trim()
        val existing = _uiState.value.orders.find {
            it.orderId.equals(cleanId, ignoreCase = true) ||
                    it.orderNumber.equals(cleanId, ignoreCase = true)
        }
        if (existing != null) {
            _uiState.value = _uiState.value.copy(
                currentOrder = existing,
                isLoadingDetail = false,
                errorMessage = null
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoadingDetail = true, errorMessage = null)
        viewModelScope.launch {
            when (val res = getOrderDetailUseCase(cleanId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        currentOrder = res.data,
                        isLoadingDetail = false,
                        errorMessage = null
                    )
                }

                is Result.Error -> {
                    val fallback = _uiState.value.orders.firstOrNull()
                    _uiState.value = _uiState.value.copy(
                        currentOrder = fallback,
                        errorMessage = if (fallback == null) res.message else null,
                        isLoadingDetail = false
                    )
                }

                is Result.Loading -> {}
            }
        }
    }

    fun cancelOrder(orderId: String, reason: String) {
        viewModelScope.launch {
            cancelOrderUseCase(orderId, reason)
            loadOrderDetail(orderId)
        }
    }

    fun downloadInvoice(context: Context, order: Order) {
        _uiState.value = _uiState.value.copy(isGeneratingInvoice = true)
        viewModelScope.launch {
            when (val result = generateInvoicePdfUseCase(order)) {
                is Result.Success -> {
                    val file = result.data
                    _uiState.value = _uiState.value.copy(
                        isGeneratingInvoice = false,
                        invoiceFile = file
                    )

                    Toast.makeText(context, "Invoice downloaded: ${file.name}", Toast.LENGTH_SHORT)
                        .show()

                    // Open via FileProvider intent chooser
                    try {
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )

                        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/pdf")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }

                        val chooser =
                            Intent.createChooser(viewIntent, "Open Tax Invoice PDF").apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        context.startActivity(chooser)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Saved to: ${file.absolutePath}", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isGeneratingInvoice = false,
                        errorMessage = result.message
                    )
                    Toast.makeText(
                        context,
                        "Error generating invoice: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Loading -> {}
            }
        }
    }
}
