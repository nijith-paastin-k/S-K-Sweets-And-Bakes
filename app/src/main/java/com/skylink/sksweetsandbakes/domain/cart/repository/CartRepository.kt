package com.skylink.sksweetsandbakes.domain.cart.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Cart
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.Coupon
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val cart: Flow<Cart>
    suspend fun addItem(item: CartItem)
    suspend fun updateQuantity(productId: String, quantity: Int)
    suspend fun removeItem(productId: String)
    suspend fun applyCoupon(couponCode: String): Result<Coupon>
    suspend fun removeCoupon()
    suspend fun clearCart()
}
