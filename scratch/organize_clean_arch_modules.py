import os

BASE = "app/src/main/java/com/skylink/sksweetsandbakes"

def write_file(rel_path, content):
    full_path = os.path.join(BASE, rel_path)
    os.makedirs(os.path.dirname(full_path), exist_ok=True)
    with open(full_path, "w") as f:
        f.write(content.strip() + "\n")
    print(f"Wrote {rel_path}")

def setup_auth_module():
    write_file("domain/auth/UserProfile.kt", """
package com.skylink.sksweetsandbakes.domain.auth

typealias UserProfile = com.skylink.sksweetsandbakes.domain.model.UserProfile
""")
    write_file("domain/auth/AuthRepository.kt", """
package com.skylink.sksweetsandbakes.domain.auth

typealias AuthRepository = com.skylink.sksweetsandbakes.domain.repository.AuthRepository
""")
    write_file("domain/auth/AuthUseCases.kt", """
package com.skylink.sksweetsandbakes.domain.auth

typealias GetCurrentUserUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetCurrentUserUseCase
typealias SignInWithPhoneUseCase = com.skylink.sksweetsandbakes.domain.usecase.SignInWithPhoneUseCase
typealias VerifyOtpUseCase = com.skylink.sksweetsandbakes.domain.usecase.VerifyOtpUseCase
typealias UpdateUserProfileUseCase = com.skylink.sksweetsandbakes.domain.usecase.UpdateUserProfileUseCase
typealias SwitchRoleUseCase = com.skylink.sksweetsandbakes.domain.usecase.SwitchRoleUseCase
typealias SignOutUseCase = com.skylink.sksweetsandbakes.domain.usecase.SignOutUseCase
typealias SetRememberMeUseCase = com.skylink.sksweetsandbakes.domain.usecase.SetRememberMeUseCase
""")
    write_file("data/auth/AuthRepositoryImpl.kt", """
package com.skylink.sksweetsandbakes.data.auth

typealias AuthRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.AuthRepositoryImpl
""")
    write_file("presentation/auth/AuthModule.kt", """
package com.skylink.sksweetsandbakes.presentation.auth

typealias AuthViewModel = com.skylink.sksweetsandbakes.feature.auth.AuthViewModel
typealias AuthUiState = com.skylink.sksweetsandbakes.feature.auth.AuthUiState
""")

def setup_dashboard_module():
    write_file("domain/dashboard/DashboardModels.kt", """
package com.skylink.sksweetsandbakes.domain.dashboard

typealias Product = com.skylink.sksweetsandbakes.domain.model.Product
typealias Category = com.skylink.sksweetsandbakes.domain.model.Category
typealias Inventory = com.skylink.sksweetsandbakes.domain.model.Inventory
typealias PosSale = com.skylink.sksweetsandbakes.domain.model.PosSale
typealias Expense = com.skylink.sksweetsandbakes.domain.model.Expense
typealias DailySummary = com.skylink.sksweetsandbakes.domain.model.DailySummary
typealias Supplier = com.skylink.sksweetsandbakes.domain.model.Supplier
typealias Review = com.skylink.sksweetsandbakes.domain.model.Review
typealias FinancialStatement = com.skylink.sksweetsandbakes.domain.usecase.FinancialStatement
""")
    write_file("domain/dashboard/DashboardRepositories.kt", """
package com.skylink.sksweetsandbakes.domain.dashboard

typealias ProductRepository = com.skylink.sksweetsandbakes.domain.repository.ProductRepository
typealias InventoryRepository = com.skylink.sksweetsandbakes.domain.repository.InventoryRepository
typealias PosRepository = com.skylink.sksweetsandbakes.domain.repository.PosRepository
typealias ExpenseRepository = com.skylink.sksweetsandbakes.domain.repository.ExpenseRepository
typealias AnalyticsRepository = com.skylink.sksweetsandbakes.domain.repository.AnalyticsRepository
""")
    write_file("domain/dashboard/DashboardUseCases.kt", """
package com.skylink.sksweetsandbakes.domain.dashboard

typealias GetProductsUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetProductsUseCase
typealias GetProductDetailUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetProductDetailUseCase
typealias SearchProductsUseCase = com.skylink.sksweetsandbakes.domain.usecase.SearchProductsUseCase
typealias AddReviewUseCase = com.skylink.sksweetsandbakes.domain.usecase.AddReviewUseCase
typealias ProcessPosSaleUseCase = com.skylink.sksweetsandbakes.domain.usecase.ProcessPosSaleUseCase
typealias GetRecentPosSalesUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetRecentPosSalesUseCase
typealias GetInventoryListUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetInventoryListUseCase
typealias AdjustStockUseCase = com.skylink.sksweetsandbakes.domain.usecase.AdjustStockUseCase
typealias GetExpensesUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetExpensesUseCase
typealias RecordExpenseUseCase = com.skylink.sksweetsandbakes.domain.usecase.RecordExpenseUseCase
typealias DeleteExpenseUseCase = com.skylink.sksweetsandbakes.domain.usecase.DeleteExpenseUseCase
typealias GetFinancialStatementUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetFinancialStatementUseCase
typealias CalculateFinancialsUseCase = com.skylink.sksweetsandbakes.domain.usecase.CalculateFinancialsUseCase
typealias CalculatePricingUseCase = com.skylink.sksweetsandbakes.domain.usecase.CalculatePricingUseCase
""")
    write_file("data/dashboard/DashboardRepositoriesImpl.kt", """
package com.skylink.sksweetsandbakes.data.dashboard

typealias ProductRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.ProductRepositoryImpl
typealias InventoryRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.InventoryRepositoryImpl
typealias PosRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.PosRepositoryImpl
typealias ExpenseRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.ExpenseRepositoryImpl
typealias AnalyticsRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.AnalyticsRepositoryImpl
""")
    write_file("presentation/dashboard/DashboardViewModels.kt", """
package com.skylink.sksweetsandbakes.presentation.dashboard

typealias HomeViewModel = com.skylink.sksweetsandbakes.feature.home.HomeViewModel
typealias CatalogViewModel = com.skylink.sksweetsandbakes.feature.catalog.CatalogViewModel
typealias ProductDetailViewModel = com.skylink.sksweetsandbakes.feature.product.ProductDetailViewModel
typealias OwnerDashboardViewModel = com.skylink.sksweetsandbakes.feature.admin.OwnerDashboardViewModel
typealias AnalyticsViewModel = com.skylink.sksweetsandbakes.feature.analytics.AnalyticsViewModel
typealias InventoryViewModel = com.skylink.sksweetsandbakes.feature.inventory.InventoryViewModel
typealias PosViewModel = com.skylink.sksweetsandbakes.feature.pos.PosViewModel
typealias ExpensesViewModel = com.skylink.sksweetsandbakes.feature.expenses.ExpensesViewModel
""")

def setup_cart_module():
    write_file("domain/cart/CartModels.kt", """
package com.skylink.sksweetsandbakes.domain.cart

typealias Cart = com.skylink.sksweetsandbakes.domain.model.Cart
typealias CartItem = com.skylink.sksweetsandbakes.domain.model.CartItem
typealias Coupon = com.skylink.sksweetsandbakes.domain.model.Coupon
""")
    write_file("domain/cart/CartRepository.kt", """
package com.skylink.sksweetsandbakes.domain.cart

typealias CartRepository = com.skylink.sksweetsandbakes.domain.repository.CartRepository
""")
    write_file("domain/cart/CartUseCases.kt", """
package com.skylink.sksweetsandbakes.domain.cart

typealias GetCartUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetCartUseCase
typealias AddToCartUseCase = com.skylink.sksweetsandbakes.domain.usecase.AddToCartUseCase
typealias UpdateCartQuantityUseCase = com.skylink.sksweetsandbakes.domain.usecase.UpdateCartQuantityUseCase
typealias RemoveFromCartUseCase = com.skylink.sksweetsandbakes.domain.usecase.RemoveFromCartUseCase
typealias ClearCartUseCase = com.skylink.sksweetsandbakes.domain.usecase.ClearCartUseCase
typealias ApplyCouponUseCase = com.skylink.sksweetsandbakes.domain.usecase.ApplyCouponUseCase
typealias RemoveCouponUseCase = com.skylink.sksweetsandbakes.domain.usecase.RemoveCouponUseCase
typealias ValidateOrderUseCase = com.skylink.sksweetsandbakes.domain.usecase.ValidateOrderUseCase
""")
    write_file("data/cart/CartRepositoryImpl.kt", """
package com.skylink.sksweetsandbakes.data.cart

typealias CartRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.CartRepositoryImpl
""")
    write_file("presentation/cart/CartPresentation.kt", """
package com.skylink.sksweetsandbakes.presentation.cart

typealias CartViewModel = com.skylink.sksweetsandbakes.feature.cart.CartViewModel
typealias CartUiState = com.skylink.sksweetsandbakes.feature.cart.CartUiState
""")

def setup_orders_module():
    write_file("domain/orders/OrdersModels.kt", """
package com.skylink.sksweetsandbakes.domain.orders

typealias Order = com.skylink.sksweetsandbakes.domain.model.Order
typealias OrderStatus = com.skylink.sksweetsandbakes.domain.model.OrderStatus
typealias FulfillmentType = com.skylink.sksweetsandbakes.domain.model.FulfillmentType
typealias PaymentMethod = com.skylink.sksweetsandbakes.domain.model.PaymentMethod
typealias DeliveryAddress = com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
typealias PickupSlot = com.skylink.sksweetsandbakes.domain.model.PickupSlot
typealias OrderPricing = com.skylink.sksweetsandbakes.domain.model.OrderPricing
typealias OrderPayment = com.skylink.sksweetsandbakes.domain.model.OrderPayment
typealias Invoice = com.skylink.sksweetsandbakes.domain.model.Invoice
typealias InvoiceItem = com.skylink.sksweetsandbakes.domain.model.InvoiceItem
""")
    write_file("domain/orders/OrdersRepositories.kt", """
package com.skylink.sksweetsandbakes.domain.orders

typealias OrderRepository = com.skylink.sksweetsandbakes.domain.repository.OrderRepository
typealias InvoiceRepository = com.skylink.sksweetsandbakes.domain.repository.InvoiceRepository
typealias NotificationRepository = com.skylink.sksweetsandbakes.domain.repository.NotificationRepository
""")
    write_file("domain/orders/OrdersUseCases.kt", """
package com.skylink.sksweetsandbakes.domain.orders

typealias GetCustomerOrdersUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetCustomerOrdersUseCase
typealias GetOrderDetailUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetOrderDetailUseCase
typealias PlaceOrderUseCase = com.skylink.sksweetsandbakes.domain.usecase.PlaceOrderUseCase
typealias CancelOrderUseCase = com.skylink.sksweetsandbakes.domain.usecase.CancelOrderUseCase
typealias GetSavedAddressesUseCase = com.skylink.sksweetsandbakes.domain.usecase.GetSavedAddressesUseCase
typealias SaveAddressUseCase = com.skylink.sksweetsandbakes.domain.usecase.SaveAddressUseCase
typealias DeleteAddressUseCase = com.skylink.sksweetsandbakes.domain.usecase.DeleteAddressUseCase
typealias GenerateInvoicePdfUseCase = com.skylink.sksweetsandbakes.domain.usecase.GenerateInvoicePdfUseCase
typealias SendPaymentNotificationUseCase = com.skylink.sksweetsandbakes.domain.usecase.SendPaymentNotificationUseCase
typealias RegisterFcmTokenUseCase = com.skylink.sksweetsandbakes.domain.usecase.RegisterFcmTokenUseCase
""")
    write_file("data/orders/OrdersRepositoriesImpl.kt", """
package com.skylink.sksweetsandbakes.data.orders

typealias OrderRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.OrderRepositoryImpl
typealias InvoiceRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.InvoiceRepositoryImpl
typealias NotificationRepositoryImpl = com.skylink.sksweetsandbakes.data.repository.NotificationRepositoryImpl
""")
    write_file("presentation/orders/OrdersPresentation.kt", """
package com.skylink.sksweetsandbakes.presentation.orders

typealias OrdersViewModel = com.skylink.sksweetsandbakes.feature.orders.OrdersViewModel
typealias OrdersUiState = com.skylink.sksweetsandbakes.feature.orders.OrdersUiState
typealias CheckoutViewModel = com.skylink.sksweetsandbakes.feature.checkout.CheckoutViewModel
typealias CheckoutUiState = com.skylink.sksweetsandbakes.feature.checkout.CheckoutUiState
""")

def main():
    setup_auth_module()
    setup_dashboard_module()
    setup_cart_module()
    setup_orders_module()
    print("Clean Architecture module partitioning complete!")

if __name__ == "__main__":
    main()
