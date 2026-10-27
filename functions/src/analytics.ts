import * as admin from "firebase-admin";

/**
 * Aggregates daily financial performance from source POS sales, online orders, and expenses.
 */
export async function calculateDailySummary(
  db: admin.firestore.Firestore,
  dateStr: string // YYYY-MM-DD
): Promise<void> {
  const startOfDay = new Date(`${dateStr}T00:00:00.000Z`);
  const endOfDay = new Date(`${dateStr}T23:59:59.999Z`);

  // 1. Fetch POS Sales for date
  const posSnap = await db
    .collection("posSales")
    .where("timestamp", ">=", startOfDay)
    .where("timestamp", "<=", endOfDay)
    .get();

  let posGrossPaise = 0;
  let posCount = 0;
  posSnap.forEach((doc) => {
    const data = doc.data();
    posGrossPaise += data.totalPaise || 0;
    posCount++;
  });

  // 2. Fetch Online Orders for date
  const ordersSnap = await db
    .collection("orders")
    .where("createdAt", ">=", startOfDay)
    .where("createdAt", "<=", endOfDay)
    .get();

  let onlineGrossPaise = 0;
  let onlineDiscountsPaise = 0;
  let onlineCount = 0;
  ordersSnap.forEach((doc) => {
    const data = doc.data();
    if (data.status !== "CANCELLED") {
      onlineGrossPaise += data.pricing?.subtotalPaise || 0;
      onlineDiscountsPaise += data.pricing?.discountPaise || 0;
      onlineCount++;
    }
  });

  // 3. Fetch Expenses for date
  const expensesSnap = await db
    .collection("expenses")
    .where("date", "==", dateStr)
    .get();

  let totalExpensesPaise = 0;
  expensesSnap.forEach((doc) => {
    totalExpensesPaise += doc.data().amountPaise || 0;
  });

  const totalGrossSales = posGrossPaise + onlineGrossPaise;
  const netSales = totalGrossSales - onlineDiscountsPaise;

  // Estimated COGS (~60% average for sweet ingredients and dairy)
  const estimatedCogs = Math.round(netSales * 0.60);
  const grossProfit = netSales - estimatedCogs;
  const operatingProfit = grossProfit - totalExpensesPaise;

  await db.collection("dailySummaries").doc(dateStr).set({
    date: dateStr,
    grossSalesPaise: totalGrossSales,
    discountsPaise: onlineDiscountsPaise,
    netSalesPaise: netSales,
    cogsPaise: estimatedCogs,
    grossProfitPaise: grossProfit,
    operatingExpensesPaise: totalExpensesPaise,
    operatingProfitPaise: operatingProfit,
    orderCount: onlineCount,
    posSaleCount: posCount,
    updatedAt: admin.firestore.FieldValue.serverTimestamp(),
  });
}
