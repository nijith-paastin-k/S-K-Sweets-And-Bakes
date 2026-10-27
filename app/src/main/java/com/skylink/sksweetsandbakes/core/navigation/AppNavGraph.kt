package com.skylink.sksweetsandbakes.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.data.favourites.repository.FavouritesRepositoryImpl
import com.skylink.sksweetsandbakes.data.local.AppThemeMode
import com.skylink.sksweetsandbakes.di.AppContainer
import com.skylink.sksweetsandbakes.di.AppModule
import com.skylink.sksweetsandbakes.domain.favourites.usecase.GetFavouritesUseCase
import com.skylink.sksweetsandbakes.domain.favourites.usecase.ToggleFavouriteUseCase
import com.skylink.sksweetsandbakes.presentation.auth.screen.AddressListScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.EmailLoginScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.FinalizeAuthScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.ForgotPasswordScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.LoginScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.OtpVerificationScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.PhoneLoginScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.ProfileEditScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.RegisterScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.SplashScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.StaffLoginScreen
import com.skylink.sksweetsandbakes.presentation.auth.screen.WelcomeScreen
import com.skylink.sksweetsandbakes.presentation.auth.viewmodel.AuthViewModel
import com.skylink.sksweetsandbakes.presentation.cart.screen.CartScreen
import com.skylink.sksweetsandbakes.presentation.cart.viewmodel.CartViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.AnalyticsScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.BusinessSettingsScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.CategoriesScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.ExpensesScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.HomeScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.InventoryScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.OwnerDashboardScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.PosScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.ProductDetailScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.ProductListScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.SearchScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.StaffManagementScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.screen.SuppliersScreen
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.AnalyticsViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.CatalogViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.ExpensesViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.HomeViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.InventoryViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.OwnerDashboardViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.PosViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.ProductDetailViewModel
import com.skylink.sksweetsandbakes.presentation.favourites.screen.FavouritesScreen
import com.skylink.sksweetsandbakes.presentation.favourites.viewmodel.FavouritesViewModel
import com.skylink.sksweetsandbakes.presentation.orders.screen.CheckoutScreen
import com.skylink.sksweetsandbakes.presentation.orders.screen.OrderConfirmationScreen
import com.skylink.sksweetsandbakes.presentation.orders.screen.OrderDetailScreen
import com.skylink.sksweetsandbakes.presentation.orders.screen.OrderHistoryScreen
import com.skylink.sksweetsandbakes.presentation.orders.viewmodel.CheckoutViewModel
import com.skylink.sksweetsandbakes.presentation.orders.viewmodel.OrdersViewModel
import com.skylink.sksweetsandbakes.presentation.wishlist.screen.WishlistScreen
import com.skylink.sksweetsandbakes.presentation.wishlist.viewmodel.WishlistViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    navController: NavHostController,
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    val authViewModel = AuthViewModel(
        getCurrentUserUseCase = container.getCurrentUserUseCase,
        signInWithPhoneUseCase = container.signInWithPhoneUseCase,
        verifyOtpUseCase = container.verifyOtpUseCase,
        updateUserProfileUseCase = container.updateUserProfileUseCase,
        switchRoleUseCase = container.switchRoleUseCase,
        signOutUseCase = container.signOutUseCase,
        setRememberMeUseCase = container.setRememberMeUseCase,
        signInWithGoogleUseCase = container.signInWithGoogleUseCase,
        signInWithFacebookUseCase = container.signInWithFacebookUseCase,
        signInWithOAuthProviderUseCase = container.signInWithOAuthProviderUseCase,
        linkOAuthProviderUseCase = container.linkOAuthProviderUseCase
    )
    val authState by authViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    fun safeBack(fallback: String = Screen.Home.route) {
        if (!navController.popBackStack()) {
            navController.navigate(fallback) {
                popUpTo(0)
            }
        }
    }

    val currentThemeMode by container.userPreferences.themeMode.collectAsState(initial = AppThemeMode.SYSTEM)
    val isRemembered by container.userPreferences.rememberMe.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    if (isRemembered && authState.userProfile != null) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                },
                onExploreClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onStaffLoginClick = {
                    navController.navigate(Screen.StaffLogin.route)
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onCustomerLoginClick = { navController.navigate(Screen.Login.route) },
                onCustomerRegisterClick = { navController.navigate(Screen.Register.route) },
                onStaffLoginClick = {
                    authViewModel.switchRoleForTesting(UserRole.OWNER)
                    navController.navigate(Screen.OwnerDashboard.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onPhoneLoginClick = { navController.navigate(Screen.PhoneLogin.route) },
                onEmailLoginClick = { navController.navigate(Screen.EmailLogin.route) },
                onStaffLoginClick = { navController.navigate(Screen.StaffLogin.route) },
                onLoginSuccess = { userName ->
                    android.util.Log.i(
                        "SKSweetsGoogleAuth",
                        "[NAV] onLoginSuccess invoked with userName='$userName'. Navigating to FinalizeAuth..."
                    )
                    navController.navigate(Screen.FinalizeAuth.createRoute(userName, "")) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                onGuestClick = {
                    authViewModel.updateProfile("Guest Explorer", "guest@sksweets.com") {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onBackClick = null
            )
        }

        composable(Screen.PhoneLogin.route) {
            PhoneLoginScreen(
                viewModel = authViewModel,
                onOtpRequested = { verificationId, phone ->
                    navController.navigate(
                        Screen.OtpVerification.createRoute(
                            verificationId,
                            phone
                        )
                    )
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onBackClick = { safeBack(Screen.Login.route) }
            )
        }

        composable(Screen.EmailLogin.route) {
            EmailLoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { userName ->
                    navController.navigate(Screen.FinalizeAuth.createRoute(userName, "")) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onBackClick = { safeBack(Screen.Login.route) }
            )
        }

        composable(Screen.StaffLogin.route) {
            StaffLoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { role ->
                    authViewModel.switchRoleForTesting(role)
                    navController.navigate(Screen.OwnerDashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onBackClick = { safeBack(Screen.Login.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onSaveAddress = { addr ->
                    scope.launch { container.saveAddressUseCase("usr_demo_01", addr) }
                },
                onRegisterSuccess = { name, email ->
                    navController.navigate(Screen.FinalizeAuth.createRoute(name, email)) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onBackClick = { safeBack(Screen.Login.route) }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onRecoverySent = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                },
                onBackClick = { safeBack(Screen.Login.route) }
            )
        }

        composable(
            route = Screen.FinalizeAuth.route,
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType; defaultValue = "Customer" },
                navArgument("email") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val uName = backStackEntry.arguments?.getString("userName") ?: "Customer"
            val uEmail = backStackEntry.arguments?.getString("email") ?: ""
            FinalizeAuthScreen(
                userName = uName,
                email = uEmail,
                onProceedClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.OtpVerification.route,
            arguments = listOf(
                navArgument("verificationId") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val vId = backStackEntry.arguments?.getString("verificationId") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpVerificationScreen(
                phoneNumber = phone,
                verificationId = vId,
                viewModel = authViewModel,
                onVerified = {
                    navController.navigate(
                        Screen.FinalizeAuth.createRoute(
                            "Verified Customer",
                            ""
                        )
                    ) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel = remember {
                HomeViewModel(
                    getProductsUseCase = container.getProductsUseCase,
                    wishlistUseCase = container.wishlistUseCase,
                    getCartUseCase = container.getCartUseCase
                )
            }
            val catalogViewModel = remember {
                CatalogViewModel(
                    getProductsUseCase = container.getProductsUseCase,
                    searchProductsUseCase = container.searchProductsUseCase
                )
            }
            val ordersViewModel = remember {
                OrdersViewModel(
                    getCustomerOrdersUseCase = container.getCustomerOrdersUseCase,
                    getOrderDetailUseCase = container.getOrderDetailUseCase,
                    cancelOrderUseCase = container.cancelOrderUseCase,
                    generateInvoicePdfUseCase = container.generateInvoicePdfUseCase
                )
            }
            val cartViewModel = remember {
                CartViewModel(
                    getCartUseCase = container.getCartUseCase,
                    updateCartQuantityUseCase = container.updateCartQuantityUseCase,
                    removeFromCartUseCase = container.removeFromCartUseCase,
                    applyCouponUseCase = container.applyCouponUseCase,
                    removeCouponUseCase = container.removeCouponUseCase,
                    clearCartUseCase = container.clearCartUseCase
                )
            }

            HomeScreen(
                viewModel = homeViewModel,
                catalogViewModel = catalogViewModel,
                ordersViewModel = ordersViewModel,
                cartViewModel = cartViewModel,
                userProfile = authState.userProfile,
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = { mode ->
                    scope.launch { container.userPreferences.setThemeMode(mode) }
                },
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onCategoryClick = { id, title ->
                    navController.navigate(
                        Screen.ProductList.createRoute(
                            id,
                            title
                        )
                    )
                },
                onAllCategoriesClick = { navController.navigate(Screen.Categories.route) },
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onCartClick = { navController.navigate(Screen.Cart.route) },
                onCheckoutClick = { navController.navigate(Screen.Checkout.route) },
                onOrderClick = { id -> navController.navigate(Screen.OrderDetail.createRoute(id)) },
                onProfileClick = { navController.navigate(Screen.ProfileEdit.route) },
                onOrdersClick = { navController.navigate(Screen.OrderHistory.route) },
                onPosClick = { navController.navigate(Screen.PosSales.route) },
                onOwnerDashboardClick = { navController.navigate(Screen.OwnerDashboard.route) },
                onInventoryClick = { navController.navigate(Screen.InventoryOverview.route) },
                onAddressesClick = { navController.navigate(Screen.AddressList.route) },
                onWishlistClick = { navController.navigate(Screen.Wishlist.route) },
                onSignOutClick = {
                    authViewModel.signOut {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Categories.route) {
            val catalogViewModel = CatalogViewModel(
                getProductsUseCase = container.getProductsUseCase,
                searchProductsUseCase = container.searchProductsUseCase
            )
            CategoriesScreen(
                viewModel = catalogViewModel,
                onCategoryClick = { id, title ->
                    navController.navigate(
                        Screen.ProductList.createRoute(
                            id,
                            title
                        )
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProductList.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType; nullable = true },
                navArgument("title") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val rawCatId = backStackEntry.arguments?.getString("categoryId")
            val rawTitle = backStackEntry.arguments?.getString("title")
            val catId = rawCatId?.let {
                try {
                    java.net.URLDecoder.decode(it, "UTF-8")
                } catch (e: Exception) {
                    it
                }
            }
            val title = rawTitle?.let {
                try {
                    java.net.URLDecoder.decode(it, "UTF-8")
                } catch (e: Exception) {
                    it
                }
            }
            val catalogViewModel = CatalogViewModel(
                getProductsUseCase = container.getProductsUseCase,
                searchProductsUseCase = container.searchProductsUseCase
            )
            ProductListScreen(
                categoryId = catId,
                categoryTitle = title ?: "Sweets & Bakes",
                viewModel = catalogViewModel,
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            val catalogViewModel = CatalogViewModel(
                getProductsUseCase = container.getProductsUseCase,
                searchProductsUseCase = container.searchProductsUseCase
            )
            SearchScreen(
                viewModel = catalogViewModel,
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val productDetailViewModel = ProductDetailViewModel(
                productId = productId,
                getProductDetailUseCase = container.getProductDetailUseCase,
                addToCartUseCase = container.addToCartUseCase,
                getCartUseCase = container.getCartUseCase,
                wishlistUseCase = container.wishlistUseCase,
                pricingUseCase = container.calculatePricingUseCase
            )
            ProductDetailScreen(
                viewModel = productDetailViewModel,
                onBackClick = { navController.popBackStack() },
                onCartClick = { navController.navigate(Screen.Cart.route) },
                onBuyNowClick = { navController.navigate(Screen.Checkout.route) }
            )
        }

        composable(Screen.Wishlist.route) {
            val wishlistViewModel = WishlistViewModel(
                wishlistUseCase = container.wishlistUseCase,
                getProductsUseCase = container.getProductsUseCase,
                addToCartUseCase = container.addToCartUseCase
            )
            WishlistScreen(
                viewModel = wishlistViewModel,
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onExploreSweetsClick = { navController.navigate(Screen.Home.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favourites.route) {
            val appModule = container as? AppModule
            val favRepo = appModule?.favouritesRepository
                ?: FavouritesRepositoryImpl(container.productRepository)
            val favouritesViewModel = FavouritesViewModel(
                getFavouritesUseCase = GetFavouritesUseCase(favRepo),
                toggleFavouriteUseCase = ToggleFavouriteUseCase(favRepo),
                addToCartUseCase = container.addToCartUseCase
            )
            FavouritesScreen(
                viewModel = favouritesViewModel,
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onExploreSweetsClick = { navController.navigate(Screen.Home.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Cart.route) {
            val cartViewModel = CartViewModel(
                getCartUseCase = container.getCartUseCase,
                updateCartQuantityUseCase = container.updateCartQuantityUseCase,
                removeFromCartUseCase = container.removeFromCartUseCase,
                applyCouponUseCase = container.applyCouponUseCase,
                removeCouponUseCase = container.removeCouponUseCase,
                clearCartUseCase = container.clearCartUseCase
            )
            CartScreen(
                viewModel = cartViewModel,
                onCheckoutClick = { navController.navigate(Screen.Checkout.route) },
                onExploreSweetsClick = { navController.navigate(Screen.Home.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Checkout.route) {
            val checkoutViewModel = CheckoutViewModel(
                getCartUseCase = container.getCartUseCase,
                placeOrderUseCase = container.placeOrderUseCase,
                getSavedAddressesUseCase = container.getSavedAddressesUseCase,
                saveAddressUseCase = container.saveAddressUseCase,
                deleteAddressUseCase = container.deleteAddressUseCase
            )
            CheckoutScreen(
                viewModel = checkoutViewModel,
                onOrderPlaced = { orderId ->
                    navController.navigate(Screen.OrderConfirmation.createRoute(orderId)) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.OrderConfirmation.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderConfirmationScreen(
                orderId = orderId,
                onViewOrderClick = { id -> navController.navigate(Screen.OrderDetail.createRoute(id)) },
                onContinueShoppingClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OrderHistory.route) {
            val ordersViewModel = OrdersViewModel(
                getCustomerOrdersUseCase = container.getCustomerOrdersUseCase,
                getOrderDetailUseCase = container.getOrderDetailUseCase,
                cancelOrderUseCase = container.cancelOrderUseCase,
                generateInvoicePdfUseCase = container.generateInvoicePdfUseCase
            )
            OrderHistoryScreen(
                viewModel = ordersViewModel,
                onOrderClick = { id -> navController.navigate(Screen.OrderDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val ordersViewModel = OrdersViewModel(
                getCustomerOrdersUseCase = container.getCustomerOrdersUseCase,
                getOrderDetailUseCase = container.getOrderDetailUseCase,
                cancelOrderUseCase = container.cancelOrderUseCase,
                generateInvoicePdfUseCase = container.generateInvoicePdfUseCase
            )
            OrderDetailScreen(
                orderId = orderId,
                viewModel = ordersViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AddressList.route) {
            val addresses by container.getSavedAddressesUseCase("usr_demo_01")
                .collectAsState(initial = emptyList())
            AddressListScreen(
                addresses = addresses,
                onSaveAddress = { newOrUpdated ->
                    scope.launch { container.saveAddressUseCase("usr_demo_01", newOrUpdated) }
                },
                onDeleteAddress = { addrId ->
                    scope.launch { container.deleteAddressUseCase("usr_demo_01", addrId) }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ProfileEdit.route) {
            ProfileEditScreen(
                viewModel = authViewModel,
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = { mode ->
                    scope.launch { container.userPreferences.setThemeMode(mode) }
                },
                onSaved = { navController.popBackStack() },
                onSignOutClick = {
                    authViewModel.signOut {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.OwnerDashboard.route) {
            val ownerDashboardViewModel = OwnerDashboardViewModel(
                getFinancialStatementUseCase = container.getFinancialStatementUseCase,
                getInventoryListUseCase = container.getInventoryListUseCase,
                getCustomerOrdersUseCase = container.getCustomerOrdersUseCase
            )
            OwnerDashboardScreen(
                viewModel = ownerDashboardViewModel,
                onNavigateToInventory = { navController.navigate(Screen.InventoryOverview.route) },
                onNavigateToPos = { navController.navigate(Screen.PosSales.route) },
                onNavigateToExpenses = { navController.navigate(Screen.Expenses.route) },
                onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) },
                onNavigateToSuppliers = { navController.navigate(Screen.Suppliers.route) },
                onNavigateToStaff = { navController.navigate(Screen.StaffManagement.route) },
                onNavigateToSettings = { navController.navigate(Screen.BusinessSettings.route) },
                onSwitchToCustomerView = {
                    authViewModel.switchRoleForTesting(UserRole.CUSTOMER)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OwnerDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.InventoryOverview.route) {
            val inventoryViewModel = InventoryViewModel(
                getInventoryListUseCase = container.getInventoryListUseCase,
                adjustStockUseCase = container.adjustStockUseCase
            )
            InventoryScreen(
                viewModel = inventoryViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PosSales.route) {
            val posViewModel = PosViewModel(
                getProductsUseCase = container.getProductsUseCase,
                processPosSaleUseCase = container.processPosSaleUseCase,
                pricingUseCase = container.calculatePricingUseCase
            )
            PosScreen(
                viewModel = posViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Expenses.route) {
            val expensesViewModel = ExpensesViewModel(
                getExpensesUseCase = container.getExpensesUseCase,
                recordExpenseUseCase = container.recordExpenseUseCase,
                deleteExpenseUseCase = container.deleteExpenseUseCase
            )
            ExpensesScreen(
                viewModel = expensesViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Analytics.route) {
            val analyticsViewModel = AnalyticsViewModel(
                getFinancialStatementUseCase = container.getFinancialStatementUseCase
            )
            AnalyticsScreen(
                viewModel = analyticsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Suppliers.route) {
            SuppliersScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.StaffManagement.route) {
            StaffManagementScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.BusinessSettings.route) {
            BusinessSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
