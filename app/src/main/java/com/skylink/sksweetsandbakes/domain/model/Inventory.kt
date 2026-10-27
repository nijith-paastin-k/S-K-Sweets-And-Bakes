package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MovementType {
    RECEIVE,
    POS_SALE,
    ONLINE_RESERVATION,
    ONLINE_FULFILLMENT,
    ONLINE_RELEASE,
    MANUAL_ADJUSTMENT,
    WASTAGE,
    RETURN
}

@Serializable
data class Inventory(
    val productId: String = "",
    val productName: String = "",
    val onHandStock: Int = 0,
    val reservedStock: Int = 0,
    val availableStock: Int = 0,
    val unit: String = "g",
    val lowStockThreshold: Int = 1000,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Serializable
data class InventoryMovement(
    val movementId: String = "",
    val productId: String = "",
    val productName: String = "",
    val type: MovementType = MovementType.MANUAL_ADJUSTMENT,
    val quantityChange: Int = 0,
    val previousOnHand: Int = 0,
    val newOnHand: Int = 0,
    val referenceId: String = "",
    val operatorId: String = "",
    val operatorName: String = "",
    val reason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
