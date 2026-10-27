import * as crypto from "crypto";
import * as admin from "firebase-admin";

/**
 * Verifies gateway webhook signature.
 */
export function verifyWebhookSignature(
  rawBody: string,
  signature: string,
  webhookSecret: string
): boolean {
  const expectedSignature = crypto
    .createHmac("sha256", webhookSecret)
    .update(rawBody)
    .digest("hex");
  return crypto.timingSafeEqual(
    Buffer.from(signature, "utf-8"),
    Buffer.from(expectedSignature, "utf-8")
  );
}

/**
 * Processes successful payment webhook idempotently.
 */
export async function processPaymentSuccessWebhook(
  db: admin.firestore.Firestore,
  eventId: string,
  orderId: string,
  paymentId: string
): Promise<void> {
  const eventRef = db.collection("processedWebhooks").doc(eventId);

  await db.runTransaction(async (transaction) => {
    const eventDoc = await transaction.get(eventRef);
    if (eventDoc.exists) {
      // Already processed idempotently
      return;
    }

    const orderRef = db.collection("orders").doc(orderId);
    const orderDoc = await transaction.get(orderRef);

    if (!orderDoc.exists) {
      throw new Error(`Order ${orderId} not found`);
    }

    // Mark event processed
    transaction.set(eventRef, {
      eventId,
      orderId,
      processedAt: admin.firestore.FieldValue.serverTimestamp(),
    });

    // Update order status
    transaction.update(orderRef, {
      status: "CONFIRMED",
      "payment.status": "SUCCESS",
      "payment.paymentId": paymentId,
      "payment.paidAt": admin.firestore.FieldValue.serverTimestamp(),
      updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    });
  });
}
