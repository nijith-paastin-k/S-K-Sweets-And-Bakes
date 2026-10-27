# Screen Inventory & UI Specification — S K Sweets & Bakes

This inventory documents all customer and owner/staff screens implemented in the application with
Jetpack Compose and Material 3.

---

## 1. Customer Flow Screens (46 Screens / Sub-Flows)

### Authentication & Profile Management

1. `SplashScreen`: App branding, session verification, and role-based redirect.
2. `WelcomeScreen`: Store introduction, quick value propositions, sign-in CTA.
3. `PhoneLoginScreen`: Mobile number input with validation and country prefix.
4. `OtpVerificationScreen`: 6-digit OTP code entry, resend timer.
5. `ProfileSetupScreen`: First-time user profile registration (name, email).
6. `ProfileEditScreen`: Edit account information.
7. `AddressListScreen`: Saved delivery addresses with default selection.
8. `AddEditAddressScreen`: Address form with landmark, pincode, GPS support.
9. `NotificationPreferencesScreen`: FCM alerts toggle for order and offers.
10. `HelpSupportScreen`: Contact phone, WhatsApp direct link, FAQ section.
11. `PrivacyPolicyScreen`: Privacy policies, store terms, and dietary disclosures.
12. `AccountDeletionScreen`: Self-service data deletion request.

### Catalog & Browsing

13. `HomeScreen`: Banners, featured sweets, best-sellers, quick categories.
14. `CategoriesScreen`: Grid view of all sweet and savoury categories.
15. `ProductListingScreen`: Paginated product cards with pricing and stock tag.
16. `ProductSearchScreen`: Real-time instant search input.
17. `SearchFiltersScreen`: Filter sheet for dietary, measurement, price range.
18. `ProductSortingSheet`: Sort by Price, Popularity, Newest.
19. `ProductDetailScreen`: Detailed description, storage info, shelf life, gallery.
20. `ProductGalleryScreen`: Full-screen zoomable photo carousel.
21. `MeasurementPicker`: Granular quantity selector (250g, 500g, 1kg, 2kg, custom).
22. `IngredientsAllergensDialog`: Nutritional breakdown, allergens (nuts, dairy).
23. `StockAvailabilityBadge`: Live on-hand stock and low-stock indicators.
24. `ReviewsListScreen`: Star ratings, customer comments, verified badge.
25. `WishlistScreen`: User's bookmarked sweets for quick ordering.

### Cart, Checkout & Orders

26. `CartScreen`: Line item summary, live price total, delivery fee, coupons.
27. `CartQuantityEditor`: In-cart quantity adjustment with minimum constraints.
28. `CouponSelectionScreen`: List of available promo codes with copy/apply action.
29. `FulfillmentModeSelector`: Doorstep Delivery vs. Shop Pickup toggle.
30. `DeliveryAddressPicker`: Select existing address or add a new one.
31. `PickupSlotPicker`: Choose pickup date and time window.
32. `CheckoutSummaryScreen`: Complete breakdown (subtotal, taxes, delivery, total).
33. `PaymentMethodSelector`: UPI, Cards, Netbanking, Cash on Pickup.
34. `PaymentInitiationScreen`: Gateway bridge with animated loading status.
35. `PaymentResultScreen`: Authoritative payment success/failure state.
36. `OrderConfirmationScreen`: Order ID, receipt summary, celebration illustration.
37. `OrderHistoryScreen`: Active and past orders with status chips.
38. `OrderDetailScreen`: Complete order snapshot with items, fees, and tracking.
39. `OrderTrackingScreen`: Stepper timeline (Confirmed -> Preparing -> Ready -> Delivered).
40. `OrderCancellationDialog`: Self-cancellation with reason prompt.
41. `RefundStatusScreen`: Refund transaction status and timeline.
42. `WriteReviewScreen`: 5-star rating and written review submission.
43. `LoyaltyPointsScreen`: Points balance, rewards history, and redemption rules.
44. `ShareProductHandler`: Native Android Sharesheet sharing with deep link.
45. `DeepLinkHandler`: Auto-navigation to specific product from shared links.
46. `SupportChatScreen`: In-app ticket and message submission.

---

## 2. Owner & Staff Flow Screens

47. `OwnerDashboardScreen`: KPI summary cards (sales, orders, profit, low stock).
48. `ProductManagementScreen`: List of products with status chips, search, and edit action.
49. `AddEditProductScreen`: Comprehensive product editor with measurement types and pricing.
50. `SharedInventoryScreen`: Live stock counts, low stock alerts, and quick adjustments.
51. `StockAdjustmentDialog`: Adjust on-hand stock with reason logging.
52. `ReceiveStockScreen`: Log supplier receipts with quantities and purchase costs.
53. `StockMovementHistoryScreen`: Immutable audit trail of all inventory events.
54. `PosSalesScreen`: Counter checkout interface with product grid and weight calculator.
55. `PosReceiptScreen`: Printable counter receipt with QR code.
56. `OrderFulfillmentScreen`: Owner/staff pipeline to advance order statuses.
57. `ExpensesScreen`: Record and categorize shop operating expenses.
58. `SuppliersScreen`: Supplier contact directory and purchase history.
59. `AnalyticsScreen`: Executive sales, COGS, gross profit, and operating profit reports.
60. `StaffManagementScreen`: Staff account list, role assignment (Cashier, Manager).
61. `BusinessSettingsScreen`: Store operating hours, delivery radius, taxes.
