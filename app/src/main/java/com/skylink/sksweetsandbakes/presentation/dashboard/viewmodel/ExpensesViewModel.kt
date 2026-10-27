package com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.DateUtils
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.DeleteExpenseUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetExpensesUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.RecordExpenseUseCase
import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val totalExpensesPaise: Long = 0L,
    val isLoading: Boolean = true
)

class ExpensesViewModel(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val recordExpenseUseCase: RecordExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            getExpensesUseCase().collect { list ->
                val total = list.sumOf { it.amountPaise }
                _uiState.value = _uiState.value.copy(
                    expenses = list,
                    totalExpensesPaise = total,
                    isLoading = false
                )
            }
        }
    }

    fun addExpense(title: String, category: ExpenseCategory, amountRupees: Double, paidTo: String) {
        val paise = CurrencyFormatter.rupeesToPaise(amountRupees)
        val exp = Expense(
            category = category,
            title = title,
            amountPaise = paise,
            date = DateUtils.toIsoDate(),
            paidTo = paidTo,
            recordedBy = "owner"
        )
        viewModelScope.launch {
            recordExpenseUseCase(exp)
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            deleteExpenseUseCase(expenseId)
        }
    }
}
