package com.skylink.sksweetsandbakes.core.payment

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.CFTheme
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.utils.CFErrorResponse
import com.cashfree.pg.ui.api.CFDropCheckoutPayment
import com.cashfree.pg.ui.api.CFPaymentComponent
import com.skylink.sksweetsandbakes.data.orders.requestmodel.CashfreePaymentRequestModel
import com.skylink.sksweetsandbakes.data.orders.responsemodel.CashfreePaymentResponseModel

/**
 * Clean Architecture Cashfree Payment Gateway Manager
 * Handles checkout initialization, session configuration, drop-in UI, and callback handling.
 */
object CashfreePaymentManager : CFCheckoutResponseCallback {
    private const val TAG = "SKSweetsCashfree"

    var currentAppId: String = "TEST1029384756"
    var currentEnvironment: CFSession.Environment = CFSession.Environment.SANDBOX

    private var activeCallback: ((CashfreePaymentResponseModel) -> Unit)? = null
    private var isInitialized = false

    fun initialize(context: Context) {
        try {
            CFPaymentGatewayService.initialize(context.applicationContext)
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
            isInitialized = true
            Log.d(TAG, "[INIT] Cashfree Payment Gateway initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "[INIT ERROR] Failed to initialize Cashfree SDK", e)
        }
    }

    fun startPayment(
        activity: Activity,
        request: CashfreePaymentRequestModel,
        onResult: (CashfreePaymentResponseModel) -> Unit
    ) {
        activeCallback = onResult
        try {
            if (!isInitialized) {
                initialize(activity)
            } else {
                CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
            }

            val session = CFSession.CFSessionBuilder()
                .setEnvironment(currentEnvironment)
                .setOrderId(request.orderId)
                .setPaymentSessionID(request.paymentSessionId)
                .build()

            val theme = CFTheme.CFThemeBuilder()
                .setNavigationBarBackgroundColor("#591720")
                .setNavigationBarTextColor("#FFFFFF")
                .setButtonBackgroundColor("#591720")
                .setButtonTextColor("#FFFFFF")
                .setPrimaryTextColor("#1C1B1F")
                .build()

            val paymentComponent = CFPaymentComponent.CFPaymentComponentBuilder()
                .add(CFPaymentComponent.CFPaymentModes.UPI)
                .add(CFPaymentComponent.CFPaymentModes.CARD)
                .add(CFPaymentComponent.CFPaymentModes.NB)
                .add(CFPaymentComponent.CFPaymentModes.WALLET)
                .add(CFPaymentComponent.CFPaymentModes.PAY_LATER)
                .build()

            val dropCheckoutPayment = CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                .setSession(session)
                .setCFUIPaymentModes(paymentComponent)
                .setCFNativeCheckoutUITheme(theme)
                .build()

            Log.i(
                TAG,
                "[LAUNCH] Opening Cashfree Drop-in Checkout: orderId=${request.orderId}, session=${request.paymentSessionId}"
            )
            CFPaymentGatewayService.getInstance().doPayment(activity, dropCheckoutPayment)
        } catch (e: Exception) {
            Log.e(TAG, "[LAUNCH ERROR] Failed to start Cashfree checkout", e)
            val res = CashfreePaymentResponseModel(
                isSuccess = false,
                orderId = request.orderId,
                errorMessage = e.localizedMessage ?: "Failed to open Cashfree checkout"
            )
            onResult(res)
            activeCallback = null
        }
    }

    override fun onPaymentVerify(orderID: String) {
        Log.i(TAG, "[SUCCESS] Cashfree payment verified for order: $orderID")
        val res = CashfreePaymentResponseModel(
            isSuccess = true,
            orderId = orderID
        )
        activeCallback?.invoke(res)
        activeCallback = null
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        Log.e(
            TAG,
            "[FAILURE] Cashfree payment failed for order: $orderID, error: ${cfErrorResponse.message}"
        )
        val res = CashfreePaymentResponseModel(
            isSuccess = false,
            orderId = orderID,
            errorCode = cfErrorResponse.code,
            errorMessage = cfErrorResponse.message
        )
        activeCallback?.invoke(res)
        activeCallback = null
    }

    fun clearCallback() {
        activeCallback = null
    }
}
