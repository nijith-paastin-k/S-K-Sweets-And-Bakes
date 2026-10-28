package com.skylink.sksweetsandbakes.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.skylink.sksweetsandbakes.data.local.AppThemeMode

private val LogoDarkColorScheme = darkColorScheme(
    primary = AppColors.GoldLight,
    onPrimary = AppColors.DarkBackground,
    primaryContainer = AppColors.MaroonDark,
    onPrimaryContainer = AppColors.CreamBackground,
    secondary = AppColors.GoldSecondary,
    onSecondary = AppColors.DarkBackground,
    secondaryContainer = AppColors.GoldDarkContainer,
    onSecondaryContainer = AppColors.GoldLight,
    tertiary = AppColors.AmberLight,
    onTertiary = AppColors.DarkBackground,
    background = AppColors.DarkBackground,
    onBackground = AppColors.TextPrimaryDark,
    surface = AppColors.DarkSurface,
    onSurface = AppColors.TextPrimaryDark,
    surfaceVariant = AppColors.DarkSurfaceVariant,
    onSurfaceVariant = AppColors.TextSecondaryDark,
    outline = AppColors.DarkOutline,
    outlineVariant = AppColors.DarkOutlineVariant,
    error = AppColors.StatusError,
    onError = Color.White
)

private val LogoLightColorScheme = lightColorScheme(
    primary = AppColors.MaroonPrimary,
    onPrimary = Color.White,
    primaryContainer = AppColors.MaroonContainer,
    onPrimaryContainer = AppColors.MaroonDark,
    secondary = AppColors.GoldSecondary,
    onSecondary = Color.White,
    secondaryContainer = AppColors.GoldContainer,
    onSecondaryContainer = AppColors.GoldDark,
    tertiary = AppColors.AmberTertiary,
    onTertiary = Color.White,
    background = AppColors.CreamBackground,
    onBackground = AppColors.TextPrimaryLight,
    surface = AppColors.WarmSurface,
    onSurface = AppColors.TextPrimaryLight,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.TextSecondaryLight,
    outline = AppColors.Outline,
    outlineVariant = AppColors.OutlineVariant,
    error = AppColors.StatusError,
    onError = Color.White
)

/**
 * AppTheme
 * The unified, one-stop access point for the entire application's design system tokens:
 * - AppTheme.colors : Current active dynamic/static ColorScheme (Light/Dark)
 * - AppTheme.typography : Material 3 Typography scale
 * - AppTheme.dimensions : Layout spacing, paddings, corner radii, component sizes, elevations
 * - AppTheme.screenSize : Adaptive ScreenSizeClass (widthClass, heightClass, widthDp, heightDp)
 * - AppTheme.fonts : AppFonts (Poppins all weights, Monospace, Default)
 * - AppTheme.fontSizes : AppFontSizes (Material sp scale, granular sizes, line heights)
 * - AppTheme.styles : AppCustomStyles (receipts, price tags, badges)
 * - AppTheme.shapes : Material 3 Shapes
 */
object AppTheme {
    val colors: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val dimensions: AppDimensions
        @Composable
        get() = LocalAppDimensions.current

    val screenSize: ScreenSizeClass
        @Composable
        get() = LocalScreenSizeClass.current

    val fonts: AppFonts
        get() = AppFonts

    val fontSizes: AppFontSizes
        get() = AppFontSizes

    val styles: AppCustomStyles
        get() = AppCustomStyles

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes
}

@Composable
fun SKSweetsAndBakesTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    dimensions: AppDimensions = AppDimens,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> systemDark
    }

    val colorScheme = if (isDark) LogoDarkColorScheme else LogoLightColorScheme

    val screenSizeClass = rememberScreenSizeClass()
    val resolvedDimensions =
        remember(screenSizeClass.widthClass, screenSizeClass.heightClass, dimensions) {
            if (dimensions != AppDimens) {
                dimensions
            } else {
                when {
                    screenSizeClass.isSmallScreen || screenSizeClass.isCompactHeight -> compactSmallDimensions()
                    screenSizeClass.isWideScreen -> expandedDimensions()
                    else -> AppDimens
                }
            }
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    CompositionLocalProvider(
        LocalScreenSizeClass provides screenSizeClass,
        LocalAppDimensions provides resolvedDimensions
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
