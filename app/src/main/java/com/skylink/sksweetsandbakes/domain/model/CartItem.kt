package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val productImageUrl: String = "",
    val measurementType: MeasurementType = MeasurementType.WEIGHT,
    val selectedQuantity: Int = 250,
    val baseUnit: String = "g",
    val displayUnit: String = "kg",
    val unitPricePaise: Long = 0L,
    val lineTotalPaise: Long = 0L
)

@Serializable
data class Cart(
    val items: List<CartItem> = emptyList(),
    val subtotalPaise: Long = 0L,
    val discountPaise: Long = 0L,
    val appliedCouponCode: String? = null,
    val deliveryFeePaise: Long = 0L,
    val packagingFeePaise: Long = 0L,
    val taxPaise: Long = 0L,
    val grandTotalPaise: Long = 0L
) {
    val totalItemCount: Int get() = items.size
    val isEmpty: Boolean get() = items.isEmpty()
}
