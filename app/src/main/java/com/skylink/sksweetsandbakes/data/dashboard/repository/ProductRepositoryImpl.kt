package com.skylink.sksweetsandbakes.data.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.data.local.SKSweetsDatabaseHelper
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.model.Review
import com.skylink.sksweetsandbakes.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val databaseHelper: SKSweetsDatabaseHelper? = null
) : ProductRepository {

    private val _categories = MutableStateFlow<List<Category>>(
        databaseHelper?.getAllCategories()?.ifEmpty { SeedData.categories } ?: SeedData.categories
    )
    private val _products = MutableStateFlow<List<Product>>(
        databaseHelper?.getAllProducts()?.ifEmpty { SeedData.products } ?: SeedData.products
    )
    private val _wishlistIds =
        MutableStateFlow<Set<String>>(setOf("prod_kaju_katli", "prod_motichoor_laddu"))
    private val _reviews = MutableStateFlow<Map<String, List<Review>>>(
        mapOf(
            "prod_kaju_katli" to listOf(
                Review(
                    reviewId = "rev_1",
                    productId = "prod_kaju_katli",
                    customerName = "Priya Sundaram",
                    rating = 5,
                    comment = "Authentic diamond cuts with genuine silver vark! Delivered fresh and soft within 30 minutes.",
                    images = listOf(
                        "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500",
                        "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=500"
                    ),
                    isVerifiedPurchase = true
                ),
                Review(
                    reviewId = "rev_2",
                    productId = "prod_kaju_katli",
                    customerName = "Arun Venkatesh",
                    rating = 5,
                    comment = "Ordered 2.75 kg for Diwali family gathering. Premium packaging and exquisite taste!",
                    images = listOf(
                        "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?w=500"
                    ),
                    isVerifiedPurchase = true
                )
            ),
            "prod_mysore_pak" to listOf(
                Review(
                    reviewId = "rev_3",
                    productId = "prod_mysore_pak",
                    customerName = "Deepa R.",
                    rating = 5,
                    comment = "Melt in the mouth pure cow ghee goodness! Best Mysore Pak in town.",
                    images = listOf(
                        "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=500"
                    ),
                    isVerifiedPurchase = true
                )
            ),
            "prod_rasgulla" to listOf(
                Review(
                    reviewId = "rev_4",
                    productId = "prod_rasgulla",
                    customerName = "Ananya Banerjee",
                    rating = 5,
                    comment = "Spongy, light, and perfectly sweet. Tastes like home in Kolkata!",
                    images = listOf(
                        "https://images.unsplash.com/photo-1541832676-9b763b0239ab?w=500"
                    ),
                    isVerifiedPurchase = true
                )
            )
        )
    )

    override fun getCategories(): Flow<List<Category>> = _categories.asStateFlow()

    override fun getProducts(categoryId: String?): Flow<List<Product>> {
        return _products.map { list ->
            if (categoryId.isNullOrBlank() || categoryId == "{categoryId}" || categoryId.equals(
                    "all",
                    ignoreCase = true
                )
            ) {
                list
            } else {
                val clean = categoryId.trim().lowercase()
                list.filter {
                    it.categoryId.trim().equals(clean, ignoreCase = true) ||
                            it.categoryName.trim().equals(clean, ignoreCase = true) ||
                            it.categoryId.trim().contains(clean, ignoreCase = true) ||
                            clean.contains(it.categoryId.trim(), ignoreCase = true) ||
                            it.categoryName.trim().contains(clean, ignoreCase = true) ||
                            clean.contains(it.categoryName.trim(), ignoreCase = true)
                }
            }
        }
    }

    override fun getFeaturedProducts(): Flow<List<Product>> {
        return _products.map { list ->
            val featured = list.filter { it.isFeatured }
            featured.ifEmpty { list.take(8) }
        }
    }

    override suspend fun getProductById(productId: String): Result<Product> {
        val cleanId = productId.trim()
        val prod = _products.value.find {
            it.productId.equals(cleanId, ignoreCase = true) ||
                    it.sku.equals(cleanId, ignoreCase = true) ||
                    it.name.equals(cleanId, ignoreCase = true) ||
                    it.productId.contains(cleanId, ignoreCase = true) ||
                    cleanId.contains(it.productId, ignoreCase = true)
        } ?: SeedData.products.find {
            it.productId.equals(cleanId, ignoreCase = true) ||
                    it.sku.equals(cleanId, ignoreCase = true) ||
                    it.name.contains(cleanId, ignoreCase = true)
        } ?: _products.value.firstOrNull()

        return if (prod != null) Result.Success(prod)
        else Result.Error(NoSuchElementException("Product $productId not found"))
    }

    override suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return _products.value
        val lower = query.lowercase().trim()
        val results = _products.value.filter {
            it.name.lowercase().contains(lower) ||
                    it.categoryName.lowercase().contains(lower) ||
                    it.description.lowercase().contains(lower)
        }
        return results.ifEmpty { _products.value.take(4) }
    }

    override suspend fun saveProduct(product: Product): Result<String> {
        val current = _products.value.toMutableList()
        val index = current.indexOfFirst { it.productId == product.productId }
        val id =
            if (product.productId.isBlank()) "prod_${System.currentTimeMillis()}" else product.productId
        val updated = product.copy(productId = id)

        if (index >= 0) {
            current[index] = updated
        } else {
            current.add(0, updated)
        }
        _products.value = current
        return Result.Success(id)
    }

    override suspend fun archiveProduct(productId: String): Result<Unit> {
        val current = _products.value.map {
            if (it.productId == productId) it.copy(isActive = false) else it
        }
        _products.value = current
        return Result.Success(Unit)
    }

    override fun getWishlistProductIds(userId: String): Flow<Set<String>> =
        _wishlistIds.asStateFlow()

    override suspend fun toggleWishlist(userId: String, productId: String): Result<Boolean> {
        val current = _wishlistIds.value.toMutableSet()
        val added = if (current.contains(productId)) {
            current.remove(productId)
            false
        } else {
            current.add(productId)
            true
        }
        _wishlistIds.value = current
        return Result.Success(added)
    }

    override fun getProductReviews(productId: String): Flow<List<Review>> {
        return _reviews.map { it[productId] ?: emptyList() }
    }

    override suspend fun addReview(review: Review): Result<String> {
        val current = _reviews.value.toMutableMap()
        val list = (current[review.productId] ?: emptyList()).toMutableList()
        val revId = "rev_${System.currentTimeMillis()}"
        list.add(0, review.copy(reviewId = revId))
        current[review.productId] = list
        _reviews.value = current
        return Result.Success(revId)
    }
}
