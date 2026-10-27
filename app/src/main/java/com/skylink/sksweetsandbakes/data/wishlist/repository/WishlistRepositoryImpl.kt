package com.skylink.sksweetsandbakes.data.wishlist.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.repository.ProductRepository
import com.skylink.sksweetsandbakes.domain.wishlist.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class WishlistRepositoryImpl(
    private val productRepository: ProductRepository
) : WishlistRepository {

    override fun getWishlistProductIds(userId: String): Flow<Set<String>> {
        return productRepository.getWishlistProductIds(userId)
    }

    override suspend fun toggleWishlist(userId: String, productId: String): Result<Boolean> {
        return productRepository.toggleWishlist(userId, productId)
    }
}
