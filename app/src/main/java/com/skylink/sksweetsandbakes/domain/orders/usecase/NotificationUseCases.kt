package com.skylink.sksweetsandbakes.domain.orders.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.repository.NotificationRepository

class SendPaymentNotificationUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(order: Order): Result<Unit> {
        return notificationRepository.sendPaymentSuccessNotification(order)
    }
}

class RegisterFcmTokenUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(userId: String, token: String): Result<Unit> {
        return notificationRepository.registerFcmToken(userId, token)
    }
}
