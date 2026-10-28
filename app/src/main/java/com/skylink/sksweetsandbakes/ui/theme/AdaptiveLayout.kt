package com.skylink.sksweetsandbakes.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * WindowWidthClass
 * Breakpoints to adapt layouts across:
 * - COMPACT_SMALL: < 340dp (Minimum devices like 240x320 px)
 * - COMPACT: 340dp .. 599dp (Standard mobile phones)
 * - MEDIUM: 600dp .. 839dp (Foldables unfolded, small tablets)
 * - EXPANDED: >= 840dp (Large tablets, maximum 1440px wide displays)
 */
enum class WindowWidthClass {
    COMPACT_SMALL,
    COMPACT,
    MEDIUM,
    EXPANDED
}

/**
 * WindowHeightClass
 * Breakpoints for vertical constraints:
 * - COMPACT_SMALL: < 480dp (Minimum vertical devices like 240x320 px, landscape)
 * - MEDIUM: 480dp .. 899dp (Standard mobile phones)
 * - EXPANDED: >= 900dp (Ultra-tall flagship phones like 1440x3120 px)
 */
enum class WindowHeightClass {
    COMPACT_SMALL,
    MEDIUM,
    EXPANDED
}

@Immutable
data class ScreenSizeClass(
    val widthClass: WindowWidthClass,
    val heightClass: WindowHeightClass,
    val widthDp: Dp,
    val heightDp: Dp
) {
    val isSmallScreen: Boolean get() = widthClass == WindowWidthClass.COMPACT_SMALL
    val isCompactHeight: Boolean get() = heightClass == WindowHeightClass.COMPACT_SMALL
    val isWideScreen: Boolean get() = widthClass == WindowWidthClass.MEDIUM || widthClass == WindowWidthClass.EXPANDED
}

val LocalScreenSizeClass = staticCompositionLocalOf {
    ScreenSizeClass(
        widthClass = WindowWidthClass.COMPACT,
        heightClass = WindowHeightClass.MEDIUM,
        widthDp = 360.dp,
        heightDp = 640.dp
    )
}

@Composable
fun rememberScreenSizeClass(): ScreenSizeClass {
    val config = LocalConfiguration.current
    val width = config.screenWidthDp
    val height = config.screenHeightDp

    val widthClass = when {
        width < 340 -> WindowWidthClass.COMPACT_SMALL
        width < 600 -> WindowWidthClass.COMPACT
        width < 840 -> WindowWidthClass.MEDIUM
        else -> WindowWidthClass.EXPANDED
    }

    val heightClass = when {
        height < 480 -> WindowHeightClass.COMPACT_SMALL
        height < 900 -> WindowHeightClass.MEDIUM
        else -> WindowHeightClass.EXPANDED
    }

    return ScreenSizeClass(
        widthClass = widthClass,
        heightClass = heightClass,
        widthDp = width.dp,
        heightDp = height.dp
    )
}

/**
 * Constrains content width on large/wide screens (up to 1440px)
 * and centers it horizontally so forms, cards, and dialogs remain readable.
 */
fun Modifier.adaptiveContentWidth(maxWidth: Dp = 560.dp): Modifier = this
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
    .widthIn(max = maxWidth)

/**
 * Adaptive product/category grid layout:
 * - 240px: 1 column
 * - 360-480px: 2 columns
 * - 600-840px: 3-4 columns
 * - 1080-1440px: 4-6 columns
 */
fun adaptiveProductGridCells(minCardWidth: Dp = 135.dp): GridCells =
    GridCells.Adaptive(minSize = minCardWidth)
