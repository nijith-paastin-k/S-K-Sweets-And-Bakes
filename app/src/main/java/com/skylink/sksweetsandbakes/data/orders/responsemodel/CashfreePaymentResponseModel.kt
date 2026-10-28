package com.skylink.sksweetsandbakes.data.orders.responsemodel

import kotlinx.serialization.Serializable

@Serializable
data class CashfreePaymentResponseModel(
    val isSuccess: Boolean,
    val orderId: String,
    val transactionId: String? = null,
    val referenceId: String? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)

typealias CashfreePaymentResponse = CashfreePaymentResponseModel
