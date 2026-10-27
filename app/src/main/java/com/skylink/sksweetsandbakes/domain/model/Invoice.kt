package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceItem(
    val productName: String,
    val quantity: Int,
    val unit: String,
    val unitPricePaise: Long,
    val lineTotalPaise: Long
)

@Serializable
data class Invoice(
    val invoiceNumber: String,
    val orderNumber: String,
    val orderId: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String?,
    val fulfillmentType: FulfillmentType,
    val paymentMethod: PaymentMethod,
    val paymentId: String,
    val paymentStatus: String,
    val items: List<InvoiceItem>,
    val subtotalPaise: Long,
    val discountPaise: Long,
    val packagingFeePaise: Long,
    val deliveryFeePaise: Long,
    val taxPaise: Long,
    val grandTotalPaise: Long,
    val issuedAt: Long = System.currentTimeMillis()
)
