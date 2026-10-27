package com.skylink.sksweetsandbakes.data.favourites.responsemodel

import com.skylink.sksweetsandbakes.domain.favourites.model.FavouriteItem
import kotlinx.serialization.Serializable

@Serializable
data class FavouritesListResponseModel(
    val items: List<FavouriteItem>
)
typealias FavouritesListResponse = FavouritesListResponseModel
