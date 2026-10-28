package com.skylink.sksweetsandbakes.data.orders.repository

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.DateUtils
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.repository.InvoiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class InvoiceRepositoryImpl(
    context: Context
) : InvoiceRepository {
    private val context: Context = context.applicationContext

    override suspend fun generateInvoicePdf(order: Order): Result<File> =
        withContext(Dispatchers.IO) {
            try {
                val invoiceDir = File(context.cacheDir, "invoices").apply {
                    if (!exists()) mkdirs()
                }
                val fileName = "Invoice_${order.orderNumber.ifEmpty { order.orderId }}.pdf"
                val file = File(invoiceDir, fileName)

                val document = PdfDocument()
                try {
                    val pageWidth = 595
                val pageHeight = 842
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
                val page = document.startPage(pageInfo)
                val canvas: Canvas = page.canvas

                val paint = Paint()
                val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

                paint.color = Color.rgb(107, 29, 47)
                paint.style = Paint.Style.FILL
                canvas.drawRect(0f, 0f, pageWidth.toFloat(), 110f, paint)

                textPaint.color = Color.WHITE
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textPaint.textSize = 24f
                canvas.drawText("S K SWEETS & BAKES", 36f, 50f, textPaint)

                textPaint.textSize = 10f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(
                    "Authentic Indian Sweets, Savories & Premium Bakes",
                    36f,
                    72f,
                    textPaint
                )
                canvas.drawText(
                    "GSTIN: 29AAAAA0000A1Z5 | Phone: +91 98765 43210 | Bengaluru, KA",
                    36f,
                    90f,
                    textPaint
                )

                textPaint.textSize = 14f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("TAX INVOICE", 450f, 60f, textPaint)

                var y = 145f

                paint.color = Color.rgb(245, 245, 247)
                canvas.drawRoundRect(
                    36f,
                    y - 20f,
                    (pageWidth - 36).toFloat(),
                    y + 80f,
                    10f,
                    10f,
                    paint
                )

                textPaint.color = Color.rgb(40, 40, 40)
                textPaint.textSize = 10f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("INVOICE DETAILS", 48f, y, textPaint)
                canvas.drawText("CUSTOMER DETAILS", 320f, y, textPaint)

                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                y += 18f
                canvas.drawText("Invoice No: INV-${order.orderNumber}", 48f, y, textPaint)
                canvas.drawText(
                    "Customer: ${order.customerName.ifEmpty { "Valued Customer" }}",
                    320f,
                    y,
                    textPaint
                )

                y += 16f
                canvas.drawText("Order ID: #${order.orderNumber}", 48f, y, textPaint)
                canvas.drawText(
                    "Phone: ${order.customerPhone.ifEmpty { "N/A" }}",
                    320f,
                    y,
                    textPaint
                )

                y += 16f
                canvas.drawText(
                    "Date: ${DateUtils.formatDateTime(order.createdAt)}",
                    48f,
                    y,
                    textPaint
                )
                val addressText = order.deliveryAddress?.let { "${it.addressLine1}, ${it.city}" }
                    ?: "In-Store Pickup"
                canvas.drawText(
                    "Fulfillment: ${order.fulfillmentType.name} (${addressText})",
                    320f,
                    y,
                    textPaint
                )

                y += 16f
                canvas.drawText(
                    "Payment: ${order.payment.method.name} [${order.payment.status}]",
                    48f,
                    y,
                    textPaint
                )
                canvas.drawText(
                    "Payment ID: ${order.payment.paymentId.ifEmpty { "PAID" }}",
                    320f,
                    y,
                    textPaint
                )

                y += 45f
                paint.color = Color.rgb(107, 29, 47)
                canvas.drawRect(36f, y, (pageWidth - 36).toFloat(), y + 26f, paint)

                textPaint.color = Color.WHITE
                textPaint.textSize = 10f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("#", 46f, y + 17f, textPaint)
                canvas.drawText("ITEM DESCRIPTION", 75f, y + 17f, textPaint)
                canvas.drawText("QTY", 330f, y + 17f, textPaint)
                canvas.drawText("UNIT PRICE", 400f, y + 17f, textPaint)
                canvas.drawText("TOTAL", 500f, y + 17f, textPaint)

                y += 26f
                textPaint.color = Color.rgb(30, 30, 30)

                order.items.forEachIndexed { index, item ->
                    y += 22f

                    if (index % 2 == 1) {
                        paint.color = Color.rgb(250, 250, 250)
                        canvas.drawRect(36f, y - 15f, (pageWidth - 36).toFloat(), y + 7f, paint)
                    }

                    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                    canvas.drawText("${index + 1}", 46f, y, textPaint)
                    canvas.drawText(
                        "${item.productName} (${item.selectedQuantity} ${item.baseUnit})",
                        75f,
                        y,
                        textPaint
                    )
                    canvas.drawText("${item.selectedQuantity}", 335f, y, textPaint)
                    canvas.drawText(
                        CurrencyFormatter.formatPaise(item.unitPricePaise),
                        400f,
                        y,
                        textPaint
                    )
                    canvas.drawText(
                        CurrencyFormatter.formatPaise(item.lineTotalPaise),
                        500f,
                        y,
                        textPaint
                    )
                }

                y += 25f
                paint.color = Color.rgb(220, 220, 220)
                paint.strokeWidth = 1f
                canvas.drawLine(36f, y, (pageWidth - 36).toFloat(), y, paint)

                y += 20f
                textPaint.textSize = 10f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

                fun drawSummaryRow(label: String, value: String, isBold: Boolean = false) {
                    textPaint.typeface = Typeface.create(
                        Typeface.DEFAULT,
                        if (isBold) Typeface.BOLD else Typeface.NORMAL
                    )
                    canvas.drawText(label, 360f, y, textPaint)
                    canvas.drawText(value, 500f, y, textPaint)
                    y += 18f
                }

                drawSummaryRow(
                    "Items Subtotal:",
                    CurrencyFormatter.formatPaise(order.pricing.subtotalPaise)
                )
                if (order.pricing.discountPaise > 0) {
                    drawSummaryRow(
                        "Discount (${order.pricing.couponCode ?: "Promo"}):",
                        "-${CurrencyFormatter.formatPaise(order.pricing.discountPaise)}"
                    )
                }
                drawSummaryRow(
                    "Packaging Fee:",
                    CurrencyFormatter.formatPaise(order.pricing.packagingFeePaise)
                )
                drawSummaryRow(
                    "Delivery Fee:",
                    CurrencyFormatter.formatPaise(order.pricing.deliveryFeePaise)
                )
                drawSummaryRow("GST (5%):", CurrencyFormatter.formatPaise(order.pricing.taxPaise))

                paint.color = Color.rgb(107, 29, 47)
                paint.strokeWidth = 1.5f
                canvas.drawLine(360f, y, (pageWidth - 36).toFloat(), y, paint)
                y += 18f

                textPaint.textSize = 12f
                textPaint.color = Color.rgb(107, 29, 47)
                drawSummaryRow(
                    "GRAND TOTAL:",
                    CurrencyFormatter.formatPaise(order.pricing.grandTotalPaise),
                    isBold = true
                )

                paint.color = Color.rgb(46, 125, 50)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 2f
                canvas.drawRoundRect(48f, y - 60f, 180f, y - 20f, 8f, 8f, paint)

                val stampPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                stampPaint.color = Color.rgb(46, 125, 50)
                stampPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                stampPaint.textSize = 14f
                canvas.drawText("PAID & VERIFIED", 58f, y - 35f, stampPaint)

                y = (pageHeight - 60).toFloat()
                paint.color = Color.rgb(240, 240, 240)
                paint.style = Paint.Style.FILL
                canvas.drawRect(36f, y, (pageWidth - 36).toFloat(), y + 36f, paint)

                textPaint.color = Color.rgb(120, 120, 120)
                textPaint.textSize = 9f
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(
                    "Thank you for choosing S K Sweets & Bakes! For support or inquiries, contact help@sksweets.com",
                    60f,
                    y + 16f,
                    textPaint
                )
                canvas.drawText(
                    "This is an electronically generated tax invoice. No physical signature is required.",
                    100f,
                    y + 28f,
                    textPaint
                )

                document.finishPage(page)

                    FileOutputStream(file).use { out ->
                        document.writeTo(out)
                    }
                    Result.Success(file)
                } finally {
                    try {
                        document.close()
                    } catch (_: Exception) {
                    }
                }
            } catch (e: Exception) {
                Result.Error(e, e.message ?: "Failed to generate PDF invoice")
            }
        }
}
