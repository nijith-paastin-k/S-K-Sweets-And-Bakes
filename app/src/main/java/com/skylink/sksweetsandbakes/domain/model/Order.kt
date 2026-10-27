package com.skylink.sksweetsandbakes.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    PENDING_PAYMENT,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    COMPLETED,
    CANCELLED,
    REFUND_PENDING,
    REFUNDED
}

@Serializable
enum class FulfillmentType {
    DELIVERY,
    PICKUP
}

@Serializable
enum class PaymentMethod {
    UPI,
    CARD,
    NETBANKING,
    CASH_ON_PICKUP,
    RAZORPAY
}

@Serializable
data class DeliveryAddress(
    val addressId: String = "",
    val recipientName: String = "",
    val phoneNumber: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val landmark: String = "",
    val city: String = "Bengaluru",
    val state: String = "Karnataka",
    val pincode: String = "",
    val isDefault: Boolean = false
)

@Serializable
data class PickupSlot(
    val slotDate: String = "",
    val timeWindow: String = ""
)

@Serializable
data class OrderPricing(
    val subtotalPaise: Long = 0L,
    val discountPaise: Long = 0L,
    val couponCode: String? = null,
    val deliveryFeePaise: Long = 0L,
    val packagingFeePaise: Long = 0L,
    val taxPaise: Long = 0L,
    val grandTotalPaise: Long = 0L
)

@Serializable
data class OrderPayment(
    val paymentId: String = "",
    val gatewayOrderId: String = "",
    val method: PaymentMethod = PaymentMethod.UPI,
    val status: String = "PENDING",
    val paidAt: Long? = null
)

@Serializable
data class Order(
    val orderId: String = "",
    val orderNumber: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val fulfillmentType: FulfillmentType = FulfillmentType.DELIVERY,
    val status: OrderStatus = OrderStatus.PENDING_PAYMENT,
    val items: List<CartItem> = emptyList(),
    val deliveryAddress: DeliveryAddress? = null,
    val pickupSlot: PickupSlot? = null,
    val pricing: OrderPricing = OrderPricing(),
    val payment: OrderPayment = OrderPayment(),
    val notes: String = "",
    val cancellationReason: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
