package com.skylink.sksweetsandbakes.data.cart.requestmodel

import com.skylink.sksweetsandbakes.domain.model.CartItem
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartRequestModel(
    val item: CartItem
)

@Serializable
data class UpdateCartQuantityRequestModel(
    val productId: String,
    val quantity: Int
)

@Serializable
data class ApplyCouponRequestModel(
    val couponCode: String
)
typealias AddToCartRequest = AddToCartRequestModel
typealias UpdateCartQuantityRequest = UpdateCartQuantityRequestModel
typealias ApplyCouponRequest = ApplyCouponRequestModel
