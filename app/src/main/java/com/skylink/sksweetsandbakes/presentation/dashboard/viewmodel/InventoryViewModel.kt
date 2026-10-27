package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.AdjustStockUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetInventoryListUseCase
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.domain.model.InventoryMovement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InventoryUiState(
    val inventoryList: List<Inventory> = emptyList(),
    val movements: List<InventoryMovement> = emptyList(),
    val selectedInventory: Inventory? = null,
    val isLoading: Boolean = true,
    val message: String? = null
)

class InventoryViewModel(
    private val getInventoryListUseCase: GetInventoryListUseCase,
    private val adjustStockUseCase: AdjustStockUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        loadInventory()
    }

    private fun loadInventory() {
        viewModelScope.launch {
            getInventoryListUseCase().collect { list ->
                _uiState.value = _uiState.value.copy(inventoryList = list, isLoading = false)
            }
        }
        viewModelScope.launch {
            getInventoryListUseCase.getMovements().collect { movs ->
                _uiState.value = _uiState.value.copy(movements = movs)
            }
        }
    }

    fun adjustStock(productId: String, quantityChange: Int, reason: String) {
        viewModelScope.launch {
            when (val res = adjustStockUseCase(productId, quantityChange, reason, "owner")) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(message = "Stock adjusted successfully")
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(message = res.message)
                }

                is Result.Loading -> {}
            }
        }
    }
}
