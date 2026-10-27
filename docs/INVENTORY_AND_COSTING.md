# Shared Inventory & Product Costing Engine — S K Sweets & Bakes

## 1. Shared Inventory Ledger

S K Sweets operates both physical retail counter sales and digital e-commerce sales. Both channels
draw from a single, unified inventory source in Firestore.

### 1.1 Invariants

- `availableStock = onHandStock - reservedStock`
- `onHandStock >= 0`
- `reservedStock >= 0`
- `availableStock >= 0`

### 1.2 State Transitions

1. **Stock Received (Procurement)**:
    - Action: `onHandStock += receivedQuantity`
    - Movement: `RECEIVE` (references Purchase Order ID)

2. **Online Checkout Reservation**:
    - Action: `reservedStock += orderQuantity`
    - Available stock drops immediately to prevent overselling.
    - Movement: `ONLINE_RESERVATION`

3. **Online Order Payment Confirmed**:
    - Action: `onHandStock -= orderQuantity`, `reservedStock -= orderQuantity`
    - Movement: `ONLINE_FULFILLMENT`

4. **Online Order Abandonment / Timeout**:
    - Action: `reservedStock -= orderQuantity`
    - Movement: `ONLINE_RELEASE`

5. **Counter POS Sale**:
    - Action: `onHandStock -= soldQuantity`
    - Movement: `POS_SALE` (references POS receipt ID)

6. **Wastage / Expiry / Damage**:
    - Action: `onHandStock -= wasteQuantity`
    - Movement: `WASTAGE` (requires recorded reason)

---

## 2. Product Costing & Valuation

### 2.1 Valuation Method: Weighted-Average Cost (WAC)

When raw materials or finished sweets batches are received:

```
New Unit Cost = ( (Current OnHand * Current Cost) + (New Quantity * New Purchase Cost) ) / (Current OnHand + New Quantity)
```

### 2.2 Cost of Goods Sold (COGS)

```
COGS = Sum over all items sold ( Quantity Sold * Weighted Average Unit Cost )
```

### 2.3 Gross Profit Calculation

```
Gross Sales = Total revenue before discounts
Net Sales = Gross Sales - Discounts - Refunds
Gross Profit = Net Sales - COGS
```

### 2.4 Estimated Operating Profit

```
Operating Profit = Gross Profit - Total Operating Expenses (Rent, Utilities, Wages, Packaging, Logistics, Gateway Fees)
```

When purchase records or cost data are incomplete for a product, the system flags the calculation as
`ESTIMATED_COST` to ensure reporting transparency.
