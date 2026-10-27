package com.skylink.sksweetsandbakes.core.payment

import android.app.Activity
import android.content.Context
import android.util.Log
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.skylink.sksweetsandbakes.data.orders.requestmodel.RazorpayPaymentRequestModel
import com.skylink.sksweetsandbakes.data.orders.responsemodel.RazorpayPaymentResponseModel
import org.json.JSONObject

/**
 * Clean Architecture Razorpay Payment Gateway Manager
 * Handles checkout initialization, options preparation, lifecycle listeners, and results.
 */
object RazorpayPaymentManager {
    private const val TAG = "SKSweetsRazorpay"
    var currentKeyId: String = "rzp_test_51SKSweetsBak"

    private var activeCallback: ((RazorpayPaymentResponseModel) -> Unit)? = null

    fun initialize(context: Context, keyId: String = currentKeyId) {
        currentKeyId = keyId
        try {
            Checkout.preload(context.applicationContext)
            Log.d(TAG, "[INIT] Razorpay Checkout preloaded successfully")
        } catch (e: Exception) {
            Log.e(TAG, "[INIT ERROR] Failed to preload Razorpay", e)
        }
    }

    fun startPayment(
        activity: Activity,
        request: RazorpayPaymentRequestModel,
        onResult: (RazorpayPaymentResponseModel) -> Unit
    ) {
        activeCallback = onResult
        try {
            val checkout = Checkout()
            checkout.setKeyID(currentKeyId)

            val options = JSONObject().apply {
                put("name", "S K Sweets & Bakes")
                put("description", request.description)
                put("currency", request.currency)
                put("amount", request.amountPaise)
                put("theme.color", "#591720")

                val prefill = JSONObject().apply {
                    put("name", request.customerName)
                    put("email", request.customerEmail)
                    put("contact", request.customerPhone)
                }
                put("prefill", prefill)

                val retry = JSONObject().apply {
                    put("enabled", true)
                    put("max_count", 3)
                }
                put("retry", retry)
            }

            Log.i(
                TAG,
                "[LAUNCH] Opening Razorpay Checkout: amount=${request.amountPaise} paise (₹${request.amountPaise / 100}) for ${request.customerName}"
            )
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e(TAG, "[LAUNCH ERROR] Failed to open Razorpay checkout", e)
            val res = RazorpayPaymentResponseModel(
                isSuccess = false,
                errorMessage = e.localizedMessage ?: "Failed to open Razorpay checkout"
            )
            onResult(res)
            activeCallback = null
        }
    }

    fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {
        val resolvedPaymentId = paymentId ?: paymentData?.paymentId
        val resolvedOrderId = paymentData?.orderId
        val resolvedSignature = paymentData?.signature
        Log.i(
            TAG,
            "[SUCCESS] Razorpay payment completed successfully! paymentId=$resolvedPaymentId, orderId=$resolvedOrderId"
        )
        val res = RazorpayPaymentResponseModel(
            isSuccess = true,
            paymentId = resolvedPaymentId,
            orderId = resolvedOrderId,
            signature = resolvedSignature
        )
        activeCallback?.invoke(res)
        activeCallback = null
    }

    fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        Log.e(TAG, "[ERROR] Razorpay payment failed (code=$code): $response")
        val res = RazorpayPaymentResponseModel(
            isSuccess = false,
            errorCode = code,
            errorMessage = response ?: "Payment cancelled or failed"
        )
        activeCallback?.invoke(res)
        activeCallback = null
    }
}
