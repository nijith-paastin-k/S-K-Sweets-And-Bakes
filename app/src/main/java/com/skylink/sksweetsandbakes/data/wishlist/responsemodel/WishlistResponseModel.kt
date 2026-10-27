package com.skylink.sksweetsandbakes.data.wishlist.responsemodel

import kotlinx.serialization.Serializable

@Serializable
data class WishlistProductIdsResponseModel(
    val productIds: Set<String>
)
typealias WishlistProductIdsResponse = WishlistProductIdsResponseModel
