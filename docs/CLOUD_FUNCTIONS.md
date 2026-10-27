# Cloud Functions Specification & Architecture — S K Sweets & Bakes

## 1. Overview

Firebase Cloud Functions provides the authoritative, secure backend layer for S K Sweets.
Implemented in **TypeScript**, these server-side endpoints execute business operations that must
never be trusted to the Android client.

---

## 2. Function Endpoints & Responsibilities

### 2.1 Pricing & Checkout

1. `validateCartAndCheckout`:
    - Inputs: List of `productId`, `selectedQuantity`, `couponCode`, `fulfillmentType`,
      `deliveryAddress`.
    - Validates active product status, unit prices, minimums, maximums, and quantity increments.
    - Computes subtotal, coupon discounts, delivery fees, taxes, and grand total in integer paise.
    - Checks available stock (`onHand - reserved`).
    - Returns immutable checkout receipt token.

2. `reserveStock`:
    - Temporarily increments `reservedStock` for online checkout.
    - Creates `inventoryMovements` record with type `ONLINE_RESERVATION`.
    - Schedules reservation expiry (15 minutes).

3. `releaseExpiredReservations`:
    - Cron trigger (`*/5 * * * *`) finding reservations older than 15 minutes without payment.
    - Releases stock back to `availableStock`.

### 2.2 Payment Orchestration

4. `createPaymentOrder`:
    - Communicates with payment gateway API (e.g. Razorpay/Cashfree/UPI).
    - Generates gateway order ID and signs client token.
5. `handlePaymentWebhook`:
    - Receives HMAC-SHA256 signed payment events from gateway.
    - Enforces idempotency via webhook event ID.
    - Transitions order status from `PENDING_PAYMENT` to `CONFIRMED`.
    - Finalizes stock depletion: decrements `onHandStock` and decrements `reservedStock`.
6. `initiateRefund`:
    - Triggered on order cancellation before preparation.
    - Calls gateway refund API, records refund status in `orders/{id}` and `payments/{id}`.

### 2.3 Shared Inventory & POS

7. `processPosSale`:
    - Executed atomically in a Firestore transaction.
    - Verifies cashier credentials.
    - For each item, verifies `onHandStock - quantity >= 0`.
    - Decrements `onHandStock` and logs `POS_SALE` inventory movement.
    - Writes `posSales` document and returns printable receipt data.
8. `adjustInventory`:
    - Privileged function for manual stock reconciliation, receiving, or wastage.
    - Logs `inventoryMovements` with reason and operator ID.

### 2.4 Aggregation & Financial Reporting

9. `aggregateDailySummaries`:
    - Scheduled nightly or on-demand by owner.
    - Sums POS sales, online sales, returns, and expenses for the day.
    - Computes Gross Sales, Net Sales, COGS, Gross Profit, and Operating Profit.
    - Stores pre-computed summary in `dailySummaries/{YYYY-MM-DD}`.

### 2.5 Role Assignment & Security

10. `assignStaffRole`:
    - Only callable by authenticated `owner`.
    - Updates Firestore `users/{userId}.role` and sets Firebase Auth Custom Claims
      (`{ role: 'manager' | 'cashier' | ... }`).
