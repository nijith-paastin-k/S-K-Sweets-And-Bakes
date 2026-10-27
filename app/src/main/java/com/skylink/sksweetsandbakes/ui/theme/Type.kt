package com.skylink.sksweetsandbakes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * Backward compatibility alias for PoppinsFontFamily
 */
val PoppinsFontFamily: FontFamily = AppFonts.Poppins

/**
 * Material 3 Typography Scale
 * Systematically built using AppFonts, AppFontSizes, and AppFontWeights.
 */
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Bold,
        fontSize = AppFontSizes.displayLarge,
        lineHeight = AppFontSizes.lineHeightDisplayLarge,
        letterSpacing = AppFontSizes.letterSpacingTight
    ),
    displayMedium = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Bold,
        fontSize = AppFontSizes.displayMedium,
        lineHeight = AppFontSizes.lineHeightDisplayMedium,
        letterSpacing = AppFontSizes.letterSpacingNone
    ),
    displaySmall = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.SemiBold,
        fontSize = AppFontSizes.displaySmall,
        lineHeight = AppFontSizes.lineHeightDisplaySmall,
        letterSpacing = AppFontSizes.letterSpacingNone
    ),
    headlineLarge = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Bold,
        fontSize = AppFontSizes.headlineLarge,
        lineHeight = AppFontSizes.lineHeightHeadlineLarge,
        letterSpacing = AppFontSizes.letterSpacingNone
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.SemiBold,
        fontSize = AppFontSizes.headlineMedium,
        lineHeight = AppFontSizes.lineHeightHeadlineMedium,
        letterSpacing = AppFontSizes.letterSpacingNone
    ),
    headlineSmall = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.SemiBold,
        fontSize = AppFontSizes.headlineSmall,
        lineHeight = AppFontSizes.lineHeightHeadlineSmall,
        letterSpacing = AppFontSizes.letterSpacingNone
    ),
    titleLarge = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.SemiBold,
        fontSize = AppFontSizes.titleLarge,
        lineHeight = AppFontSizes.lineHeightTitleLarge,
        letterSpacing = AppFontSizes.letterSpacingNone
    ),
    titleMedium = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Medium,
        fontSize = AppFontSizes.titleMedium,
        lineHeight = AppFontSizes.lineHeightTitleMedium,
        letterSpacing = AppFontSizes.letterSpacingNormal
    ),
    titleSmall = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Medium,
        fontSize = AppFontSizes.titleSmall,
        lineHeight = AppFontSizes.lineHeightTitleSmall,
        letterSpacing = AppFontSizes.letterSpacingSnug
    ),
    bodyLarge = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Regular,
        fontSize = AppFontSizes.bodyLarge,
        lineHeight = AppFontSizes.lineHeightBodyLarge,
        letterSpacing = AppFontSizes.letterSpacingExtraLoose
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Regular,
        fontSize = AppFontSizes.bodyMedium,
        lineHeight = AppFontSizes.lineHeightBodyMedium,
        letterSpacing = AppFontSizes.letterSpacingWide
    ),
    bodySmall = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Regular,
        fontSize = AppFontSizes.bodySmall,
        lineHeight = AppFontSizes.lineHeightBodySmall,
        letterSpacing = AppFontSizes.letterSpacingLoose
    ),
    labelLarge = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Medium,
        fontSize = AppFontSizes.labelLarge,
        lineHeight = AppFontSizes.lineHeightLabelLarge,
        letterSpacing = AppFontSizes.letterSpacingSnug
    ),
    labelMedium = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Medium,
        fontSize = AppFontSizes.labelMedium,
        lineHeight = AppFontSizes.lineHeightLabelMedium,
        letterSpacing = AppFontSizes.letterSpacingExtraLoose
    ),
    labelSmall = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = AppFontWeights.Medium,
        fontSize = AppFontSizes.labelSmall,
        lineHeight = AppFontSizes.lineHeightLabelSmall,
        letterSpacing = AppFontSizes.letterSpacingExtraLoose
    )
)

/**
 * AppCustomStyles
 * Domain-specific text styles for e-commerce, POS receipts, and badges.
 */
object AppCustomStyles {
    val receiptCode = TextStyle(
        fontFamily = AppFonts.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = AppFontSizes.subtext,
        letterSpacing = AppFontSizes.letterSpacingSnug
    )

    val receiptBold = TextStyle(
        fontFamily = AppFonts.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = AppFontSizes.regular,
        letterSpacing = AppFontSizes.letterSpacingSnug
    )

    val productPrice = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = AppFontSizes.price,
        lineHeight = AppFontSizes.lineHeightTitleLarge
    )

    val badgeText = TextStyle(
        fontFamily = AppFonts.Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = AppFontSizes.badge,
        lineHeight = AppFontSizes.lineHeightMicro
    )
}
