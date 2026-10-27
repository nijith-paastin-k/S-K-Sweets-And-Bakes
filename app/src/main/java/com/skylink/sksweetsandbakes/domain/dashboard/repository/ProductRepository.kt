package com.skylink.sksweetsandbakes.domain.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getCategories(): Flow<List<Category>>
    fun getProducts(categoryId: String? = null): Flow<List<Product>>
    fun getFeaturedProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: String): Result<Product>
    suspend fun searchProducts(query: String): List<Product>
    suspend fun saveProduct(product: Product): Result<String>
    suspend fun archiveProduct(productId: String): Result<Unit>
    fun getWishlistProductIds(userId: String): Flow<Set<String>>
    suspend fun toggleWishlist(userId: String, productId: String): Result<Boolean>
    fun getProductReviews(productId: String): Flow<List<Review>>
    suspend fun addReview(review: Review): Result<String>
}
