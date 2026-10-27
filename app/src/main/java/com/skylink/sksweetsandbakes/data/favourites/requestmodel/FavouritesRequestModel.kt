package com.skylink.sksweetsandbakes.data.favourites.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class ToggleFavouriteRequestModel(
    val userId: String,
    val productId: String
)
typealias ToggleFavouriteRequest = ToggleFavouriteRequestModel
