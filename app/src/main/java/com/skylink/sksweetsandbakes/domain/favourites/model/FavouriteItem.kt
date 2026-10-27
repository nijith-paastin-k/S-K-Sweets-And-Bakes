package com.skylink.sksweetsandbakes.domain.favourites.model

import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class FavouriteItem(
    val product: Product,
    val addedAt: Long = System.currentTimeMillis()
)
