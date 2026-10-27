package com.skylink.sksweetsandbakes.data.orders.responsemodel

import kotlinx.serialization.Serializable

@Serializable
data class RazorpayPaymentResponseModel(
    val isSuccess: Boolean,
    val paymentId: String? = null,
    val orderId: String? = null,
    val signature: String? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null
)

typealias RazorpayPaymentResponse = RazorpayPaymentResponseModel
