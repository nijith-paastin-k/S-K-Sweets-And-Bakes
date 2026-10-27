package com.skylink.sksweetsandbakes.domain.cart.usecase

import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculatePricingUseCase
import com.skylink.sksweetsandbakes.domain.model.Cart
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.Coupon
import com.skylink.sksweetsandbakes.domain.model.DiscountType
import com.skylink.sksweetsandbakes.domain.model.FulfillmentType
import com.skylink.sksweetsandbakes.domain.model.OrderPricing

class ValidateOrderUseCase(
    private val pricingUseCase: CalculatePricingUseCase = CalculatePricingUseCase()
) {

    /**
     * Recalculates cart pricing authoritatively on server/domain model.
     */
    fun calculateCartPricing(
        items: List<CartItem>,
        fulfillmentType: FulfillmentType = FulfillmentType.DELIVERY,
        appliedCoupon: Coupon? = null
    ): OrderPricing {
        if (items.isEmpty()) {
            return OrderPricing()
        }

        var subtotalPaise = 0L
        for (item in items) {
            val lineTotal = pricingUseCase.calculateLineTotalPaise(
                measurementType = item.measurementType,
                unitPricePaise = item.unitPricePaise,
                quantity = item.selectedQuantity
            )
            subtotalPaise += lineTotal
        }

        var discountPaise = 0L
        if (appliedCoupon != null && appliedCoupon.isActive) {
            if (subtotalPaise >= appliedCoupon.minOrderValuePaise) {
                discountPaise = when (appliedCoupon.discountType) {
                    DiscountType.PERCENTAGE -> {
                        val calculated = (subtotalPaise * appliedCoupon.discountValue) / 100
                        minOf(calculated, appliedCoupon.maxDiscountPaise)
                    }

                    DiscountType.FLAT -> {
                        minOf(appliedCoupon.maxDiscountPaise, subtotalPaise)
                    }
                }
            }
        }

        val packagingFeePaise = 1500L
        val deliveryFeePaise = if (fulfillmentType == FulfillmentType.DELIVERY) {
            if (subtotalPaise >= 100000L) 0L else 4000L
        } else {
            0L
        }
        val taxableAmount = maxOf(0L, subtotalPaise - discountPaise)
        val taxPaise = Math.round(taxableAmount * 0.05)

        val grandTotalPaise =
            maxOf(0L, taxableAmount + packagingFeePaise + deliveryFeePaise + taxPaise)

        return OrderPricing(
            subtotalPaise = subtotalPaise,
            discountPaise = discountPaise,
            couponCode = appliedCoupon?.code,
            deliveryFeePaise = deliveryFeePaise,
            packagingFeePaise = packagingFeePaise,
            taxPaise = taxPaise,
            grandTotalPaise = grandTotalPaise
        )
    }

    /**
     * Validates whether order can be placed based on business constraints.
     */
    fun validateForPlacement(cart: Cart, fulfillmentType: FulfillmentType): String? {
        if (cart.isEmpty) {
            return "Your cart is empty. Please add items before placing order."
        }

        val calculatedPricing = calculateCartPricing(cart.items, fulfillmentType)
        if (calculatedPricing.grandTotalPaise <= 0L) {
            return "Invalid order amount. Please recalculate your cart."
        }

        return null
    }
}
