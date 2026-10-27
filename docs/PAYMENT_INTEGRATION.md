# Payment Gateway Integration Guide — S K Sweets & Bakes

## 1. Supported Payment Modes

- **UPI**: Google Pay, PhonePe, Paytm, BHIM, and any UPI Intent apps.
- **Debit & Credit Cards**: Visa, MasterCard, RuPay, Maestro.
- **Netbanking**: Major Indian Banks (SBI, HDFC, ICICI, Axis, Kotak, etc.).
- **Cash on Pickup**: Available exclusively for in-store pickup orders and physical counter POS
  sales.

---

## 2. Payment Flow Architecture

```
[Android Client]          [Cloud Functions Backend]       [Payment Gateway]
      |                                |                          |
      | 1. Request Order Creation      |                          |
      |------------------------------->|                          |
      |                                | 2. Create Gateway Order  |
      |                                |------------------------->|
      |                                |<-------------------------|
      |                                |    Returns Order ID      |
      | 3. Order ID & Token            |                          |
      |<-------------------------------|                          |
      |                                |                          |
      | 4. Launch Payment Sheet        |                          |
      |==========================================================>|
      |                                |                          |
      | 5. Payment Completed           |                          |
      |<==========================================================|
      |                                |                          |
      |                                | 6. Authoritative Webhook |
      |                                |<-------------------------|
      |                                |    (HMAC-SHA256 Signed)  |
      |                                |                          |
      |                                | 7. Verify Signature,     |
      |                                |    Confirm Order & Stock |
      | 8. Poll / Listen to Order Doc  |                          |
      |------------------------------->|                          |
      |    Status = CONFIRMED          |                          |
      |<-------------------------------|                          |
```

---

## 3. Security Requirements

1. **Zero Secret Storage**: Gateway API Secrets and Webhook Secrets are saved only in Cloud
   Functions environment secrets (`defineSecret('PAYMENT_KEY_SECRET')`). They are NEVER compiled
   into the Android APK.
2. **HMAC-SHA256 Signature Verification**: Every webhook payload must be checked against
   `X-Razorpay-Signature` or gateway signature header before executing any order transition.
3. **Idempotency**: Webhook events are checked against `processedWebhooks/{eventId}`. If already
   processed, a 200 OK is returned immediately without double processing stock or balances.
4. **Authoritative Confirmation**: The Android client never marks an order `CONFIRMED` on its own.
   The UI observes the Firestore document `orders/{orderId}`, which is updated solely by the backend
   webhook or trusted verification function.
