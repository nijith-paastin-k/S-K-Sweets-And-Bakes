package com.skylink.sksweetsandbakes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.utils.CFErrorResponse
import com.facebook.CallbackManager
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.skylink.sksweetsandbakes.core.navigation.AppNavGraph
import com.skylink.sksweetsandbakes.core.payment.CashfreePaymentManager
import com.skylink.sksweetsandbakes.core.payment.RazorpayPaymentManager
import com.skylink.sksweetsandbakes.data.local.AppThemeMode
import com.skylink.sksweetsandbakes.ui.theme.SKSweetsAndBakesTheme

class MainActivity : FragmentActivity(), PaymentResultWithDataListener, CFCheckoutResponseCallback {
    val facebookCallbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Preload Razorpay Checkout SDK
        RazorpayPaymentManager.initialize(applicationContext)

        // Initialize Cashfree Payment Gateway SDK
        CashfreePaymentManager.initialize(applicationContext)

        val appContainer = (application as SKSweetsApplication).container

        setContent {
            val themeMode by appContainer.userPreferences.themeMode.collectAsState(initial = AppThemeMode.SYSTEM)

            SKSweetsAndBakesTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    container = appContainer
                )
            }
        }
    }

    // --- Razorpay Payment Callbacks ---
    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        RazorpayPaymentManager.onPaymentSuccess(razorpayPaymentId, paymentData)
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        RazorpayPaymentManager.onPaymentError(code, response, paymentData)
    }

    // --- Cashfree Payment Callbacks ---
    override fun onPaymentVerify(orderID: String) {
        CashfreePaymentManager.onPaymentVerify(orderID)
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        CashfreePaymentManager.onPaymentFailure(cfErrorResponse, orderID)
    }

    @Deprecated("Facebook Login SDK requires forwarding activity results.")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Prevent memory leaks by detaching pending gateway callbacks
        RazorpayPaymentManager.clearCallback()
        CashfreePaymentManager.clearCallback()
    }
}
