package com.skylink.sksweetsandbakes.core.util

import java.text.DecimalFormat
import java.util.Locale

/**
 * Currency utility guaranteeing decimal-safe arithmetic and formatting.
 * 1 INR = 100 paise. All domain and database amounts are strictly integer paise.
 */
object CurrencyFormatter {

    private val indianLocale = Locale("en", "IN")
    private val decimalFormat = DecimalFormat("##,##,##0.00")
    private val wholeFormat = DecimalFormat("##,##,##0")

    /**
     * Formats integer paise into a formatted rupee string (e.g. 95000 -> "₹950.00").
     */
    fun formatPaise(paise: Long, includeDecimals: Boolean = true): String {
        val rupees = paise / 100.0
        val formattedNumber = if (includeDecimals || paise % 100 != 0L) {
            decimalFormat.format(rupees)
        } else {
            wholeFormat.format(rupees)
        }
        return "₹$formattedNumber"
    }

    /**
     * Overload for Int paise.
     */
    fun formatPaise(paise: Int, includeDecimals: Boolean = true): String {
        return formatPaise(paise.toLong(), includeDecimals)
    }

    /**
     * Converts rupees (e.g. 950.50) into integer paise (95050).
     */
    fun rupeesToPaise(rupees: Double): Long {
        return Math.round(rupees * 100.0)
    }

    /**
     * Converts paise into decimal rupees for display calculations.
     */
    fun paiseToRupees(paise: Long): Double {
        return paise / 100.0
    }
}
