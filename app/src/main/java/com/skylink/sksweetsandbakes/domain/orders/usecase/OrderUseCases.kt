package com.skylink.sksweetsandbakes.domain.orders.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.repository.CartRepository
import com.skylink.sksweetsandbakes.domain.repository.NotificationRepository
import com.skylink.sksweetsandbakes.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetCustomerOrdersUseCase(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(customerId: String): Flow<List<Order>> {
        return orderRepository.getCustomerOrders(customerId)
    }
}

class GetOrderDetailUseCase(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String): Result<Order> {
        return orderRepository.getOrderById(orderId)
    }
}

class PlaceOrderUseCase(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(order: Order): Result<String> {
        val result = orderRepository.createOrder(order)
        if (result is Result.Success) {
            cartRepository.clearCart()
            val orderWithAssignedId = order.copy(orderId = result.data)
            notificationRepository.sendPaymentSuccessNotification(orderWithAssignedId)
        }
        return result
    }
}

class CancelOrderUseCase(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, reason: String): Result<Unit> {
        return orderRepository.cancelOrder(orderId, reason)
    }
}

class GetSavedAddressesUseCase(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(userId: String): Flow<List<DeliveryAddress>> {
        return orderRepository.getSavedAddresses(userId)
    }
}

class SaveAddressUseCase(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(userId: String, address: DeliveryAddress): Result<String> {
        return orderRepository.saveAddress(userId, address)
    }
}

class DeleteAddressUseCase(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(userId: String, addressId: String): Result<Unit> {
        return orderRepository.deleteAddress(userId, addressId)
    }
}
