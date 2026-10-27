package com.skylink.sksweetsandbakes.data.dashboard.responsemodel

import com.skylink.sksweetsandbakes.domain.dashboard.usecase.FinancialStatement
import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.domain.model.PosSale
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponseModel(
    val products: List<Product>
)

@Serializable
data class InventoryListResponseModel(
    val items: List<Inventory>
)

@Serializable
data class ExpenseListResponseModel(
    val expenses: List<Expense>
)

@Serializable
data class PosSalesListResponseModel(
    val sales: List<PosSale>
)

data class FinancialStatementResponseModel(
    val statement: FinancialStatement
)

typealias ProductListResponse = ProductListResponseModel
typealias InventoryListResponse = InventoryListResponseModel
typealias ExpenseListResponse = ExpenseListResponseModel
typealias PosSalesListResponse = PosSalesListResponseModel
typealias FinancialStatementResponse = FinancialStatementResponseModel
