package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import com.skylink.sksweetsandbakes.domain.model.PosSale

data class FinancialStatement(
    val grossSalesPaise: Long = 0L,
    val discountsPaise: Long = 0L,
    val netSalesPaise: Long = 0L,
    val cogsPaise: Long = 0L,
    val grossProfitPaise: Long = 0L,
    val operatingExpensesPaise: Long = 0L,
    val operatingProfitPaise: Long = 0L,
    val totalOrdersCount: Int = 0,
    val posSalesCount: Int = 0,
    val onlineSalesCount: Int = 0,
    val averageOrderValuePaise: Long = 0L,
    val isEstimatedCogs: Boolean = false
)

class CalculateFinancialsUseCase {

    fun generateStatement(
        orders: List<Order>,
        posSales: List<PosSale>,
        expenses: List<Expense>,
        estimatedCogsRatio: Double = 0.60
    ): FinancialStatement {
        var onlineGross = 0L
        var onlineDiscounts = 0L
        var validOnlineCount = 0

        for (order in orders) {
            if (order.status != OrderStatus.CANCELLED && order.status != OrderStatus.REFUNDED) {
                onlineGross += order.pricing.subtotalPaise
                onlineDiscounts += order.pricing.discountPaise
                validOnlineCount++
            }
        }

        var posGross = 0L
        var posDiscounts = 0L
        for (sale in posSales) {
            posGross += sale.subtotalPaise
            posDiscounts += sale.discountPaise
        }

        val totalGross = onlineGross + posGross
        val totalDiscounts = onlineDiscounts + posDiscounts
        val netSales = totalGross - totalDiscounts

        val cogs = Math.round(netSales * estimatedCogsRatio)
        val grossProfit = netSales - cogs

        var operatingExpenses = 0L
        for (exp in expenses) {
            operatingExpenses += exp.amountPaise
        }

        val operatingProfit = grossProfit - operatingExpenses
        val totalSalesCount = validOnlineCount + posSales.size
        val aov = if (totalSalesCount > 0) netSales / totalSalesCount else 0L

        return FinancialStatement(
            grossSalesPaise = totalGross,
            discountsPaise = totalDiscounts,
            netSalesPaise = netSales,
            cogsPaise = cogs,
            grossProfitPaise = grossProfit,
            operatingExpensesPaise = operatingExpenses,
            operatingProfitPaise = operatingProfit,
            totalOrdersCount = totalSalesCount,
            posSalesCount = posSales.size,
            onlineSalesCount = validOnlineCount,
            averageOrderValuePaise = aov,
            isEstimatedCogs = true
        )
    }
}
