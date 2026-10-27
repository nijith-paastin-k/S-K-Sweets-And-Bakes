package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesUseCase(
    private val expenseRepository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> = expenseRepository.getExpenses()
}

class RecordExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<String> =
        expenseRepository.addExpense(expense)
}

class DeleteExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expenseId: String): Result<Unit> =
        expenseRepository.deleteExpense(expenseId)
}
