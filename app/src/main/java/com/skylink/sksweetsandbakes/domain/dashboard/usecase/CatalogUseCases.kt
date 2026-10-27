package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.model.Review
import com.skylink.sksweetsandbakes.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(categoryId: String? = null): Flow<List<Product>> =
        productRepository.getProducts(categoryId)

    fun getFeatured(): Flow<List<Product>> =
        productRepository.getFeaturedProducts()

    fun getCategories(): Flow<List<Category>> =
        productRepository.getCategories()
}

class GetProductDetailUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<Product> =
        productRepository.getProductById(productId)

    fun getReviews(productId: String): Flow<List<Review>> =
        productRepository.getProductReviews(productId)
}

class SearchProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(query: String): List<Product> =
        productRepository.searchProducts(query)
}

class WishlistUseCase(
    private val productRepository: ProductRepository
) {
    fun getWishlistProductIds(userId: String): Flow<Set<String>> =
        productRepository.getWishlistProductIds(userId)

    suspend fun toggleWishlist(userId: String, productId: String): Result<Boolean> =
        productRepository.toggleWishlist(userId, productId)
}

class AddReviewUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(review: Review): Result<String> =
        productRepository.addReview(review)
}
