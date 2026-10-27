package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PosSaleItem(
    val productId: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val unit: String = "g",
    val unitPricePaise: Long = 0L,
    val lineTotalPaise: Long = 0L
)

@Serializable
data class PosSale(
    val saleId: String = "",
    val saleNumber: String = "",
    val cashierId: String = "",
    val cashierName: String = "",
    val items: List<PosSaleItem> = emptyList(),
    val subtotalPaise: Long = 0L,
    val discountPaise: Long = 0L,
    val totalPaise: Long = 0L,
    val paymentMethod: String = "CASH",
    val timestamp: Long = System.currentTimeMillis()
)
