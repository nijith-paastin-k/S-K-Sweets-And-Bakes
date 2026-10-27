package com.skylink.sksweetsandbakes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.skylink.sksweetsandbakes.core.navigation.AppNavGraph
import com.skylink.sksweetsandbakes.core.payment.RazorpayPaymentManager
import com.skylink.sksweetsandbakes.data.local.AppThemeMode
import com.skylink.sksweetsandbakes.ui.theme.SKSweetsAndBakesTheme

class MainActivity : FragmentActivity(), PaymentResultWithDataListener {
    val facebookCallbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Preload Razorpay Checkout SDK
        RazorpayPaymentManager.initialize(applicationContext)

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

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        RazorpayPaymentManager.onPaymentSuccess(razorpayPaymentId, paymentData)
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        RazorpayPaymentManager.onPaymentError(code, response, paymentData)
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
}
