package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.FinancialStatement
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetFinancialStatementUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetInventoryListUseCase
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetCustomerOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OwnerDashboardUiState(
    val financialStatement: FinancialStatement = FinancialStatement(),
    val lowStockItems: List<Inventory> = emptyList(),
    val recentOrders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

class OwnerDashboardViewModel(
    private val getFinancialStatementUseCase: GetFinancialStatementUseCase,
    private val getInventoryListUseCase: GetInventoryListUseCase,
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerDashboardUiState())
    val uiState: StateFlow<OwnerDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val res = getFinancialStatementUseCase(0)) {
                is Result.Success -> {
                    _uiState.value =
                        _uiState.value.copy(financialStatement = res.data, isLoading = false)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }

                is Result.Loading -> {}
            }
        }

        viewModelScope.launch {
            getInventoryListUseCase().collect { list ->
                val lowStock = list.filter { it.onHandStock <= it.lowStockThreshold }
                _uiState.value = _uiState.value.copy(lowStockItems = lowStock)
            }
        }

        viewModelScope.launch {
            getCustomerOrdersUseCase("usr_demo_01").collect { orders ->
                _uiState.value = _uiState.value.copy(recentOrders = orders.take(5))
            }
        }
    }
}
