package com.skylink.sksweetsandbakes.data.cart.responsemodel

import com.skylink.sksweetsandbakes.domain.model.Cart
import com.skylink.sksweetsandbakes.domain.model.Coupon
import kotlinx.serialization.Serializable

@Serializable
data class CartResponseModel(
    val cart: Cart
)

@Serializable
data class CouponValidationResponseModel(
    val coupon: Coupon,
    val isValid: Boolean = true
)
typealias CartResponse = CartResponseModel
typealias CouponValidationResponse = CouponValidationResponseModel
