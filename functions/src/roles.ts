import * as admin from "firebase-admin";

export type UserRole = "customer" | "owner" | "manager" | "cashier" | "inventory_clerk";

/**
 * Assigns role to user and sets Auth Custom Claims.
 */
export async function assignUserRole(
  auth: admin.auth.Auth,
  db: admin.firestore.Firestore,
  callerUid: string,
  targetUid: string,
  newRole: UserRole
): Promise<void> {
  // Check that caller is an owner
  const callerUser = await auth.getUser(callerUid);
  if (callerUser.customClaims?.role !== "owner") {
    // Check fallback in firestore
    const callerDoc = await db.collection("users").doc(callerUid).get();
    if (!callerDoc.exists || callerDoc.data()?.role !== "owner") {
      throw new Error("Unauthorized: Only shop owners can assign roles.");
    }
  }

  // Set Custom Claims in Firebase Auth
  await auth.setCustomUserClaims(targetUid, { role: newRole });

  // Update Firestore user document
  await db.collection("users").doc(targetUid).set(
    {
      role: newRole,
      updatedAt: admin.firestore.FieldValue.serverTimestamp(),
    },
    { merge: true }
  );
}
