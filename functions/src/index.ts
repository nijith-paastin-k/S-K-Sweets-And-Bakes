import * as admin from "firebase-admin";
import * as functions from "firebase-functions";
import { calculateLineTotalPaise, MeasurementType } from "./pricing";
import { reserveStockAtomic, processPosSaleAtomic } from "./inventory";
import { assignUserRole, UserRole } from "./roles";
import { calculateDailySummary } from "./analytics";

admin.initializeApp();
const db = admin.firestore();
const auth = admin.auth();

/**
 * Validates cart items and calculates total.
 */
export const validateCartAndCheckout = functions.https.onCall(async (data, context) => {
  const items = data.items as Array<{
    productId: string;
    measurementType: MeasurementType;
    quantity: number;
  }>;

  if (!items || items.length === 0) {
    throw new functions.https.HttpsError("invalid-argument", "Cart cannot be empty");
  }

  let subtotalPaise = 0;
  const validatedItems = [];

  for (const item of items) {
    const prodDoc = await db.collection("products").doc(item.productId).get();
    if (!prodDoc.exists) {
      throw new functions.https.HttpsError("not-found", `Product ${item.productId} not found`);
    }

    const prodData = prodDoc.data()!;
    const lineTotal = calculateLineTotalPaise(
      item.measurementType,
      prodData.unitPricePaise,
      item.quantity
    );

    subtotalPaise += lineTotal;
    validatedItems.push({
      productId: item.productId,
      productName: prodData.name,
      measurementType: item.measurementType,
      quantity: item.quantity,
      unitPricePaise: prodData.unitPricePaise,
      lineTotalPaise: lineTotal,
    });
  }

  const deliveryFeePaise = data.fulfillmentType === "DELIVERY" ? 4000 : 0;
  const packagingFeePaise = 1500;
  const taxPaise = Math.round(subtotalPaise * 0.05); // 5% GST for sweets
  const grandTotalPaise = subtotalPaise + deliveryFeePaise + packagingFeePaise + taxPaise;

  return {
    items: validatedItems,
    subtotalPaise,
    deliveryFeePaise,
    packagingFeePaise,
    taxPaise,
    grandTotalPaise,
  };
});

/**
 * Atomically reserve stock for checkout.
 */
export const reserveStock = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "User must be authenticated");
  }

  const reservations = data.reservations;
  await reserveStockAtomic(db, reservations);
  return { success: true };
});

/**
 * Privileged role assignment.
 */
export const setStaffRole = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "Authentication required");
  }

  const targetUid = data.targetUid as string;
  const newRole = data.role as UserRole;

  await assignUserRole(auth, db, context.auth.uid, targetUid, newRole);
  return { success: true, targetUid, newRole };
});

/**
 * Atomic POS Sale for Cashiers.
 */
export const recordPosSale = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "Authentication required");
  }

  const saleNumber = data.saleNumber as string;
  const items = data.items;

  const saleId = await processPosSaleAtomic(db, context.auth.uid, saleNumber, items);
  return { success: true, saleId };
});

/**
 * Scheduled or manual daily financial summary trigger.
 */
export const generateDailySummary = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "Authentication required");
  }

  const dateStr = data.date as string; // YYYY-MM-DD
  await calculateDailySummary(db, dateStr);
  return { success: true, date: dateStr };
});
