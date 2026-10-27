package com.skylink.sksweetsandbakes.domain.wishlist.repository

import com.skylink.sksweetsandbakes.core.common.Result
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlistProductIds(userId: String = "usr_demo_01"): Flow<Set<String>>
    suspend fun toggleWishlist(userId: String = "usr_demo_01", productId: String): Result<Boolean>
}
