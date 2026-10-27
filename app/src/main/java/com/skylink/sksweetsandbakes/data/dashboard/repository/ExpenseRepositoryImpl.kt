package com.skylink.sksweetsandbakes.data.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseRepositoryImpl : ExpenseRepository {

    private val _expenses = MutableStateFlow<List<Expense>>(SeedData.sampleExpenses)

    override fun getExpenses(): Flow<List<Expense>> = _expenses.asStateFlow()

    override suspend fun addExpense(expense: Expense): Result<String> {
        val id =
            if (expense.expenseId.isBlank()) "exp_${System.currentTimeMillis()}" else expense.expenseId
        val newExp = expense.copy(expenseId = id)
        val cur = _expenses.value.toMutableList()
        cur.add(0, newExp)
        _expenses.value = cur
        return Result.Success(id)
    }

    override suspend fun deleteExpense(expenseId: String): Result<Unit> {
        _expenses.value = _expenses.value.filterNot { it.expenseId == expenseId }
        return Result.Success(Unit)
    }
}
