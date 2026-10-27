package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Supplier(
    val supplierId: String = "",
    val name: String = "",
    val contactPerson: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val address: String = "",
    val gstin: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class PurchaseItem(
    val productId: String = "",
    val productName: String = "",
    val quantityReceived: Int = 0,
    val unit: String = "g",
    val costPerUnitPaise: Long = 0L,
    val totalCostPaise: Long = 0L
)

@Serializable
data class PurchaseRecord(
    val purchaseId: String = "",
    val supplierId: String = "",
    val supplierName: String = "",
    val invoiceNumber: String = "",
    val invoiceDate: String = "",
    val items: List<PurchaseItem> = emptyList(),
    val totalCostPaise: Long = 0L,
    val receivedBy: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
