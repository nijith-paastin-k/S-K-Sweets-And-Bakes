package com.skylink.sksweetsandbakes.data.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculateFinancialsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.FinancialStatement
import com.skylink.sksweetsandbakes.domain.repository.AnalyticsRepository
import com.skylink.sksweetsandbakes.domain.repository.ExpenseRepository
import com.skylink.sksweetsandbakes.domain.repository.OrderRepository
import com.skylink.sksweetsandbakes.domain.repository.PosRepository
import kotlinx.coroutines.flow.first

class AnalyticsRepositoryImpl(
    private val orderRepository: OrderRepository,
    private val posRepository: PosRepository,
    private val expenseRepository: ExpenseRepository,
    private val calculateFinancialsUseCase: CalculateFinancialsUseCase = CalculateFinancialsUseCase()
) : AnalyticsRepository {

    override suspend fun getFinancialStatement(daysAgo: Int): Result<FinancialStatement> {
        val orders = orderRepository.getAllOrders().first()
        val posSales = posRepository.getRecentSales().first()
        val expenses = expenseRepository.getExpenses().first()

        val statement = calculateFinancialsUseCase.generateStatement(
            orders = orders,
            posSales = posSales,
            expenses = expenses
        )
        return Result.Success(statement)
    }
}
