import re

files_to_fix = [
    ("app/src/main/java/com/skylink/sksweetsandbakes/presentation/orders/OrdersScreens.kt", [
        (r"com\.skylink\.sksweetsandbakes\.feature\.cart\.BillRow", "com.skylink.sksweetsandbakes.presentation.cart.BillRow")
    ]),
    ("app/src/main/java/com/skylink/sksweetsandbakes/presentation/orders/CheckoutScreens.kt", [
        (r"com\.skylink\.sksweetsandbakes\.feature\.auth\.EditAddressDialog", "com.skylink.sksweetsandbakes.presentation.auth.EditAddressDialog")
    ]),
    ("app/src/main/java/com/skylink/sksweetsandbakes/presentation/dashboard/AnalyticsScreen.kt", [
        (r"import com\.skylink\.sksweetsandbakes\.feature\.admin\.KpiCard", ""),
        (r"com\.skylink\.sksweetsandbakes\.feature\.cart\.BillRow", "com.skylink.sksweetsandbakes.presentation.cart.BillRow")
    ]),
    ("app/src/main/java/com/skylink/sksweetsandbakes/presentation/dashboard/HomeScreen.kt", [
        (r"com\.skylink\.sksweetsandbakes\.feature\.cart\.BillRow", "com.skylink.sksweetsandbakes.presentation.cart.BillRow"),
        (r"com\.skylink\.sksweetsandbakes\.feature\.cart\.CartItemRow", "com.skylink.sksweetsandbakes.presentation.cart.CartItemRow"),
        (r"com\.skylink\.sksweetsandbakes\.feature\.cart\.CartViewModel", "com.skylink.sksweetsandbakes.presentation.cart.CartViewModel"),
        (r"import com\.skylink\.sksweetsandbakes\.feature\.catalog\.CatalogViewModel", ""),
        (r"com\.skylink\.sksweetsandbakes\.feature\.orders\.OrdersViewModel", "com.skylink.sksweetsandbakes.presentation.orders.OrdersViewModel")
    ]),
    ("app/src/main/java/com/skylink/sksweetsandbakes/core/navigation/AppNavGraph.kt", [
        (r"com\.skylink\.sksweetsandbakes\.feature\.admin\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.analytics\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.auth\.", "com.skylink.sksweetsandbakes.presentation.auth."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.catalog\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.cart\.", "com.skylink.sksweetsandbakes.presentation.cart."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.checkout\.", "com.skylink.sksweetsandbakes.presentation.orders."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.expenses\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.wishlist\.", "com.skylink.sksweetsandbakes.presentation.wishlist."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.home\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.inventory\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.orders\.", "com.skylink.sksweetsandbakes.presentation.orders."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.pos\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.product\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.settings\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.staff\.", "com.skylink.sksweetsandbakes.presentation.dashboard."),
        (r"com\.skylink\.sksweetsandbakes\.feature\.suppliers\.", "com.skylink.sksweetsandbakes.presentation.dashboard.")
    ])
]

for file_path, replacements in files_to_fix:
    with open(file_path, "r") as f:
        c = f.read()
    for pattern, repl in replacements:
        c = re.sub(pattern, repl, c)
    with open(file_path, "w") as f:
        f.write(c)
    print(f"Updated {file_path}")

