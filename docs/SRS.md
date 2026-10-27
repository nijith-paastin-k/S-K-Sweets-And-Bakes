# Software Requirements Specification (SRS) — S K Sweets Android Application

## 1. Introduction

### 1.1 Purpose

This document specifies the software requirements for the **S K Sweets** Android e-commerce and
point-of-sale (POS) business management system. The application serves both retail sweet shop
customers and shop staff/owners with unified product, order, and inventory management.

### 1.2 Scope

- **Customer Mobile Application**: Browsing sweets, savouries, and bakery items with flexible
  weight/volume/piece quantity selectors, cart management, checkout with doorstep delivery or store
  pickup, UPI/Card/Netbanking payments, order tracking, and reviews.
- **Shop Owner & Manager Suite**: Real-time sales dashboard, product catalogue management, pricing
  and measurement rules, shared inventory ledger, purchase & supplier management, expense tracking,
  gross/operating profit analytics, coupons, staff RBAC, and business settings.
- **Counter POS (Point-of-Sale)**: In-store physical counter checkout supporting fast search,
  weighed items, receipt generation, and atomic shared inventory depletion.
- **Backend & Cloud Infrastructure**: Cloud Firestore, Firebase Auth, Cloud Storage, Firebase Cloud
  Messaging, and TypeScript-based Firebase Cloud Functions for trusted server-side computation.

---

## 2. Overall Description

### 2.1 User Classes and Characteristics

1. **Customer**: Authenticates via phone number OTP, browses products, places delivery or pickup
   orders, pays online, and tracks fulfillment.
2. **Shop Owner**: Administrative superuser with unrestricted access to financial metrics, pricing,
   stock, staff accounts, expenses, and configuration.
3. **Manager**: Elevated staff member permitted to oversee orders, stock movements, and customer
   support.
4. **Cashier / POS Operator**: Restricted staff role capable of processing physical counter sales
   and viewing active walk-in transactions. Denied access to owner financial reports, net profits,
   and staff management.
5. **Inventory Clerk**: Staff role restricted to receiving stock, conducting manual inventory
   counts, and reporting wastage.
6. **Order Processing Staff**: Staff role managing kitchen prep, packing, and dispatch workflows.

### 2.2 Operating Environment

- Client: Android OS 6.0 (API 23 - Marshmallow) through Android 16 (API 37).
- Framework: Modern Jetpack Compose, Material Design 3, Kotlin Coroutines & Flow.
- Backend: Google Cloud Firebase (Firestore, Storage, Authentication, Cloud Functions, FCM).

---

## 3. System Features & Functional Requirements

### 3.1 Product Measurement & Pricing System

- **Weight-based items** (e.g., Kaju Katli, Mysore Pak, Laddu, Mixture):
    - Standard increments: 250g, 500g, 750g, 1kg, 1.25kg, 1.5kg, 1.75kg, 2kg, 2.25kg, 2.5kg, plus
      custom configurable amounts.
    - Price equation: `Price = (Grams * PricePerKg) / 1000`.
- **Volume-based items** (e.g., Badam Milk, Syrups):
    - Units: Millilitres (ml) and Litres (L).
    - Price equation: `Price = (Millilitres * PricePerLitre) / 1000`.
- **Piece-based items** (e.g., Samosa, Pastry, Single Sweets):
    - Price equation: `Price = PieceCount * PricePerPiece`.
- **Fixed-price Packs & Assorted Boxes**:
    - Price equation: `Price = PackCount * PricePerPack`.
- **Monetary Safety**: All monetary values are maintained as integer paise (1 INR = 100 paise) to
  eliminate floating-point rounding errors.
- **Authoritative Validation**: Client-provided line totals and discounts are treated as untrusted
  and recalculation occurs on the backend before order generation.

### 3.2 Unified Shared Inventory Engine

- Shared ledger between physical counter sales (POS) and online e-commerce orders.
- Stock statuses:
    - `onHand`: Total physically present stock in grams/ml/units.
    - `reserved`: Stock held for active online checkouts awaiting payment confirmation.
    - `available`: Calculated as `onHand - reserved`.
- Atomic Firestore transactions prevent overselling when online customers and physical counter
  customers purchase simultaneously.
- Automatic release of reserved stock upon payment timeout or cancellation.

### 3.3 Payment Gateway Orchestration

- Server-side payment order initialization (e.g., Razorpay/UPI).
- Client-side payment launch with signature verification.
- Asynchronous webhook reception with HMAC-SHA256 signature validation and idempotency checking.
- Automated refund tracking and failure recovery.

### 3.4 Role-Based Access Control (RBAC)

- Custom claims and Firestore security rules enforce role checks.
- Sensitive collections (`expenses`, `dailySummaries`, `auditLogs`, `staff`) deny read/write to
  non-owner roles.

---

## 4. Non-Functional Requirements

### 4.1 Security

- All private user data is accessible only by the owning user or authorized shop admins.
- Deny-by-default Firestore and Cloud Storage security policies.
- Zero secrets or service account keys in the Android APK.

### 4.2 Performance & Reliability

- Compose UI responsiveness targeting 60fps / 120fps.
- Firestore offline persistence for smooth catalog browsing.
- Immediate UI feedback with loading, error, empty, and success states for all network operations.
