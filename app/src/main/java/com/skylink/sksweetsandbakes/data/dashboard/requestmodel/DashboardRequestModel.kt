package com.skylink.sksweetsandbakes.data.dashboard.requestmodel

import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.model.PosSale
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class SaveProductRequestModel(
    val product: Product
)

@Serializable
data class AdjustStockRequestModel(
    val productId: String,
    val quantityChange: Int,
    val reason: String,
    val operatorId: String
)

@Serializable
data class ReceiveStockRequestModel(
    val productId: String,
    val quantity: Int,
    val costPaise: Long,
    val invoiceRef: String,
    val operatorId: String
)

@Serializable
data class RecordExpenseRequestModel(
    val expense: Expense
)

@Serializable
data class ProcessPosSaleRequestModel(
    val sale: PosSale
)
typealias SaveProductRequest = SaveProductRequestModel
typealias AdjustStockRequest = AdjustStockRequestModel
typealias ReceiveStockRequest = ReceiveStockRequestModel
typealias RecordExpenseRequest = RecordExpenseRequestModel
typealias ProcessPosSaleRequest = ProcessPosSaleRequestModel
