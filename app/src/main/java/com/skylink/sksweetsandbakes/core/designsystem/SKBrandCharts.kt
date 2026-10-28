package com.skylink.sksweetsandbakes.core.designsystem

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import com.skylink.sksweetsandbakes.ui.theme.AppFontSizes
import kotlin.math.atan2

/**
 * Data model for Donut / Pie Chart slices.
 */
data class PieChartSlice(
    val label: String,
    val value: Double,
    val color: Color,
    val formattedValue: String
)

/**
 * Premium Interactive Donut / Pie Chart for Owner Analytics.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SKDonutPieChart(
    title: String,
    subtitle: String = "",
    slices: List<PieChartSlice>,
    modifier: Modifier = Modifier,
    chartSize: Dp = AppDimens.dp180,
    strokeWidth: Dp = AppDimens.dp32,
    centerTitle: String = "Total",
    centerSubtitle: String = ""
) {
    val total = remember(slices) { slices.sumOf { it.value } }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(slices) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(AppDimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (total <= 0.0 || slices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartSize),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No sales data recorded yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppDimens.spacingSm),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(chartSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(slices, total) {
                                    detectTapGestures { tapOffset ->
                                        val center = Offset(size.width / 2f, size.height / 2f)
                                        val dx = tapOffset.x - center.x
                                        val dy = tapOffset.y - center.y
                                        var touchAngle =
                                            Math.toDegrees(atan2(dy.toDouble(), dx.toDouble()))
                                                .toFloat()
                                        if (touchAngle < 0f) touchAngle += 360f

                                        var cumulative = 0f
                                        var tapped: Int? = null
                                        for ((idx, slice) in slices.withIndex()) {
                                            val sweep = (slice.value / total * 360.0).toFloat()
                                            if (touchAngle >= cumulative && touchAngle < cumulative + sweep) {
                                                tapped = idx
                                                break
                                            }
                                            cumulative += sweep
                                        }
                                        selectedIndex =
                                            if (selectedIndex == tapped) null else tapped
                                    }
                                }
                        ) {
                            val strokePx = strokeWidth.toPx()
                            val chartDiameter = size.minDimension - strokePx
                            val topLeft = Offset(
                                (size.width - chartDiameter) / 2f,
                                (size.height - chartDiameter) / 2f
                            )
                            val arcSize = Size(chartDiameter, chartDiameter)

                            var startAngle = -90f
                            slices.forEachIndexed { idx, slice ->
                                val sweepAngle =
                                    (slice.value / total * 360.0).toFloat() * progress.value
                                val isSelected = selectedIndex == idx
                                val actualStroke = if (isSelected) strokePx * 1.2f else strokePx
                                val sliceColor = if (selectedIndex != null && !isSelected) {
                                    slice.color.copy(alpha = 0.45f)
                                } else {
                                    slice.color
                                }

                                drawArc(
                                    color = sliceColor,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(width = actualStroke, cap = StrokeCap.Butt)
                                )
                                startAngle += sweepAngle
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = AppDimens.spacingXl)
                        ) {
                            val activeSlice = selectedIndex?.let { slices.getOrNull(it) }
                            if (activeSlice != null) {
                                Text(
                                    text = activeSlice.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = activeSlice.color,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = activeSlice.formattedValue,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                val pct = (activeSlice.value / total * 100).toInt()
                                Text(
                                    text = "$pct%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = centerTitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (centerSubtitle.isNotBlank()) {
                                    Text(
                                        text = centerSubtitle,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                ) {
                    slices.forEachIndexed { idx, slice ->
                        val isSelected = selectedIndex == idx
                        val pct = if (total > 0) (slice.value / total * 100).toInt() else 0
                        Surface(
                            shape = RoundedCornerShape(AppDimens.radiusSmall),
                            color = if (isSelected) slice.color.copy(alpha = 0.20f) else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                AppDimens.dp1,
                                if (isSelected) slice.color else MaterialTheme.colorScheme.outlineVariant.copy(
                                    alpha = 0.5f
                                )
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimens.radiusSmall))
                                .clickable { selectedIndex = if (isSelected) null else idx }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(
                                    horizontal = AppDimens.dp8,
                                    vertical = AppDimens.dp6
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(AppDimens.dp10)
                                        .clip(CircleShape)
                                        .background(slice.color)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp6))
                                Text(
                                    text = "${slice.label} ($pct%)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp4))
                                Text(
                                    text = slice.formattedValue,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Data model for Bar Chart bars.
 */
data class BarChartItem(
    val label: String,
    val value: Double,
    val displayValue: String,
    val color: Color? = null,
    val isHighlighted: Boolean = false
)

/**
 * Premium Animated Bar Chart for Owner Sales Trends.
 */
@Composable
fun SKBarChart(
    title: String,
    subtitle: String = "",
    items: List<BarChartItem>,
    modifier: Modifier = Modifier,
    barHeight: Dp = AppDimens.dp150,
    defaultBarColor: Color = AppColors.GoldSecondary,
    highlightBarColor: Color = AppColors.MaroonPrimary
) {
    val maxValue = remember(items) { (items.maxOfOrNull { it.value } ?: 1.0).coerceAtLeast(1.0) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(items) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(AppDimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(barHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No trend data available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val activeItem = selectedIndex?.let { items.getOrNull(it) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp24),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (activeItem != null) {
                        Text(
                            text = "${activeItem.label}: ${activeItem.displayValue}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = activeItem.color
                                ?: if (activeItem.isHighlighted) highlightBarColor else defaultBarColor
                        )
                    } else {
                        Text(
                            text = "Tap any bar for detailed value",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(barHeight),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    items.forEachIndexed { idx, barItem ->
                        val isSelected = selectedIndex == idx
                        val barRatio = (barItem.value / maxValue).toFloat().coerceIn(0.05f, 1f)
                        val effectiveHeight = (barRatio * progress.value).coerceIn(0.05f, 1f)
                        val itemColor = barItem.color
                            ?: if (barItem.isHighlighted || isSelected) highlightBarColor else defaultBarColor

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { selectedIndex = if (isSelected) null else idx },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = if (barItem.value >= 1000) "${(barItem.value / 1000).toInt()}k" else barItem.value.toInt()
                                    .toString(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = AppFontSizes.sp9,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) itemColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(AppDimens.dp4))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .fillMaxHeight(effectiveHeight)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = AppDimens.radiusSmall,
                                            topEnd = AppDimens.radiusSmall
                                        )
                                    )
                                    .background(itemColor)
                            )

                            Spacer(modifier = Modifier.height(AppDimens.dp8))

                            Text(
                                text = barItem.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (barItem.isHighlighted || isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (barItem.isHighlighted || isSelected) itemColor else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
