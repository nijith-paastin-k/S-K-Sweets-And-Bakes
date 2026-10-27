# Test Plan & Verification Strategy — S K Sweets & Bakes

## 1. Test Levels

1. **Unit Testing**:
    - Pricing Engine: Comprehensive tests covering all weight steps (250g, 500g, 750g, 1kg, 1.25kg,
      1.5kg, 1.75kg, 2kg, 2.25kg, 2.5kg, custom), volume conversions, piece counts, pack counts, and
      integer paise arithmetic.
    - Domain Use Cases: Cart calculation, inventory reservation calculations, order state
      transitions.
    - Financial Calculations: Gross sales, COGS, gross margin, operating profit.
2. **Integration Testing**:
    - Shared inventory atomic updates: Simultaneous POS sale and online reservation.
    - Payment webhook processing and idempotency.
    - Repository interactions with mocked Firebase data sources.
3. **UI / Compose Tests**:
    - Key screen rendering (Catalog, Product Details, Quantity Picker, Cart, Checkout, POS).
    - Validation states: invalid phone number, insufficient quantity, empty cart.

---

## 2. Test Cases Matrix

| Test ID   | Area      | Scenario                                      | Expected Outcome                             |
|-----------|-----------|-----------------------------------------------|----------------------------------------------|
| TC-PRC-01 | Pricing   | 250g of ₹800/kg sweet                         | Returns 20,000 paise (₹200.00) exactly       |
| TC-PRC-02 | Pricing   | 1.25kg of ₹600/kg sweet                       | Returns 75,000 paise (₹750.00) exactly       |
| TC-PRC-03 | Pricing   | Piece-based: 6 samosas at ₹25 each            | Returns 15,000 paise (₹150.00)               |
| TC-PRC-04 | Pricing   | Fixed pack: 2 festive gift boxes at ₹999 each | Returns 199,800 paise (₹1,998.00)            |
| TC-PRC-05 | Pricing   | Volume: 500ml badam milk at ₹160/litre        | Returns 8,000 paise (₹80.00)                 |
| TC-INV-01 | Inventory | POS counter sale of 500g                      | Decrements on-hand by 500g, logs POS_SALE    |
| TC-INV-02 | Inventory | Online reservation of 1000g                   | Increases reserved by 1000g, available drops |
| TC-INV-03 | Inventory | Payment confirmed                             | Decrements on-hand and reserved by 1000g     |
| TC-INV-04 | Inventory | Online order cancelled                        | Releases reserved by 1000g                   |
| TC-ORD-01 | Order     | State transition CONFIRMED -> PREPARING       | Valid transition allowed                     |
| TC-ORD-02 | Order     | State transition DELIVERED -> CANCELLED       | Invalid transition rejected with exception   |
| TC-SEC-01 | Security  | Customer attempts role change to owner        | Blocked by Firestore security rules          |
| TC-SEC-02 | Security  | Cashier attempts reading expenses             | Blocked by Firestore security rules          |
