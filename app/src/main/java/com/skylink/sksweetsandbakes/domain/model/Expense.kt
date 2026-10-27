package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ExpenseCategory {
    RENT,
    WAGES,
    ELECTRICITY,
    PACKAGING,
    DELIVERY,
    GATEWAY_FEE,
    REPAIRS,
    MISCELLANEOUS
}

@Serializable
data class Expense(
    val expenseId: String = "",
    val category: ExpenseCategory = ExpenseCategory.MISCELLANEOUS,
    val title: String = "",
    val amountPaise: Long = 0L,
    val date: String = "",
    val paidTo: String = "",
    val receiptImageUrl: String? = null,
    val recordedBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
