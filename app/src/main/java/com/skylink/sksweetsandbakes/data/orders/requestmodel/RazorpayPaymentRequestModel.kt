package com.skylink.sksweetsandbakes.data.orders.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class RazorpayPaymentRequestModel(
    val amountPaise: Long,
    val currency: String = "INR",
    val orderId: String,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val description: String = "S K Sweets & Bakes Order Payment"
)

typealias RazorpayPaymentRequest = RazorpayPaymentRequestModel
