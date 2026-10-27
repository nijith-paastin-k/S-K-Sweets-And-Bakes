package com.skylink.sksweetsandbakes.domain.orders.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getCustomerOrders(customerId: String): Flow<List<Order>>
    fun getAllOrders(): Flow<List<Order>>
    suspend fun getOrderById(orderId: String): Result<Order>
    suspend fun createOrder(order: Order): Result<String>
    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<Unit>
    suspend fun cancelOrder(orderId: String, reason: String): Result<Unit>
    fun getSavedAddresses(userId: String): Flow<List<DeliveryAddress>>
    suspend fun saveAddress(userId: String, address: DeliveryAddress): Result<String>
    suspend fun deleteAddress(userId: String, addressId: String): Result<Unit>
}
