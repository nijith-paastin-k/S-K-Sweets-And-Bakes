package com.skylink.sksweetsandbakes.data.wishlist.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class ToggleWishlistRequestModel(
    val userId: String,
    val productId: String
)
typealias ToggleWishlistRequest = ToggleWishlistRequestModel
