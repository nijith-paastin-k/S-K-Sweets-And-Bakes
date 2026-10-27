package com.skylink.sksweetsandbakes.data.orders.responsemodel

import com.skylink.sksweetsandbakes.domain.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseModel(
    val order: Order
)

@Serializable
data class OrderListResponseModel(
    val orders: List<Order>
)

data class InvoicePdfResponseModel(
    val filePath: String,
    val isGenerated: Boolean = true
)

@Serializable
data class NotificationResponseModel(
    val isDelivered: Boolean = true
)
typealias OrderResponse = OrderResponseModel
typealias OrderListResponse = OrderListResponseModel
typealias InvoicePdfResponse = InvoicePdfResponseModel
typealias NotificationResponse = NotificationResponseModel
