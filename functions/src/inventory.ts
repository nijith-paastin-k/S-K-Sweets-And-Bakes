import * as admin from "firebase-admin";

export interface StockReservationRequest {
  productId: string;
  quantity: number;
  orderId: string;
}

/**
 * Atomically reserve stock for an online checkout.
 */
export async function reserveStockAtomic(
  db: admin.firestore.Firestore,
  reservations: StockReservationRequest[]
): Promise<boolean> {
  return db.runTransaction(async (transaction) => {
    for (const req of reservations) {
      const invRef = db.collection("inventory").doc(req.productId);
      const invSnap = await transaction.get(invRef);

      if (!invSnap.exists) {
        throw new Error(`Inventory record not found for product ${req.productId}`);
      }

      const data = invSnap.data()!;
      const onHand = data.onHandStock || 0;
      const reserved = data.reservedStock || 0;
      const available = onHand - reserved;

      if (available < req.quantity) {
        throw new Error(
          `Insufficient stock for product ${req.productId}. Requested: ${req.quantity}, Available: ${available}`
        );
      }

      // Increment reservation
      transaction.update(invRef, {
        reservedStock: reserved + req.quantity,
        availableStock: onHand - (reserved + req.quantity),
        lastUpdated: admin.firestore.FieldValue.serverTimestamp(),
      });

      // Write movement log
      const movementRef = db.collection("inventoryMovements").doc();
      transaction.set(movementRef, {
        movementId: movementRef.id,
        productId: req.productId,
        type: "ONLINE_RESERVATION",
        quantityChange: -req.quantity,
        previousOnHand: onHand,
        newOnHand: onHand,
        referenceId: req.orderId,
        operatorId: "SYSTEM",
        reason: "Online checkout stock reservation",
        timestamp: admin.firestore.FieldValue.serverTimestamp(),
      });
    }

    return true;
  });
}

/**
 * Atomically process a counter POS sale.
 */
export async function processPosSaleAtomic(
  db: admin.firestore.Firestore,
  cashierId: string,
  saleNumber: string,
  items: Array<{ productId: string; name: string; quantity: number; unitPricePaise: number; lineTotalPaise: number }>
): Promise<string> {
  return db.runTransaction(async (transaction) => {
    for (const item of items) {
      const invRef = db.collection("inventory").doc(item.productId);
      const invSnap = await transaction.get(invRef);

      if (!invSnap.exists) {
        throw new Error(`Inventory not found for ${item.productId}`);
      }

      const data = invSnap.data()!;
      const onHand = data.onHandStock || 0;
      const reserved = data.reservedStock || 0;

      if (onHand < item.quantity) {
        throw new Error(`Insufficient physical stock for ${item.name}. On Hand: ${onHand}`);
      }

      const newOnHand = onHand - item.quantity;
      transaction.update(invRef, {
        onHandStock: newOnHand,
        availableStock: newOnHand - reserved,
        lastUpdated: admin.firestore.FieldValue.serverTimestamp(),
      });

      // Write movement log
      const movementRef = db.collection("inventoryMovements").doc();
      transaction.set(movementRef, {
        movementId: movementRef.id,
        productId: item.productId,
        type: "POS_SALE",
        quantityChange: -item.quantity,
        previousOnHand: onHand,
        newOnHand: newOnHand,
        referenceId: saleNumber,
        operatorId: cashierId,
        reason: "Counter POS Sale",
        timestamp: admin.firestore.FieldValue.serverTimestamp(),
      });
    }

    const saleRef = db.collection("posSales").doc();
    transaction.set(saleRef, {
      saleId: saleRef.id,
      saleNumber,
      cashierId,
      items,
      timestamp: admin.firestore.FieldValue.serverTimestamp(),
    });

    return saleRef.id;
  });
}
