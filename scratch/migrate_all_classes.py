import os
import re

BASE = "app/src/main/java/com/skylink/sksweetsandbakes"

def copy_and_update_package(src_rel, dst_rel, new_pkg):
    src = os.path.join(BASE, src_rel)
    dst = os.path.join(BASE, dst_rel)
    if not os.path.exists(src):
        print(f"Skipping {src}, does not exist")
        return
    with open(src, "r") as f:
        content = f.read()

    # Replace package header
    content = re.sub(r"^package\s+[a-zA-Z0-9_\.]+", f"package {new_pkg}", content, count=1)

    os.makedirs(os.path.dirname(dst), exist_ok=True)
    with open(dst, "w") as f:
        f.write(content)
    print(f"Moved {src_rel} -> {dst_rel} with pkg {new_pkg}")

# Domain use cases
copy_and_update_package("domain/usecase/AuthUseCases.kt", "domain/auth/AuthUseCases.kt", "com.skylink.sksweetsandbakes.domain.auth")
copy_and_update_package("domain/usecase/CartUseCases.kt", "domain/cart/CartUseCases.kt", "com.skylink.sksweetsandbakes.domain.cart")
copy_and_update_package("domain/usecase/ValidateOrderUseCase.kt", "domain/cart/ValidateOrderUseCase.kt", "com.skylink.sksweetsandbakes.domain.cart")
copy_and_update_package("domain/usecase/OrderUseCases.kt", "domain/orders/OrderUseCases.kt", "com.skylink.sksweetsandbakes.domain.orders")
copy_and_update_package("domain/usecase/InvoiceUseCases.kt", "domain/orders/InvoiceUseCases.kt", "com.skylink.sksweetsandbakes.domain.orders")
copy_and_update_package("domain/usecase/NotificationUseCases.kt", "domain/orders/NotificationUseCases.kt", "com.skylink.sksweetsandbakes.domain.orders")
copy_and_update_package("domain/usecase/CatalogUseCases.kt", "domain/dashboard/CatalogUseCases.kt", "com.skylink.sksweetsandbakes.domain.dashboard")
copy_and_update_package("domain/usecase/PosUseCases.kt", "domain/dashboard/PosUseCases.kt", "com.skylink.sksweetsandbakes.domain.dashboard")
copy_and_update_package("domain/usecase/InventoryUseCases.kt", "domain/dashboard/InventoryUseCases.kt", "com.skylink.sksweetsandbakes.domain.dashboard")
copy_and_update_package("domain/usecase/ExpenseUseCases.kt", "domain/dashboard/ExpenseUseCases.kt", "com.skylink.sksweetsandbakes.domain.dashboard")
copy_and_update_package("domain/usecase/AnalyticsUseCases.kt", "domain/dashboard/AnalyticsUseCases.kt", "com.skylink.sksweetsandbakes.domain.dashboard")
copy_and_update_package("domain/usecase/CalculatePricingUseCase.kt", "domain/dashboard/CalculatePricingUseCase.kt", "com.skylink.sksweetsandbakes.domain.dashboard")
copy_and_update_package("domain/usecase/CalculateFinancialsUseCase.kt", "domain/dashboard/CalculateFinancialsUseCase.kt", "com.skylink.sksweetsandbakes.domain.dashboard")

# Data repositories
copy_and_update_package("data/repository/AuthRepositoryImpl.kt", "data/auth/AuthRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.auth")
copy_and_update_package("data/repository/CartRepositoryImpl.kt", "data/cart/CartRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.cart")
copy_and_update_package("data/repository/OrderRepositoryImpl.kt", "data/orders/OrderRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.orders")
copy_and_update_package("data/repository/InvoiceRepositoryImpl.kt", "data/orders/InvoiceRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.orders")
copy_and_update_package("data/repository/NotificationRepositoryImpl.kt", "data/orders/NotificationRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.orders")
copy_and_update_package("data/repository/ProductRepositoryImpl.kt", "data/dashboard/ProductRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.dashboard")
copy_and_update_package("data/repository/InventoryRepositoryImpl.kt", "data/dashboard/InventoryRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.dashboard")
copy_and_update_package("data/repository/PosRepositoryImpl.kt", "data/dashboard/PosRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.dashboard")
copy_and_update_package("data/repository/ExpenseRepositoryImpl.kt", "data/dashboard/ExpenseRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.dashboard")
copy_and_update_package("data/repository/AnalyticsRepositoryImpl.kt", "data/dashboard/AnalyticsRepositoryImpl.kt", "com.skylink.sksweetsandbakes.data.dashboard")

# Presentation
copy_and_update_package("feature/auth/AuthViewModel.kt", "presentation/auth/AuthViewModel.kt", "com.skylink.sksweetsandbakes.presentation.auth")
copy_and_update_package("feature/auth/AuthScreens.kt", "presentation/auth/AuthScreens.kt", "com.skylink.sksweetsandbakes.presentation.auth")

copy_and_update_package("feature/cart/CartViewModel.kt", "presentation/cart/CartViewModel.kt", "com.skylink.sksweetsandbakes.presentation.cart")
copy_and_update_package("feature/cart/CartScreen.kt", "presentation/cart/CartScreen.kt", "com.skylink.sksweetsandbakes.presentation.cart")

copy_and_update_package("feature/orders/OrdersViewModel.kt", "presentation/orders/OrdersViewModel.kt", "com.skylink.sksweetsandbakes.presentation.orders")
copy_and_update_package("feature/orders/OrdersScreens.kt", "presentation/orders/OrdersScreens.kt", "com.skylink.sksweetsandbakes.presentation.orders")
copy_and_update_package("feature/checkout/CheckoutViewModel.kt", "presentation/orders/CheckoutViewModel.kt", "com.skylink.sksweetsandbakes.presentation.orders")
copy_and_update_package("feature/checkout/CheckoutScreens.kt", "presentation/orders/CheckoutScreens.kt", "com.skylink.sksweetsandbakes.presentation.orders")

copy_and_update_package("feature/home/HomeScreen.kt", "presentation/dashboard/HomeScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/home/HomeViewModel.kt", "presentation/dashboard/HomeViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/catalog/CatalogScreens.kt", "presentation/dashboard/CatalogScreens.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/catalog/CatalogViewModel.kt", "presentation/dashboard/CatalogViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/product/ProductDetailScreen.kt", "presentation/dashboard/ProductDetailScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/product/ProductDetailViewModel.kt", "presentation/dashboard/ProductDetailViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/admin/OwnerDashboardScreen.kt", "presentation/dashboard/OwnerDashboardScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/admin/OwnerDashboardViewModel.kt", "presentation/dashboard/OwnerDashboardViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/analytics/AnalyticsScreen.kt", "presentation/dashboard/AnalyticsScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/analytics/AnalyticsViewModel.kt", "presentation/dashboard/AnalyticsViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/inventory/InventoryScreen.kt", "presentation/dashboard/InventoryScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/inventory/InventoryViewModel.kt", "presentation/dashboard/InventoryViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/pos/PosScreen.kt", "presentation/dashboard/PosScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/pos/PosViewModel.kt", "presentation/dashboard/PosViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/expenses/ExpensesScreen.kt", "presentation/dashboard/ExpensesScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/expenses/ExpensesViewModel.kt", "presentation/dashboard/ExpensesViewModel.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/staff/StaffManagementScreen.kt", "presentation/dashboard/StaffManagementScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/suppliers/SuppliersScreen.kt", "presentation/dashboard/SuppliersScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")
copy_and_update_package("feature/settings/BusinessSettingsScreen.kt", "presentation/dashboard/BusinessSettingsScreen.kt", "com.skylink.sksweetsandbakes.presentation.dashboard")

print("Migration completed.")
