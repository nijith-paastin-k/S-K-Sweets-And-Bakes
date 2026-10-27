package com.skylink.sksweetsandbakes.data.orders.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.data.local.SKSweetsDatabaseHelper
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.FulfillmentType
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.model.OrderPayment
import com.skylink.sksweetsandbakes.domain.model.OrderPricing
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import com.skylink.sksweetsandbakes.domain.model.PaymentMethod
import com.skylink.sksweetsandbakes.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val databaseHelper: SKSweetsDatabaseHelper? = null
) : OrderRepository {

    private val _addresses = MutableStateFlow<Map<String, List<DeliveryAddress>>>(
        mapOf(
            "usr_demo_01" to listOf(
                DeliveryAddress(
                    addressId = "addr_01",
                    recipientName = "Ramesh Kumar",
                    phoneNumber = "+91 98765 43210",
                    addressLine1 = "#14, 2nd Cross, Sweet Garden Layout",
                    addressLine2 = "Near Anjaneya Temple",
                    landmark = "Opposite Metro Pillar 240",
                    city = "Bengaluru",
                    pincode = "560001",
                    isDefault = true
                ),
                DeliveryAddress(
                    addressId = "addr_02",
                    recipientName = "Ramesh Kumar (Office)",
                    phoneNumber = "+91 98765 43210",
                    addressLine1 = "Tech Park Tower 3, 5th Floor",
                    city = "Bengaluru",
                    pincode = "560066",
                    isDefault = false
                )
            )
        )
    )

    private val _orders = MutableStateFlow<List<Order>>(
        listOf(
            Order(
                orderId = "ord_101",
                orderNumber = "SKS-20261023-001",
                customerId = "usr_demo_01",
                customerName = "Ramesh Kumar",
                customerPhone = "+91 98765 43210",
                fulfillmentType = FulfillmentType.DELIVERY,
                status = OrderStatus.CONFIRMED,
                items = listOf(
                    CartItem(
                        productId = "prod_kaju_katli",
                        productName = "Kaju Katli Special",
                        productImageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500",
                        measurementType = MeasurementType.WEIGHT,
                        selectedQuantity = 500,
                        unitPricePaise = 96000L,
                        lineTotalPaise = 48000L
                    ),
                    CartItem(
                        productId = "prod_mysore_pak",
                        productName = "Desi Ghee Mysore Pak",
                        productImageUrl = "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=500",
                        measurementType = MeasurementType.WEIGHT,
                        selectedQuantity = 500,
                        unitPricePaise = 74000L,
                        lineTotalPaise = 37000L
                    )
                ),
                deliveryAddress = DeliveryAddress(
                    addressId = "addr_01",
                    recipientName = "Ramesh Kumar",
                    phoneNumber = "+91 98765 43210",
                    addressLine1 = "#14, 2nd Cross, Sweet Garden Layout",
                    city = "Bengaluru",
                    pincode = "560001"
                ),
                pricing = OrderPricing(
                    subtotalPaise = 85000L,
                    discountPaise = 8500L,
                    couponCode = "FESTIVE10",
                    deliveryFeePaise = 0L,
                    packagingFeePaise = 1500L,
                    taxPaise = 3825L,
                    grandTotalPaise = 81825L
                ),
                payment = OrderPayment(
                    paymentId = "pay_mock_981",
                    method = PaymentMethod.UPI,
                    status = "SUCCESS",
                    paidAt = System.currentTimeMillis() - 3600000L
                ),
                createdAt = System.currentTimeMillis() - 3600000L
            )
        )
    )

    override fun getCustomerOrders(customerId: String): Flow<List<Order>> {
        return _orders.map { list ->
            if (customerId.isBlank() || customerId == "usr_demo_01") list
            else {
                val filtered =
                    list.filter { it.customerId == customerId || it.customerId == "usr_demo_01" }
                filtered.ifEmpty { list }
            }
        }
    }

    override fun getAllOrders(): Flow<List<Order>> = _orders.asStateFlow()

    override suspend fun getOrderById(orderId: String): Result<Order> {
        val cleanId = orderId.trim()
        val ord = _orders.value.find {
            it.orderId.equals(cleanId, ignoreCase = true) ||
                    it.orderNumber.equals(cleanId, ignoreCase = true) ||
                    (cleanId.isNotBlank() && cleanId.contains(it.orderId, ignoreCase = true)) ||
                    (cleanId.isNotBlank() && it.orderId.contains(cleanId, ignoreCase = true))
        } ?: _orders.value.firstOrNull()

        return if (ord != null) Result.Success(ord)
        else Result.Error(NoSuchElementException("Order $orderId not found"))
    }

    override suspend fun createOrder(order: Order): Result<String> {
        val id = if (order.orderId.isBlank()) "ord_${System.currentTimeMillis()}" else order.orderId
        val num =
            if (order.orderNumber.isBlank()) "SKS-${System.currentTimeMillis() % 100000}" else order.orderNumber
        val newOrder = order.copy(orderId = id, orderNumber = num)

        val current = _orders.value.toMutableList()
        current.add(0, newOrder)
        _orders.value = current
        databaseHelper?.saveOrder(newOrder)
        return Result.Success(id)
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<Unit> {
        val current = _orders.value.toMutableList()
        val index = current.indexOfFirst { it.orderId == orderId }
        if (index >= 0) {
            current[index] =
                current[index].copy(status = newStatus, updatedAt = System.currentTimeMillis())
            _orders.value = current
            return Result.Success(Unit)
        }
        return Result.Error(NoSuchElementException("Order $orderId not found"))
    }

    override suspend fun cancelOrder(orderId: String, reason: String): Result<Unit> {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }

    override fun getSavedAddresses(userId: String): Flow<List<DeliveryAddress>> {
        return _addresses.map { map ->
            val list = if (userId.isNotBlank()) map[userId] else null
            if (!list.isNullOrEmpty()) list
            else map["usr_demo_01"] ?: emptyList()
        }
    }

    override suspend fun saveAddress(userId: String, address: DeliveryAddress): Result<String> {
        val current = _addresses.value.toMutableMap()
        val targetKey = if (userId.isNotBlank()) userId else "usr_demo_01"
        val list = (current[targetKey] ?: current["usr_demo_01"] ?: emptyList()).toMutableList()
        val id =
            if (address.addressId.isBlank()) "addr_${System.currentTimeMillis()}" else address.addressId
        val updated = address.copy(addressId = id)

        val index = list.indexOfFirst { it.addressId == id }
        if (index >= 0) list[index] = updated else list.add(0, updated)

        current[targetKey] = list
        current["usr_demo_01"] = list
        _addresses.value = current
        databaseHelper?.saveAddress(targetKey, updated)
        return Result.Success(id)
    }

    override suspend fun deleteAddress(userId: String, addressId: String): Result<Unit> {
        val current = _addresses.value.toMutableMap()
        val targetKey = if (userId.isNotBlank()) userId else "usr_demo_01"
        val list = (current[targetKey] ?: current["usr_demo_01"]
        ?: emptyList()).filterNot { it.addressId == addressId }
        current[targetKey] = list
        current["usr_demo_01"] = list
        _addresses.value = current
        databaseHelper?.deleteAddress(addressId)
        return Result.Success(Unit)
    }
}
