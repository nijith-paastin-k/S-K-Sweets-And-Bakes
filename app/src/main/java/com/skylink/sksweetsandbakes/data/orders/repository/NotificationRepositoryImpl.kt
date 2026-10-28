package com.skylink.sksweetsandbakes.data.orders.repository

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.skylink.sksweetsandbakes.MainActivity
import com.skylink.sksweetsandbakes.R
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepositoryImpl(
    context: Context
) : NotificationRepository {
    private val context: Context = context.applicationContext

    companion object {
        const val CHANNEL_ID = "sksweets_order_updates"
        const val CHANNEL_NAME = "Order & Payment Updates"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    "Live notifications for order status, payments, and delivery tracking."
                enableVibration(true)
                setShowBadge(true)
            }
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    override suspend fun sendPaymentSuccessNotification(order: Order): Result<Unit> =
        withContext(Dispatchers.Main) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val hasPermission = ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!hasPermission) {
                        return@withContext Result.Success(Unit)
                    }
                }

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("orderId", order.orderId)
                    putExtra("orderNumber", order.orderNumber)
                }

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    order.orderNumber.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val formattedAmount = CurrencyFormatter.formatPaise(order.pricing.grandTotalPaise)
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Payment Successful! Order #${order.orderNumber} Confirmed 🎉")
                    .setContentText("Paid $formattedAmount via ${order.payment.method.name}. Sweets are being prepared!")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Payment of $formattedAmount received for Order #${order.orderNumber}. Our master confectioners are freshly preparing your order. You can view or download the invoice in the app.")
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

                val notificationManager = NotificationManagerCompat.from(context)
                val notificationId = (order.orderNumber.hashCode() and 0x7FFFFFFF)
                notificationManager.notify(notificationId, notification)

                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e, e.message ?: "Failed to dispatch notification")
            }
        }

    override suspend fun registerFcmToken(userId: String, token: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e, e.message ?: "Failed to register FCM token")
            }
        }
}
