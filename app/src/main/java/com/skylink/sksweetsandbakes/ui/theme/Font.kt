package com.skylink.sksweetsandbakes.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.skylink.sksweetsandbakes.R

/**
 * AppFonts
 * Centralized repository for all font families and weights used across S K Sweets & Bakes.
 */
object AppFonts {
    /**
     * Primary Brand Font Family - Poppins
     * Fully loaded across all weights and styles from res/font.
     */
    val Poppins = FontFamily(
        Font(R.font.poppins_thin, FontWeight.Thin, FontStyle.Normal),
        Font(R.font.poppins_thin_italic, FontWeight.Thin, FontStyle.Italic),
        Font(R.font.poppins_extralight, FontWeight.ExtraLight, FontStyle.Normal),
        Font(R.font.poppins_extralight_italic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(R.font.poppins_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.poppins_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.poppins_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
        Font(R.font.poppins_semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(R.font.poppins_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
        Font(R.font.poppins_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.poppins_extrabold, FontWeight.ExtraBold, FontStyle.Normal)
    )

    /**
     * Monospaced Font Family for Receipts, POS slips, OTP inputs, and Invoices.
     */
    val Monospace = FontFamily.Monospace

    /**
     * System Default Sans-Serif Font Family.
     */
    val Default = FontFamily.Default
}

/**
 * AppFontWeights
 * Quick references for typography weights.
 */
object AppFontWeights {
    val Thin = FontWeight.Thin
    val ExtraLight = FontWeight.ExtraLight
    val Light = FontWeight.Light
    val Regular = FontWeight.Normal
    val Medium = FontWeight.Medium
    val SemiBold = FontWeight.SemiBold
    val Bold = FontWeight.Bold
    val ExtraBold = FontWeight.ExtraBold
}
