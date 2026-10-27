package com.skylink.sksweetsandbakes.data.favourites.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.favourites.repository.FavouritesRepository
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val productRepository: ProductRepository
) : FavouritesRepository {

    override fun getFavourites(userId: String): Flow<List<Product>> {
        return combine(
            productRepository.getWishlistProductIds(userId),
            productRepository.getProducts()
        ) { ids, prods ->
            prods.filter { ids.contains(it.productId) }
        }
    }

    override suspend fun toggleFavourite(userId: String, productId: String): Result<Boolean> {
        return productRepository.toggleWishlist(userId, productId)
    }

    override fun isFavourite(userId: String, productId: String): Flow<Boolean> {
        return productRepository.getWishlistProductIds(userId).map { it.contains(productId) }
    }
}
