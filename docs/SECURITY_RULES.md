# Security Rules & Permission Boundaries — S K Sweets & Bakes

## 1. Principles of Security

1. **Deny-by-Default**: Every path in Cloud Firestore and Cloud Storage is blocked unless explicitly
   permitted by a rule.
2. **Untrusted Client Data**: Clients can never write trusted prices, payment status, inventory
   stock counts, or daily sales summaries directly.
3. **Role Escalation Prevention**: Users cannot set their own role or custom claims to `owner` or
   `manager`. Role updates are strictly restricted to the Shop Owner or trusted Cloud Functions
   using the Firebase Admin SDK.
4. **Least Privilege for Staff**:
    - Cashiers have access only to product reading, customer order status updating, and POS
      transaction creation.
    - Cashiers CANNOT read `expenses/`, `dailySummaries/`, `staff/`, or audit logs.
    - Inventory clerks can log stock receiving and movements, but cannot access financial
      statements.

---

## 2. Firestore Rules Breakdown

| Collection                 | Read Permission                 | Write / Create Permission            | Notes                                                                     |
|----------------------------|---------------------------------|--------------------------------------|---------------------------------------------------------------------------|
| `categories/`              | Public (anyone)                 | Owner only                           | Anyone can browse categories                                              |
| `products/`                | Public (anyone)                 | Owner only                           | Anyone can view sweets catalog                                            |
| `inventory/`               | Staff (Cashier/Mgr/Owner)       | Owner only                           | Direct writes blocked; POS/Online sales use transactional Cloud Functions |
| `inventoryMovements/`      | Staff only                      | Staff / Cloud Function               | Audit log of every stock change                                           |
| `users/{userId}`           | User Self or Owner              | Self (name/email only) or Owner      | Role/loyalty points are immutable by user self                            |
| `users/{userId}/addresses` | User Self only                  | User Self only                       | Private delivery addresses                                                |
| `orders/{orderId}`         | Order owner or Staff            | Customer create, Staff status update | Customers cannot alter price/payment                                      |
| `posSales/{saleId}`        | Staff only                      | Staff create, Owner edit             | Counter sales log                                                         |
| `expenses/{expenseId}`     | Owner only                      | Owner only                           | Business expenses hidden from cashiers                                    |
| `dailySummaries/{date}`    | Owner only                      | Cloud Functions only                 | Authoritative financial summaries                                         |
| `suppliers/`, `purchases/` | Manager, Inventory Clerk, Owner | Manager, Inventory Clerk, Owner      | Procurement and ingredient costs                                          |
| `coupons/`                 | Public read (active)            | Owner write                          | Promotional discount codes                                                |

---

## 3. Storage Rules Breakdown

- **Product & Category Images**: Public read, restricted image upload (max 5 MB, MIME `image/*`).
- **Expense Receipts**: Authenticated owner/staff write, restricted to image formats.
- **Profile Photos**: Public read, authenticated user write for their own directory.
