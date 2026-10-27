# Firestore Schema and Data Models — S K Sweets & Bakes

## 1. Schema Design Principles

- **Integer Currency**: All financial amounts (prices, discounts, taxes, totals) are stored as
  integer **paise** (1 INR = 100 paise) to prevent floating-point inaccuracies.
- **Base Units**: Stock quantities are recorded in base integer units:
    - Weight: Grams (`g`)
    - Volume: Millilitres (`ml`)
    - Pieces: Count (`pieces`)
    - Packs: Count (`packs`)
- **Server Timestamps**: Timestamps use Firestore `FieldValue.serverTimestamp()` on write.
- **Audit Trails**: Inventory movements and order state transitions are recorded in immutable
  subcollections or dedicated audit collections.

---

## 2. Collection Hierarchy

### 2.1 `users/{userId}`

Stores registered customer and staff profile records.

```json
{
  "userId": "string (auth uid)",
  "phoneNumber": "+919876543210",
  "displayName": "Ramesh Kumar",
  "email": "ramesh@example.com",
  "role": "customer | owner | manager | cashier | inventory_clerk",
  "loyaltyPoints": 150,
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

#### Subcollection: `users/{userId}/addresses/{addressId}`

```json
{
  "addressId": "string",
  "title": "Home | Office",
  "recipientName": "Ramesh Kumar",
  "phoneNumber": "+919876543210",
  "addressLine1": "Plot 42, Sweet Street",
  "addressLine2": "Near Town Hall",
  "landmark": "Opposite Clock Tower",
  "city": "Bengaluru",
  "state": "Karnataka",
  "pincode": "560001",
  "latitude": 12.9716,
  "longitude": 77.5946,
  "isDefault": true
}
```

#### Subcollection: `users/{userId}/wishlist/{productId}`

```json
{
  "productId": "string",
  "addedAt": "timestamp"
}
```

---

### 2.2 `categories/{categoryId}`

Product groupings for sweets, savouries, bakery, and festive gift packs.

```json
{
  "categoryId": "string",
  "name": "Traditional Ghee Sweets",
  "description": "Authentic desi ghee sweets prepared fresh daily",
  "imageUrl": "https://storage.googleapis.com/.../ghee_sweets.jpg",
  "displayOrder": 1,
  "isActive": true,
  "createdAt": "timestamp"
}
```

---

### 2.3 `products/{productId}`

Master product definitions with pricing rules and stock configuration.

```json
{
  "productId": "string",
  "sku": "SKS-SWT-001",
  "name": "Kaju Katli Special",
  "categoryId": "ghee_sweets",
  "description": "Diamond-cut cashew fudge made with premium cashews and silver vark",
  "images": [
    "https://storage.googleapis.com/.../kaju_katli_1.jpg",
    "https://storage.googleapis.com/.../kaju_katli_2.jpg"
  ],
  "measurementType": "WEIGHT | VOLUME | PIECE | FIXED_PACK",
  "baseUnit": "g",
  "displayUnit": "kg",
  "unitPricePaise": 95000,
  "minQuantity": 250,
  "maxQuantity": 10000,
  "quantityIncrement": 250,
  "shelfLifeDays": 15,
  "storageInstructions": "Store in a cool, dry place. Keep refrigerated after opening.",
  "ingredients": [
    "Cashew nuts",
    "Sugar",
    "Cardamom",
    "Silver foil"
  ],
  "allergens": [
    "Tree nuts"
  ],
  "isActive": true,
  "isFeatured": true,
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

---

### 2.4 `inventory/{productId}`

Authoritative single-source ledger for live stock counts.

```json
{
  "productId": "string",
  "onHandStock": 25000,
  "reservedStock": 1500,
  "availableStock": 23500,
  "lowStockThreshold": 5000,
  "unit": "g",
  "lastUpdated": "timestamp"
}
```

---

### 2.5 `inventoryMovements/{movementId}`

Immutable audit log of all stock changes.

```json
{
  "movementId": "string",
  "productId": "string",
  "type": "RECEIVE | POS_SALE | ONLINE_RESERVATION | ONLINE_FULFILLMENT | ONLINE_RELEASE | MANUAL_ADJUSTMENT | WASTAGE | RETURN",
  "quantityChange": -500,
  "previousOnHand": 25000,
  "newOnHand": 24500,
  "referenceId": "ORDER-98124 | POS-1029 | PO-401",
  "operatorId": "staff_uid",
  "reason": "Counter POS Sale",
  "timestamp": "timestamp"
}
```

---

### 2.6 `orders/{orderId}`

Online customer orders.

```json
{
  "orderId": "string",
  "customerId": "user_uid",
  "orderNumber": "SKS-20261023-001",
  "fulfillmentType": "DELIVERY | PICKUP",
  "status": "PENDING_PAYMENT | CONFIRMED | PREPARING | READY_FOR_PICKUP | OUT_FOR_DELIVERY | COMPLETED | CANCELLED | REFUNDED",
  "items": [
    {
      "productId": "string",
      "productName": "Kaju Katli Special",
      "measurementType": "WEIGHT",
      "selectedQuantity": 500,
      "unit": "g",
      "unitPricePaise": 95000,
      "lineTotalPaise": 47500,
      "imageUrl": "https://..."
    }
  ],
  "deliveryAddress": {
    "recipientName": "Ramesh Kumar",
    "phoneNumber": "+919876543210",
    "addressLine1": "Plot 42, Sweet Street",
    "city": "Bengaluru",
    "pincode": "560001"
  },
  "pickupSlot": {
    "slotDate": "2026-10-24",
    "timeWindow": "16:00 - 18:00"
  },
  "pricing": {
    "subtotalPaise": 47500,
    "discountPaise": 5000,
    "couponCode": "FESTIVE10",
    "deliveryFeePaise": 4000,
    "packagingFeePaise": 1500,
    "taxPaise": 2400,
    "grandTotalPaise": 50400
  },
  "payment": {
    "paymentId": "pay_98231",
    "gatewayOrderId": "order_rcptid_812",
    "method": "UPI | CARD | NETBANKING | CASH_ON_PICKUP",
    "status": "SUCCESS | FAILED | PENDING",
    "paidAt": "timestamp"
  },
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

---

### 2.7 `posSales/{saleId}`

Physical walk-in counter sales.

```json
{
  "saleId": "string",
  "saleNumber": "POS-20261023-004",
  "cashierId": "staff_uid",
  "cashierName": "Anand S",
  "items": [
    {
      "productId": "string",
      "name": "Mysore Pak",
      "quantity": 250,
      "unit": "g",
      "unitPricePaise": 70000,
      "lineTotalPaise": 17500
    }
  ],
  "subtotalPaise": 17500,
  "discountPaise": 0,
  "totalPaise": 17500,
  "paymentMethod": "CASH | UPI_QR | CARD",
  "timestamp": "timestamp"
}
```

---

### 2.8 `expenses/{expenseId}`

Shop operating expenses for profitability reports.

```json
{
  "expenseId": "string",
  "category": "RENT | WAGES | ELECTRICITY | PACKAGING | DELIVERY | GATEWAY_FEE | REPAIRS | MISC",
  "title": "Monthly Electricity Bill",
  "amountPaise": 1850000,
  "date": "2026-10-20",
  "paidTo": "BESCOM",
  "receiptImageUrl": "https://...",
  "recordedBy": "owner_uid",
  "createdAt": "timestamp"
}
```

---

### 2.9 `suppliers/{supplierId}` and `purchases/{purchaseId}`

Raw ingredients and stock procurement records.

```json
{
  "purchaseId": "string",
  "supplierId": "sup_01",
  "supplierName": "Sri Krishna Dairy & Ghee Traders",
  "invoiceNumber": "INV-2026-891",
  "invoiceDate": "2026-10-18",
  "items": [
    {
      "productId": "string",
      "productName": "Pure Desi Cow Ghee",
      "quantityReceived": 50000,
      "unit": "ml",
      "costPerUnitPaise": 65000,
      "totalCostPaise": 3250000
    }
  ],
  "totalCostPaise": 3250000,
  "receivedBy": "staff_uid",
  "createdAt": "timestamp"
}
```

---

### 2.10 `coupons/{couponId}`

```json
{
  "couponId": "string",
  "code": "DIWALI50",
  "discountType": "PERCENTAGE | FLAT",
  "discountValue": 10,
  "maxDiscountPaise": 15000,
  "minOrderValuePaise": 50000,
  "validFrom": "2026-10-01",
  "validUntil": "2026-11-15",
  "usageLimitPerUser": 1,
  "isActive": true
}
```

---

### 2.11 `dailySummaries/{dateString}`

Authoritative pre-aggregated daily summaries generated by backend functions.

```json
{
  "date": "2026-10-23",
  "grossSalesPaise": 2450000,
  "discountsPaise": 120000,
  "netSalesPaise": 2330000,
  "refundsPaise": 0,
  "cogsPaise": 1420000,
  "grossProfitPaise": 910000,
  "operatingExpensesPaise": 250000,
  "operatingProfitPaise": 660000,
  "orderCount": 48,
  "posSaleCount": 112,
  "onlineSaleCount": 36,
  "updatedAt": "timestamp"
}
```
