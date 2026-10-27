package com.skylink.sksweetsandbakes.domain.orders.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Order

interface NotificationRepository {
    suspend fun sendPaymentSuccessNotification(order: Order): Result<Unit>
    suspend fun registerFcmToken(userId: String, token: String): Result<Unit>
}
