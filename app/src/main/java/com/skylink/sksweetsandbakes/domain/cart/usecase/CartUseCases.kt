package com.skylink.sksweetsandbakes.domain.cart.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Cart
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.Coupon
import com.skylink.sksweetsandbakes.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class GetCartUseCase(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<Cart> = cartRepository.cart
}

class AddToCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) = cartRepository.addItem(item)
}

class UpdateCartQuantityUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int) =
        cartRepository.updateQuantity(productId, quantity)
}

class RemoveFromCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String) = cartRepository.removeItem(productId)
}

class ClearCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke() = cartRepository.clearCart()
}

class ApplyCouponUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(couponCode: String): Result<Coupon> =
        cartRepository.applyCoupon(couponCode)
}

class RemoveCouponUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke() = cartRepository.removeCoupon()
}
