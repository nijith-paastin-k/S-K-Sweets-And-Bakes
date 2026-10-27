package com.skylink.sksweetsandbakes.domain.favourites.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.favourites.repository.FavouritesRepository
import com.skylink.sksweetsandbakes.domain.model.Product
import kotlinx.coroutines.flow.Flow

class GetFavouritesUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    operator fun invoke(userId: String = "usr_demo_01"): Flow<List<Product>> =
        favouritesRepository.getFavourites(userId)
}

class ToggleFavouriteUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    suspend operator fun invoke(
        productId: String,
        userId: String = "usr_demo_01"
    ): Result<Boolean> =
        favouritesRepository.toggleFavourite(userId, productId)
}

class IsFavouriteUseCase(
    private val favouritesRepository: FavouritesRepository
) {
    operator fun invoke(productId: String, userId: String = "usr_demo_01"): Flow<Boolean> =
        favouritesRepository.isFavourite(userId, productId)
}
