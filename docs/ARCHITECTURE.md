# Software Architecture — S K Sweets & Bakes

## 1. Architectural Overview

The application adheres to **Clean Architecture** and **MVVM** (Model-View-ViewModel) design
principles, tailored for Jetpack Compose, Kotlin Coroutines, and Google Cloud Firebase.

```
+-------------------------------------------------------------+
|                      Presentation Layer                     |
|  - Jetpack Compose UI (Screens, Components, Theme)          |
|  - ViewModels (StateFlow, UI State, Event Handlers)         |
|  - Navigation (Navigation Compose, Role-Guarded Destinations)|
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                         Domain Layer                        |
|  - Domain Models (Product, Order, InventoryMovement, User)  |
|  - Repository Interfaces (ProductRepository, AuthRepo, ...)  |
|  - Use Cases (CalculatePricingUseCase, PlaceOrderUseCase)   |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                          Data Layer                         |
|  - Repository Implementations                               |
|  - Remote Data Sources (Firestore, Firebase Auth, Functions) |
|  - Local Data Sources (DataStore Preferences, Cache)        |
|  - Data Mappers (Entity <-> DTO <-> Domain)                 |
+-------------------------------------------------------------+
```

---

## 2. Package Structure

The codebase is organized into modular packages under `com.skylink.sksweetsandbakes`:

```
com.skylink.sksweetsandbakes/
|-- core/
|   |-- common/          # Result wrappers, AppError, DispatcherProvider
|   |-- designsystem/    # S K Sweets theme, color palettes, typography, components
|   |-- navigation/      # Navigation graphs, typed routes, role-based guard
|   |-- network/         # Network connectivity monitor, HTTP clients
|   |-- firebase/        # Firebase provider, emulator config, collections
|   |-- security/        # Role definitions, permission checks, tokens
|   `-- util/            # CurrencyFormatter, DateUtils, Extension functions
|
|-- data/
|   |-- remote/          # Firestore data sources, Cloud Functions callers
|   |-- local/           # DataStore preferences (session, theme, cart cache)
|   |-- repository/      # Repository implementations
|   |-- mapper/          # Mappers between Firestore documents and Domain models
|   `-- model/           # Data transfer objects (DTOs) with serialization
|
|-- domain/
|   |-- model/           # Core domain entities (Product, CartItem, Order, UserProfile)
|   |-- repository/      # Repository interfaces
|   `-- usecase/         # Core business logic use cases (pricing, stock check, etc.)
|
|-- feature/
|   |-- auth/            # Phone login, OTP verification, Profile setup
|   |-- home/            # Customer home screen, category banners, quick picks
|   |-- catalog/         # Category browsing, product listing, search & filters
|   |-- product/         # Product details, image gallery, measurement picker
|   |-- cart/            # Shopping cart, quantity editor, coupon input
|   |-- checkout/        # Address selection, delivery/pickup slots, order summary
|   |-- payment/         # Gateway launcher, payment result listener
|   |-- orders/          # Customer order history, live tracking, cancellation
|   |-- reviews/         # Verified purchase review submission and ratings
|   |-- profile/         # User profile, address manager, notification prefs
|   |-- admin/           # Owner dashboard, executive KPIs, sales charts
|   |-- inventory/       # Stock overview, adjustments, receiving, movements
|   |-- pos/             # Counter sales interface for walk-in customers
|   |-- suppliers/       # Supplier directory, purchase orders, invoice tracking
|   |-- expenses/        # Business expense recording and categorized logging
|   |-- analytics/       # In-depth financial, profitability & sales reports
|   |-- coupons/         # Discount coupons and promo campaign manager
|   |-- staff/           # Staff accounts, roles, and permission assignments
|   `-- settings/        # Store hours, delivery radius, shop configuration
|
`-- di/                  # Dependency Injection container / Service Locator
```

---

## 3. Role-Based Navigation Routing

The application dynamically renders destinations based on the authenticated user's role:

- **Unauthenticated**: Splash -> Welcome -> Phone Login -> OTP Verification.
- **Customer Role**: Home, Catalog, Product Details, Cart, Checkout, Order Tracking, Profile,
  Addresses.
- **Owner Role**: Full access to Owner Dashboard, Analytics, Inventory, POS, Products, Suppliers,
  Expenses, Staff Management, and Settings.
- **Cashier / POS Staff**: Direct access to POS Screen and recent walk-in transactions; strictly
  barred from Owner Analytics, Net Profit, and Staff Admin.
- **Inventory Clerk**: Access to Stock Overview, Stock Adjustment, and Purchase Receiving.

---

## 4. State Management and Concurrency

- **ViewModels**: Expose immutable `StateFlow<UiState>` to Compose UI.
- **Coroutines**: Structured concurrency using `viewModelScope` with dedicated `Dispatchers.IO` for
  Firebase and disk operations.
- **Error Handling**: Standardized `Result<T>` sealed hierarchy capturing Domain, Network,
  Authorization, and Validation errors with user-friendly error messages.
