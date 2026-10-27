package com.skylink.sksweetsandbakes.domain.wishlist.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.wishlist.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class GetWishlistUseCase(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke(userId: String = "usr_demo_01"): Flow<Set<String>> =
        wishlistRepository.getWishlistProductIds(userId)
}

class ToggleWishlistUseCase(
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(
        productId: String,
        userId: String = "usr_demo_01"
    ): Result<Boolean> =
        wishlistRepository.toggleWishlist(userId, productId)
}
