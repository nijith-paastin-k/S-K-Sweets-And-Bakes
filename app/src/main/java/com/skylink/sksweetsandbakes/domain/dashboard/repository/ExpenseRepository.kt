package com.skylink.sksweetsandbakes.domain.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense): Result<String>
    suspend fun deleteExpense(expenseId: String): Result<Unit>
}
