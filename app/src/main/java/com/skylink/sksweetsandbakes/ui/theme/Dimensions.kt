package com.skylink.sksweetsandbakes.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * AppDimensions
 * Centralized Single Source of Truth for all layout spacing, paddings, corner radii,
 * elevations, icon sizes, and component dimensions across S K Sweets & Bakes.
 */
@Immutable
data class AppDimensions(
    val dp0: Dp = 0.dp,
    val dp1: Dp = 1.dp,
    val dp1_2: Dp = 1.2.dp,
    val dp1_5: Dp = 1.5.dp,
    val dp2: Dp = 2.dp,
    val dp2_5: Dp = 2.5.dp,
    val dp3: Dp = 3.dp,
    val dp4: Dp = 4.dp,
    val dp5: Dp = 5.dp,
    val dp6: Dp = 6.dp,
    val dp7: Dp = 7.dp,
    val dp8: Dp = 8.dp,
    val dp10: Dp = 10.dp,
    val dp12: Dp = 12.dp,
    val dp13: Dp = 13.dp,
    val dp14: Dp = 14.dp,
    val dp15: Dp = 15.dp,
    val dp16: Dp = 16.dp,
    val dp18: Dp = 18.dp,
    val dp20: Dp = 20.dp,
    val dp22: Dp = 22.dp,
    val dp24: Dp = 24.dp,
    val dp26: Dp = 26.dp,
    val dp28: Dp = 28.dp,
    val dp30: Dp = 30.dp,
    val dp32: Dp = 32.dp,
    val dp34: Dp = 34.dp,
    val dp36: Dp = 36.dp,
    val dp38: Dp = 38.dp,
    val dp40: Dp = 40.dp,
    val dp42: Dp = 42.dp,
    val dp44: Dp = 44.dp,
    val dp46: Dp = 46.dp,
    val dp48: Dp = 48.dp,
    val dp50: Dp = 50.dp,
    val dp52: Dp = 52.dp,
    val dp54: Dp = 54.dp,
    val dp56: Dp = 56.dp,
    val dp60: Dp = 60.dp,
    val dp64: Dp = 64.dp,
    val dp70: Dp = 70.dp,
    val dp72: Dp = 72.dp,
    val dp76: Dp = 76.dp,
    val dp80: Dp = 80.dp,
    val dp84: Dp = 84.dp,
    val dp90: Dp = 90.dp,
    val dp95: Dp = 95.dp,
    val dp96: Dp = 96.dp,
    val dp100: Dp = 100.dp,
    val dp105: Dp = 105.dp,
    val dp110: Dp = 110.dp,
    val dp120: Dp = 120.dp,
    val dp130: Dp = 130.dp,
    val dp140: Dp = 140.dp,
    val dp150: Dp = 150.dp,
    val dp160: Dp = 160.dp,
    val dp170: Dp = 170.dp,
    val dp175: Dp = 175.dp,
    val dp180: Dp = 180.dp,
    val dp200: Dp = 200.dp,
    val dp220: Dp = 220.dp,
    val dp240: Dp = 240.dp,
    val dp260: Dp = 260.dp,
    val dp280: Dp = 280.dp,
    val dp300: Dp = 300.dp,
    val dp310: Dp = 310.dp,
    val dp999: Dp = 999.dp,

    val spacingNone: Dp = 0.dp,
    val spacingXxs: Dp = 2.dp,
    val spacingXs: Dp = 4.dp,
    val spacingS: Dp = 6.dp,
    val spacingSm: Dp = 8.dp,
    val spacing10: Dp = 10.dp,
    val spacingM: Dp = 12.dp,
    val spacing14: Dp = 14.dp,
    val spacingMd: Dp = 16.dp,
    val spacingL: Dp = 20.dp,
    val spacingXl: Dp = 24.dp,
    val spacingXxl: Dp = 32.dp,
    val spacingXxxl: Dp = 40.dp,
    val spacingHuge: Dp = 48.dp,
    val spacingMassive: Dp = 64.dp,

    val radiusNone: Dp = 0.dp,
    val radiusXs: Dp = 4.dp,
    val radius6: Dp = 6.dp,
    val radiusSmall: Dp = 8.dp,
    val radius10: Dp = 10.dp,
    val radiusMedium: Dp = 12.dp,
    val radius14: Dp = 14.dp,
    val radiusLarge: Dp = 16.dp,
    val radius20: Dp = 20.dp,
    val radiusXl: Dp = 24.dp,
    val radiusPill: Dp = 50.dp,
    val radiusRound: Dp = 999.dp,

    val iconMicro: Dp = 12.dp,
    val iconSmall: Dp = 16.dp,
    val icon18: Dp = 18.dp,
    val iconMedium: Dp = 20.dp,
    val iconStandard: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val iconXl: Dp = 48.dp,
    val iconHero: Dp = 64.dp,

    val avatarSmall: Dp = 32.dp,
    val avatarMedium: Dp = 44.dp,
    val avatarLarge: Dp = 64.dp,
    val avatarXl: Dp = 96.dp,

    val buttonHeightSmall: Dp = 36.dp,
    val buttonHeightMedium: Dp = 48.dp,
    val buttonHeightLarge: Dp = 56.dp,
    val inputFieldHeight: Dp = 56.dp,
    val searchBarHeight: Dp = 52.dp,
    val chipHeight: Dp = 32.dp,
    val badgeSize: Dp = 18.dp,
    val topAppBarHeight: Dp = 64.dp,
    val bottomNavHeight: Dp = 72.dp,
    val cardMinHeight: Dp = 80.dp,
    val productCardWidth: Dp = 160.dp,
    val productCardHeight: Dp = 220.dp,
    val bannerHeight: Dp = 180.dp,
    val logoSize: Dp = 120.dp,
    val logoSizeSmall: Dp = 40.dp,

    val elevationNone: Dp = 0.dp,
    val elevationLow: Dp = 2.dp,
    val elevationMedium: Dp = 4.dp,
    val elevationHigh: Dp = 8.dp,
    val elevationModal: Dp = 16.dp,

    val borderThin: Dp = 1.dp,
    val borderMedium: Dp = 1.5.dp,
    val borderThick: Dp = 2.dp,
    val dividerThickness: Dp = 1.dp
)

/**
 * Singleton constant object for direct static access anywhere across the app.
 * e.g., AppDimens.dp10, AppDimens.spacingMd, AppDimens.radiusLarge, AppDimens.iconStandard
 */
val AppDimens = AppDimensions()

/**
 * CompositionLocal for theme-based dimension access.
 * e.g., AppTheme.dimensions.spacingMd
 */
val LocalAppDimensions = staticCompositionLocalOf { AppDimensions() }
