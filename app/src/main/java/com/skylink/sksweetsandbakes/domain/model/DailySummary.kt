package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DailySummary(
    val date: String = "",
    val grossSalesPaise: Long = 0L,
    val discountsPaise: Long = 0L,
    val netSalesPaise: Long = 0L,
    val refundsPaise: Long = 0L,
    val cogsPaise: Long = 0L,
    val grossProfitPaise: Long = 0L,
    val operatingExpensesPaise: Long = 0L,
    val operatingProfitPaise: Long = 0L,
    val orderCount: Int = 0,
    val posSaleCount: Int = 0,
    val onlineSaleCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
enum class DiscountType {
    PERCENTAGE,
    FLAT
}

@Serializable
data class Coupon(
    val couponId: String = "",
    val code: String = "",
    val discountType: DiscountType = DiscountType.PERCENTAGE,
    val discountValue: Int = 10,
    val maxDiscountPaise: Long = 10000L,
    val minOrderValuePaise: Long = 50000L,
    val validFrom: Long = 0L,
    val validUntil: Long = Long.MAX_VALUE,
    val isActive: Boolean = true
)

@Serializable
data class Review(
    val reviewId: String = "",
    val productId: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val rating: Int = 5,
    val comment: String = "",
    val images: List<String> = emptyList(),
    val isVerifiedPurchase: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class UserProfile(
    val userId: String = "",
    val phoneNumber: String = "",
    val displayName: String = "",
    val email: String = "",
    val role: String = "customer",
    val linkedProviderIds: Set<String> = emptySet(),
    val loyaltyPoints: Int = 0,
    val avatarUrl: String = "",
    val defaultAddress: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
