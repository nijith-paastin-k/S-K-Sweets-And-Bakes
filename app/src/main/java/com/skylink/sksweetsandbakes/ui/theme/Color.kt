package com.skylink.sksweetsandbakes.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * AppColors
 * Centralized Single Source of Truth for all color tokens and palettes used in S K Sweets & Bakes.
 * Every screen, component, and feature consumes colors directly from here.
 */
object AppColors {
    val MaroonPrimary = Color(0xFF5E1119)
    val MaroonDark = Color(0xFF3F0A10)
    val MaroonLight = Color(0xFF7E1B28)
    val MaroonContainer = Color(0xFFF5DEE1)
    val MaroonSurface = Color(0xFFFBF1F2)

    val GoldSecondary = Color(0xFFC69326)
    val GoldDark = Color(0xFF8C6412)
    val GoldLight = Color(0xFFE8BC52)
    val GoldContainer = Color(0xFFF8E7BE)
    val GoldSurface = Color(0xFFFDF7E7)
    val GoldAccent = Color(0xFFD4AF37)
    val GoldDarkContainer = Color(0xFF332005)

    val AmberTertiary = Color(0xFFA86F15)
    val AmberLight = Color(0xFFF4D489)
    val AmberDark = Color(0xFF6B4508)
    val CreamBackground = Color(0xFFF7F3E7)
    val WarmSurface = Color(0xFFFCFAF2)
    val CardSurface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFEFE8D6)
    val Outline = Color(0xFF857371)
    val OutlineVariant = Color(0xFFD7C1C3)
    val DarkBackground = Color(0xFF140406)
    val DarkSurface = Color(0xFF1F080C)
    val DarkSurfaceVariant = Color(0xFF2C0D13)
    val DarkOutline = Color(0xFFA08C8E)
    val DarkOutlineVariant = Color(0xFF524345)
    val TextPrimaryLight = Color(0xFF2E070D)
    val TextSecondaryLight = Color(0xFF6B4B38)
    val TextTertiaryLight = Color(0xFF8F7B70)

    val TextPrimaryDark = Color(0xFFFAF4EA)
    val TextSecondaryDark = Color(0xFFD4C2B4)
    val TextTertiaryDark = Color(0xFFA8988D)
    val ProfitGreen = Color(0xFF276749)
    val ProfitGreenContainer = Color(0xFFE8F5E9)
    val ProfitGreenText = Color(0xFF1B5E20)

    val ExpenseRed = Color(0xFFC53030)
    val ExpenseRedContainer = Color(0xFFFFF5F5)
    val ExpenseAlertRed = Color(0xFFE53E3E)
    val ExpenseAlertBorder = Color(0xFFFEB2B2)
    val ExpenseAlertDark = Color(0xFF742A2A)

    val CostOrange = Color(0xFFDD6B20)
    val CostOrangeContainer = Color(0xFFFFF9C4)

    val RevenueBlue = Color(0xFF3182CE)
    val RevenueBlueContainer = Color(0xFFE8EAF6)
    val StatusSuccess = Color(0xFF276749)
    val StatusSuccessContainer = Color(0xFFE8F5E9)
    val StatusSuccessAlt = Color(0xFF2E7D32)
    val StatusSuccessLight = Color(0xFF81C784)
    val StatusSuccessGlow = Color(0xFF4CAF50)

    val StatusWarning = Color(0xFFD69E2E)
    val StatusWarningContainer = Color(0xFFFFF3CD)
    val StatusWarningAlt = Color(0xFFC69326)

    val StatusError = Color(0xFFC53030)
    val StatusErrorContainer = Color(0xFFF8D7DA)
    val StatusErrorAlt = Color(0xFFD32F2F)

    val StatusInfo = Color(0xFF3182CE)
    val StatusInfoContainer = Color(0xFFD1ECF1)
    val StatusInfoAlt = Color(0xFF1A73E8)

    val FavoriteRed = Color(0xFFD32F2F)
    val OrderPlaced = Color(0xFFD69E2E)
    val OrderConfirmed = Color(0xFF3182CE)
    val OrderPreparing = Color(0xFFDD6B20)
    val OrderDispatched = Color(0xFF805AD5)
    val OrderDelivered = Color(0xFF276749)
    val OrderCancelled = Color(0xFFC53030)
    val PasswordStrengthWeak = Color(0xFFE53E3E)
    val PasswordStrengthFair = Color(0xFFDD6B20)
    val PasswordStrengthGood = Color(0xFF3182CE)
    val PasswordStrengthStrong = Color(0xFF38A169)
    val ActionPos = Color(0xFF2F855A)
    val ActionInventory = Color(0xFFC05621)
    val ActionExpenses = Color(0xFFC53030)
    val ActionAnalytics = Color(0xFF3182CE)
    val ActionSuppliers = Color(0xFF805AD5)
    val ActionStaff = Color(0xFFD69E2E)
    val BankSbi = Color(0xFF0083CA)
    val BankHdfc = Color(0xFF004C8F)
    val BankIcici = Color(0xFFB3282D)
    val BankAxis = Color(0xFF97144D)

    val PaymentUpi = Color(0xFF002970)
    val PaymentPaytm = Color(0xFF00B9F5)
    val PaymentPhonePe = Color(0xFF5F259F)
    val PaymentGooglePay = Color(0xFF1A73E8)
    val PaymentCardDark = Color(0xFF0C2340)
    val PaymentCardBlue = Color(0xFF3395FF)
    val PaymentCodDark = Color(0xFF111111)
    val GoogleBlue = Color(0xFF1A73E8)
    val GoogleGreen = Color(0xFF1E8E3E)
    val GoogleRed = Color(0xFFD93025)
    val GoogleAmber = Color(0xFFE37400)
    val GooglePurple = Color(0xFF8E24AA)
    val GoogleTeal = Color(0xFF00897B)

    val FacebookBlue = Color(0xFF1877F2)
    val FacebookBlueDark = Color(0xFF4599FF)

    val MicrosoftRed = Color(0xFFF25022)
    val MicrosoftGreen = Color(0xFF7FBA00)
    val MicrosoftBlue = Color(0xFF00A4EF)
    val MicrosoftYellow = Color(0xFFFFB900)

    val AppleDark = Color(0xFF111111)
    val XDark = Color(0xFF000000)
    val MorningGradient = listOf(Color(0xFFE65100), Color(0xFFFF7043), Color(0xFFFFB74D))
    val MorningSun = Color(0xFFFFEB3B)
    val MorningAura = Color(0x66FFF176)

    val AfternoonGradient = listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF42A5F5))
    val AfternoonSun = Color(0xFFFFEE58)
    val AfternoonAura = Color(0x77FFF59D)

    val EveningGradient = listOf(Color(0xFF311B92), Color(0xFF6A1B9A), Color(0xFFD84315))
    val EveningSun = Color(0xFFFF7043)
    val EveningAura = Color(0x66FF8A65)

    val NightGradient = listOf(Color(0xFF0A1128), Color(0xFF1C2541), Color(0xFF3A506B))
    val NightMoon = Color(0xFFFFF9C4)
    val NightAura = Color(0x55FFFDE7)

    val MascotLadoo = Color(0xFFFFB300)
    val MascotCheek = Color(0xFFFF7043)
    val MascotShadow = Color(0x28000000)
    val MascotHighlight = Color(0xFFFFD54F)
    val MascotShade = Color(0xFFE65100)
    val MascotPistachio = Color(0xFF2E7D32)
    val MascotSaffron = Color(0xFFC62828)
    val MascotDarkCharcoal = Color(0xFF212121)
    val MascotSunglasses = Color(0xFF1A1A1A)
    val MascotGlareWhite = Color(0x66FFFFFF)
    val MascotBowtie = Color(0xFFD32F2F)
    val MascotBowtieKnot = Color(0xFFFFCDD2)
    val MascotNightcap = Color(0xFF5C6BC0)
    val MascotNightcapPompom = Color(0xFFE8EAF6)
    val MascotMoonStar = Color(0xFFFFF9C4)
    val MascotHillShadow = Color(0x33000000)
    val BorderSubtle = Color(0xFFDDDDDD)
    val BorderLight = Color(0xFFE2E8F0)
    val BorderMedium = Color(0xFFDADCE0)
    val BorderDark = Color(0xFF3E4042)
    val ShimmerBase = Color(0xFFE0D8CB)
    val ShimmerHighlight = Color(0xFFF5EFE6)
    val DividerLight = Color(0x1F2E070D)
    val DividerDark = Color(0x1FFAF4EA)
    val DialogDarkSurface = Color(0xFF28292A)
    val DialogDarkCardBg = Color(0xFF202122)
    val DialogDarkBorder = Color(0xFF444746)
    val DialogDarkTextPrimary = Color(0xFFE3E3E3)
    val DialogDarkTextSecondary = Color(0xFFC4C7C5)
    val DialogDarkDivider = Color(0xFF444746)
    val DialogDarkLink = Color(0xFF8AB4F8)
    val DialogDarkIconTint = Color(0xFF3C4043)
    val DialogFbDarkSurface = Color(0xFF242526)
    val DialogFbDarkCardBg = Color(0xFF18191A)
    val DialogFbDarkTextPrimary = Color(0xFFE4E6EB)
    val DialogFbDarkTextSecondary = Color(0xFFB0B3B8)
    val DialogLightDivider = Color(0xFFE8EAED)
    val DialogLightTextPrimary = Color(0xFF202124)
    val DialogLightTextSecondary = Color(0xFF5F6368)
    val PrimaryMaroonGradient = Brush.verticalGradient(
        colors = listOf(MaroonLight, MaroonPrimary, MaroonDark)
    )

    val RoyalGoldGradient = Brush.horizontalGradient(
        colors = listOf(GoldDark, GoldSecondary, GoldLight, GoldSecondary)
    )

    val CardGoldMaroonGradient = Brush.linearGradient(
        colors = listOf(MaroonPrimary, MaroonDark)
    )

    val WarmCreamGradient = Brush.verticalGradient(
        colors = listOf(WarmSurface, CreamBackground)
    )
}

val LogoMaroonPrimary = AppColors.MaroonPrimary
val LogoMaroonDark = AppColors.MaroonDark
val LogoMaroonLight = AppColors.MaroonLight
val LogoMaroonContainer = AppColors.MaroonContainer

val LogoGoldSecondary = AppColors.GoldSecondary
val LogoGoldDark = AppColors.GoldDark
val LogoGoldLight = AppColors.GoldLight
val LogoGoldContainer = AppColors.GoldContainer

val LogoAmberTertiary = AppColors.AmberTertiary
val LogoAmberLight = AppColors.AmberLight

val LogoCreamBackground = AppColors.CreamBackground
val LogoWarmSurface = AppColors.WarmSurface
val LogoCardSurface = AppColors.CardSurface

val DarkBackground = AppColors.DarkBackground
val DarkSurface = AppColors.DarkSurface
val DarkSurfaceVariant = AppColors.DarkSurfaceVariant

val TextPrimaryLight = AppColors.TextPrimaryLight
val TextSecondaryLight = AppColors.TextSecondaryLight
val TextPrimaryDark = AppColors.TextPrimaryDark
val TextSecondaryDark = AppColors.TextSecondaryDark

val StatusSuccess = AppColors.StatusSuccess
val StatusWarning = AppColors.StatusWarning
val StatusError = AppColors.StatusError
val StatusInfo = AppColors.StatusInfo
