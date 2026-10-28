package com.skylink.sksweetsandbakes.data.orders.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class CashfreePaymentRequestModel(
    val orderId: String,
    val paymentSessionId: String,
    val orderAmount: Double,
    val orderCurrency: String = "INR",
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val description: String = "S K Sweets & Bakes Order Payment"
)

typealias CashfreePaymentRequest = CashfreePaymentRequestModel
