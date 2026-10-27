package com.skylink.sksweetsandbakes.di

import android.content.Context
import com.skylink.sksweetsandbakes.data.auth.repository.AuthRepositoryImpl
import com.skylink.sksweetsandbakes.data.cart.repository.CartRepositoryImpl
import com.skylink.sksweetsandbakes.data.dashboard.repository.AnalyticsRepositoryImpl
import com.skylink.sksweetsandbakes.data.dashboard.repository.ExpenseRepositoryImpl
import com.skylink.sksweetsandbakes.data.dashboard.repository.InventoryRepositoryImpl
import com.skylink.sksweetsandbakes.data.dashboard.repository.PosRepositoryImpl
import com.skylink.sksweetsandbakes.data.dashboard.repository.ProductRepositoryImpl
import com.skylink.sksweetsandbakes.data.favourites.repository.FavouritesRepositoryImpl
import com.skylink.sksweetsandbakes.data.local.SKSweetsDatabaseHelper
import com.skylink.sksweetsandbakes.data.local.UserPreferences
import com.skylink.sksweetsandbakes.data.orders.repository.InvoiceRepositoryImpl
import com.skylink.sksweetsandbakes.data.orders.repository.NotificationRepositoryImpl
import com.skylink.sksweetsandbakes.data.orders.repository.OrderRepositoryImpl
import com.skylink.sksweetsandbakes.data.wishlist.repository.WishlistRepositoryImpl
import com.skylink.sksweetsandbakes.domain.auth.repository.AuthRepository
import com.skylink.sksweetsandbakes.domain.auth.usecase.GetCurrentUserUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.LinkOAuthProviderUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SetRememberMeUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithFacebookUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithGoogleUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithOAuthProviderUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithPhoneUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignOutUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SwitchRoleUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.UpdateUserProfileUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.VerifyOtpUseCase
import com.skylink.sksweetsandbakes.domain.cart.repository.CartRepository
import com.skylink.sksweetsandbakes.domain.cart.usecase.AddToCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.ApplyCouponUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.ClearCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.GetCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.RemoveCouponUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.RemoveFromCartUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.UpdateCartQuantityUseCase
import com.skylink.sksweetsandbakes.domain.cart.usecase.ValidateOrderUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.repository.AnalyticsRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.ExpenseRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.InventoryRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.PosRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.ProductRepository
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.AddReviewUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.AdjustStockUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculateFinancialsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculatePricingUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.DeleteExpenseUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetExpensesUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetFinancialStatementUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetInventoryListUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductDetailUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetProductsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.GetRecentPosSalesUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.ProcessPosSaleUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.RecordExpenseUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.SearchProductsUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.WishlistUseCase
import com.skylink.sksweetsandbakes.domain.favourites.repository.FavouritesRepository
import com.skylink.sksweetsandbakes.domain.favourites.usecase.GetFavouritesUseCase
import com.skylink.sksweetsandbakes.domain.favourites.usecase.ToggleFavouriteUseCase
import com.skylink.sksweetsandbakes.domain.orders.repository.InvoiceRepository
import com.skylink.sksweetsandbakes.domain.orders.repository.NotificationRepository
import com.skylink.sksweetsandbakes.domain.orders.repository.OrderRepository
import com.skylink.sksweetsandbakes.domain.orders.usecase.CancelOrderUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.DeleteAddressUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GenerateInvoicePdfUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetCustomerOrdersUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetOrderDetailUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.GetSavedAddressesUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.PlaceOrderUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.RegisterFcmTokenUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.SaveAddressUseCase
import com.skylink.sksweetsandbakes.domain.orders.usecase.SendPaymentNotificationUseCase
import com.skylink.sksweetsandbakes.domain.wishlist.repository.WishlistRepository

/**
 * AppModule provides modular Clean Architecture dependencies partitioned by:
 * - Auth Module
 * - Dashboard Module
 * - Cart Module
 * - Favourites Module
 * - Wishlist Module
 * - Orders Module
 */
class AppModule(private val context: Context) : AppContainer {

    override val databaseHelper: SKSweetsDatabaseHelper by lazy {
        SKSweetsDatabaseHelper(context)
    }

    override val userPreferences: UserPreferences by lazy {
        UserPreferences(context)
    }
    override val calculatePricingUseCase: CalculatePricingUseCase by lazy {
        CalculatePricingUseCase()
    }

    override val validateOrderUseCase: ValidateOrderUseCase by lazy {
        ValidateOrderUseCase(calculatePricingUseCase)
    }

    override val calculateFinancialsUseCase: CalculateFinancialsUseCase by lazy {
        CalculateFinancialsUseCase()
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(userPreferences, databaseHelper)
    }

    override val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(databaseHelper)
    }

    override val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(calculatePricingUseCase, validateOrderUseCase)
    }

    override val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl(databaseHelper)
    }

    override val inventoryRepository: InventoryRepository by lazy {
        InventoryRepositoryImpl()
    }

    override val posRepository: PosRepository by lazy {
        PosRepositoryImpl(inventoryRepository)
    }

    override val expenseRepository: ExpenseRepository by lazy {
        ExpenseRepositoryImpl()
    }

    override val analyticsRepository: AnalyticsRepository by lazy {
        AnalyticsRepositoryImpl(
            orderRepository = orderRepository,
            posRepository = posRepository,
            expenseRepository = expenseRepository,
            calculateFinancialsUseCase = calculateFinancialsUseCase
        )
    }

    override val invoiceRepository: InvoiceRepository by lazy {
        InvoiceRepositoryImpl(context)
    }

    override val notificationRepository: NotificationRepository by lazy {
        NotificationRepositoryImpl(context, userPreferences)
    }
    val favouritesRepository: FavouritesRepository by lazy {
        FavouritesRepositoryImpl(productRepository)
    }

    val wishlistRepository: WishlistRepository by lazy {
        WishlistRepositoryImpl(productRepository)
    }
    override val getCustomerOrdersUseCase: GetCustomerOrdersUseCase by lazy {
        GetCustomerOrdersUseCase(orderRepository)
    }

    override val getOrderDetailUseCase: GetOrderDetailUseCase by lazy {
        GetOrderDetailUseCase(orderRepository)
    }

    override val placeOrderUseCase: PlaceOrderUseCase by lazy {
        PlaceOrderUseCase(orderRepository, cartRepository, notificationRepository)
    }

    override val cancelOrderUseCase: CancelOrderUseCase by lazy {
        CancelOrderUseCase(orderRepository)
    }

    override val getSavedAddressesUseCase: GetSavedAddressesUseCase by lazy {
        GetSavedAddressesUseCase(orderRepository)
    }

    override val saveAddressUseCase: SaveAddressUseCase by lazy {
        SaveAddressUseCase(orderRepository)
    }

    override val deleteAddressUseCase: DeleteAddressUseCase by lazy {
        DeleteAddressUseCase(orderRepository)
    }
    override val generateInvoicePdfUseCase: GenerateInvoicePdfUseCase by lazy {
        GenerateInvoicePdfUseCase(invoiceRepository)
    }

    override val sendPaymentNotificationUseCase: SendPaymentNotificationUseCase by lazy {
        SendPaymentNotificationUseCase(notificationRepository)
    }

    override val registerFcmTokenUseCase: RegisterFcmTokenUseCase by lazy {
        RegisterFcmTokenUseCase(notificationRepository)
    }
    override val getCartUseCase: GetCartUseCase by lazy {
        GetCartUseCase(cartRepository)
    }

    override val addToCartUseCase: AddToCartUseCase by lazy {
        AddToCartUseCase(cartRepository)
    }

    override val updateCartQuantityUseCase: UpdateCartQuantityUseCase by lazy {
        UpdateCartQuantityUseCase(cartRepository)
    }

    override val removeFromCartUseCase: RemoveFromCartUseCase by lazy {
        RemoveFromCartUseCase(cartRepository)
    }

    override val clearCartUseCase: ClearCartUseCase by lazy {
        ClearCartUseCase(cartRepository)
    }

    override val applyCouponUseCase: ApplyCouponUseCase by lazy {
        ApplyCouponUseCase(cartRepository)
    }

    override val removeCouponUseCase: RemoveCouponUseCase by lazy {
        RemoveCouponUseCase(cartRepository)
    }
    override val getProductsUseCase: GetProductsUseCase by lazy {
        GetProductsUseCase(productRepository)
    }

    override val getProductDetailUseCase: GetProductDetailUseCase by lazy {
        GetProductDetailUseCase(productRepository)
    }

    override val searchProductsUseCase: SearchProductsUseCase by lazy {
        SearchProductsUseCase(productRepository)
    }

    override val wishlistUseCase: WishlistUseCase by lazy {
        WishlistUseCase(productRepository)
    }

    override val addReviewUseCase: AddReviewUseCase by lazy {
        AddReviewUseCase(productRepository)
    }
    val getFavouritesUseCase: GetFavouritesUseCase by lazy {
        GetFavouritesUseCase(favouritesRepository)
    }

    val toggleFavouriteUseCase: ToggleFavouriteUseCase by lazy {
        ToggleFavouriteUseCase(favouritesRepository)
    }
    override val processPosSaleUseCase: ProcessPosSaleUseCase by lazy {
        ProcessPosSaleUseCase(posRepository)
    }

    override val getRecentPosSalesUseCase: GetRecentPosSalesUseCase by lazy {
        GetRecentPosSalesUseCase(posRepository)
    }

    override val getInventoryListUseCase: GetInventoryListUseCase by lazy {
        GetInventoryListUseCase(inventoryRepository)
    }

    override val adjustStockUseCase: AdjustStockUseCase by lazy {
        AdjustStockUseCase(inventoryRepository)
    }
    override val getExpensesUseCase: GetExpensesUseCase by lazy {
        GetExpensesUseCase(expenseRepository)
    }

    override val recordExpenseUseCase: RecordExpenseUseCase by lazy {
        RecordExpenseUseCase(expenseRepository)
    }

    override val deleteExpenseUseCase: DeleteExpenseUseCase by lazy {
        DeleteExpenseUseCase(expenseRepository)
    }

    override val getFinancialStatementUseCase: GetFinancialStatementUseCase by lazy {
        GetFinancialStatementUseCase(analyticsRepository)
    }
    override val getCurrentUserUseCase: GetCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }

    override val signInWithPhoneUseCase: SignInWithPhoneUseCase by lazy {
        SignInWithPhoneUseCase(authRepository)
    }

    override val verifyOtpUseCase: VerifyOtpUseCase by lazy {
        VerifyOtpUseCase(authRepository)
    }

    override val updateUserProfileUseCase: UpdateUserProfileUseCase by lazy {
        UpdateUserProfileUseCase(authRepository)
    }

    override val signInWithGoogleUseCase: SignInWithGoogleUseCase by lazy {
        SignInWithGoogleUseCase(authRepository)
    }

    override val signInWithFacebookUseCase: SignInWithFacebookUseCase by lazy {
        SignInWithFacebookUseCase(authRepository)
    }

    override val signInWithOAuthProviderUseCase: SignInWithOAuthProviderUseCase by lazy {
        SignInWithOAuthProviderUseCase(authRepository)
    }

    override val linkOAuthProviderUseCase: LinkOAuthProviderUseCase by lazy {
        LinkOAuthProviderUseCase(authRepository)
    }

    override val switchRoleUseCase: SwitchRoleUseCase by lazy {
        SwitchRoleUseCase(authRepository)
    }

    override val signOutUseCase: SignOutUseCase by lazy {
        SignOutUseCase(authRepository)
    }

    override val setRememberMeUseCase: SetRememberMeUseCase by lazy {
        SetRememberMeUseCase(authRepository)
    }
}
