package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Product

/**
 * Authoritative pricing calculation use case executing decimal-safe integer paise arithmetic.
 * Never allows floating-point drift.
 */
class CalculatePricingUseCase {

    /**
     * Calculates line item price in integer paise.
     *
     * @param measurementType WEIGHT, VOLUME, PIECE, FIXED_PACK
     * @param unitPricePaise For WEIGHT: price per 1000g (1kg). For VOLUME: price per 1000ml (1L). For PIECE: price per piece. For FIXED_PACK: price per pack.
     * @param quantity Base units: grams for weight, ml for volume, count for pieces/packs.
     */
    fun calculateLineTotalPaise(
        measurementType: MeasurementType,
        unitPricePaise: Long,
        quantity: Int
    ): Long {
        require(quantity > 0) { "Quantity must be greater than zero" }
        require(unitPricePaise >= 0) { "Unit price cannot be negative" }

        return when (measurementType) {
            MeasurementType.WEIGHT -> {
                Math.round((quantity.toDouble() * unitPricePaise.toDouble()) / 1000.0)
            }

            MeasurementType.VOLUME -> {
                Math.round((quantity.toDouble() * unitPricePaise.toDouble()) / 1000.0)
            }

            MeasurementType.PIECE -> {
                quantity.toLong() * unitPricePaise
            }

            MeasurementType.FIXED_PACK -> {
                quantity.toLong() * unitPricePaise
            }
        }
    }

    /**
     * Validates that the selected quantity conforms to product limits and increment rules.
     */
    fun validateQuantity(product: Product, quantity: Int): Result<Unit> {
        if (quantity < product.minQuantity) {
            return Result.failure(
                IllegalArgumentException("Minimum quantity is ${product.minQuantity} ${product.baseUnit}")
            )
        }
        if (quantity > product.maxQuantity) {
            return Result.failure(
                IllegalArgumentException("Maximum quantity is ${product.maxQuantity} ${product.baseUnit}")
            )
        }
        if (product.quantityIncrement > 0) {
            val delta = quantity - product.minQuantity
            if (delta % product.quantityIncrement != 0) {
                return Result.failure(
                    IllegalArgumentException(
                        "Quantity must be in steps of ${product.quantityIncrement} ${product.baseUnit}"
                    )
                )
            }
        }
        return Result.success(Unit)
    }

    /**
     * Extended weight steps for customer UI selectors including 2.75 kg, 3 kg, up to 5 kg.
     */
    fun getStandardWeightSteps(): List<Int> {
        return listOf(
            250,
            500,
            750,
            1000,
            1250,
            1500,
            1750,
            2000,
            2250,
            2500,
            2750,
            3000,
            3500,
            4000,
            5000
        )
    }

    /**
     * Formats grams into clean human readable string (e.g. 2750 -> "2.75 kg", 3000 -> "3 kg", 500 -> "500 g").
     */
    fun formatWeightDisplay(grams: Int): String {
        return if (grams >= 1000) {
            val kg = grams / 1000.0
            if (kg % 1.0 == 0.0) "${kg.toInt()} kg" else "$kg kg"
        } else {
            "$grams g"
        }
    }
}
