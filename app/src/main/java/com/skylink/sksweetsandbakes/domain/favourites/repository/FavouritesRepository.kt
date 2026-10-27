package com.skylink.sksweetsandbakes.domain.favourites.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getFavourites(userId: String): Flow<List<Product>>
    suspend fun toggleFavourite(userId: String, productId: String): Result<Boolean>
    fun isFavourite(userId: String, productId: String): Flow<Boolean>
}
