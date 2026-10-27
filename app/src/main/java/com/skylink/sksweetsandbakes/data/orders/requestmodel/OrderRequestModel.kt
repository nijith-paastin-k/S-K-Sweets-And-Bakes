package com.skylink.sksweetsandbakes.data.orders.requestmodel

import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequestModel(
    val order: Order
)

@Serializable
data class CancelOrderRequestModel(
    val orderId: String,
    val reason: String
)

@Serializable
data class SaveAddressRequestModel(
    val userId: String,
    val address: DeliveryAddress
)
typealias CreateOrderRequest = CreateOrderRequestModel
typealias CancelOrderRequest = CancelOrderRequestModel
typealias SaveAddressRequest = SaveAddressRequestModel
