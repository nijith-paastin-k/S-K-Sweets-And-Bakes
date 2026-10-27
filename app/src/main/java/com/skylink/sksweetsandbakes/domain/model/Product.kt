package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

/**
 * Supported measurement types for sweets, savouries, beverages, and bakery packs.
 */
@Serializable
enum class MeasurementType {
    WEIGHT,
    VOLUME,
    PIECE,
    FIXED_PACK
}

@Serializable
data class Category(
    val categoryId: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val displayOrder: Int = 0,
    val isActive: Boolean = true
)

@Serializable
data class Product(
    val productId: String = "",
    val sku: String = "",
    val name: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val measurementType: MeasurementType = MeasurementType.WEIGHT,
    val baseUnit: String = "g",
    val displayUnit: String = "kg",
    val unitPricePaise: Long = 0L,
    val minQuantity: Int = 250,
    val maxQuantity: Int = 10000,
    val quantityIncrement: Int = 250,
    val shelfLifeDays: Int = 7,
    val storageInstructions: String = "",
    val ingredients: List<String> = emptyList(),
    val allergens: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val isActive: Boolean = true,
    val onHandStock: Int = 0,
    val availableStock: Int = 0,
    val lowStockThreshold: Int = 1000,
    val createdAt: Long = System.currentTimeMillis()
)
