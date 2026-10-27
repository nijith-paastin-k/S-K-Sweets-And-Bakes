package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.FinancialStatement
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetFinancialStatementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AnalyticsUiState(
    val statement: FinancialStatement = FinancialStatement(),
    val selectedPeriod: String = "Today",
    val isLoading: Boolean = true
)

class AnalyticsViewModel(
    private val getFinancialStatementUseCase: GetFinancialStatementUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadStatement(0)
    }

    fun loadStatement(daysAgo: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val res = getFinancialStatementUseCase(daysAgo)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        statement = res.data,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }

                is Result.Loading -> {}
            }
        }
    }

    fun onPeriodSelected(period: String) {
        val days = when (period) {
            "Yesterday" -> 1
            "Last 7 Days" -> 7
            "Last 30 Days" -> 30
            else -> 0
        }
        _uiState.value = _uiState.value.copy(selectedPeriod = period)
        loadStatement(days)
    }
}
