package com.skylink.sksweetsandbakes.core.navigation

import java.net.URLEncoder

/**
 * Type-safe navigation routes for S K Sweets & Bakes.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object PhoneLogin : Screen("phone_login")
    object EmailLogin : Screen("email_login")
    object StaffLogin : Screen("staff_login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object FinalizeAuth : Screen("finalize_auth?userName={userName}&email={email}") {
        fun createRoute(userName: String = "", email: String = "") =
            "finalize_auth?userName=$userName&email=$email"
    }

    object OtpVerification : Screen("otp_verification/{verificationId}/{phone}") {
        fun createRoute(verificationId: String, phone: String) =
            "otp_verification/$verificationId/$phone"
    }

    object ProfileSetup : Screen("profile_setup")
    object ProfileEdit : Screen("profile_edit")
    object AddressList : Screen("address_list")
    object AddEditAddress : Screen("add_edit_address?addressId={addressId}") {
        fun createRoute(addressId: String? = null) =
            if (addressId != null) "add_edit_address?addressId=$addressId" else "add_edit_address"
    }

    object Home : Screen("home")
    object Categories : Screen("categories")
    object ProductList : Screen("product_list?categoryId={categoryId}&title={title}") {
        fun createRoute(categoryId: String? = null, title: String? = null): String {
            val encCat = categoryId?.takeIf { it.isNotBlank() }?.let {
                try {
                    URLEncoder.encode(it, "UTF-8")
                } catch (e: Exception) {
                    it
                }
            }
            val encTitle = title?.takeIf { it.isNotBlank() }?.let {
                try {
                    URLEncoder.encode(it, "UTF-8")
                } catch (e: Exception) {
                    it
                }
            }
            val catParam = encCat?.let { "categoryId=$it" } ?: ""
            val titleParam = encTitle?.let { "title=$it" } ?: ""
            val query = listOf(catParam, titleParam).filter { it.isNotBlank() }.joinToString("&")
            return if (query.isNotBlank()) "product_list?$query" else "product_list"
        }
    }

    object Search : Screen("search")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object Wishlist : Screen("wishlist")
    object Favourites : Screen("favourites")
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object Payment : Screen("payment/{orderId}") {
        fun createRoute(orderId: String) = "payment/$orderId"
    }

    object OrderConfirmation : Screen("order_confirmation/{orderId}") {
        fun createRoute(orderId: String) = "order_confirmation/$orderId"
    }

    object OrderHistory : Screen("order_history")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }

    object HelpSupport : Screen("help_support")

    object OwnerDashboard : Screen("owner_dashboard")
    object InventoryOverview : Screen("inventory_overview")
    object PosSales : Screen("pos_sales")
    object Expenses : Screen("expenses")
    object Analytics : Screen("analytics")
    object Suppliers : Screen("suppliers")
    object StaffManagement : Screen("staff_management")
    object BusinessSettings : Screen("business_settings")
}
