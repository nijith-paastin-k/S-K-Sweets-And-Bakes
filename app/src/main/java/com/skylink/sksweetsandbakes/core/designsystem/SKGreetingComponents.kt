package com.skylink.sksweetsandbakes.core.designsystem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.skylink.sksweetsandbakes.R
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import com.skylink.sksweetsandbakes.ui.theme.AppFontSizes
import com.skylink.sksweetsandbakes.ui.theme.LocalScreenSizeClass
import java.util.Calendar

/**
 * 4 Dynamic Time-of-Day Periods for S K Sweets & Bakes:
 * 1. Morning - Sunrise warm peach/orange gradient, glowing sun, smiling sweet mascot
 * 2. Afternoon - Sky blue gradient, bright sun, sunglasses sweet mascot
 * 3. Evening - Twilight purple-magenta sunset gradient, setting sun, bow-tie sweet mascot
 * 4. Night - Midnight navy starry sky gradient, glowing crescent moon, sleeping sweet mascot with nightcap & "z z Z"
 */
enum class TimeOfDayPeriod(
    val title: String,
    val greeting: String,
    val subtitle: String,
    val timeRange: String,
    val badgeLabel: String,
    val gradientColors: List<Color>,
    val celestialColor: Color,
    val celestialAuraColor: Color,
    val mascotColor: Color = AppColors.MascotLadoo,
    val mascotCheekColor: Color = AppColors.MascotCheek
) {
    MORNING(
        title = "Morning",
        greeting = "Good Morning!",
        subtitle = "Fresh ghee sweets are out of kadai! Sign in to savor.",
        timeRange = "Morning Special",
        badgeLabel = "SUNRISE",
        gradientColors = AppColors.MorningGradient,
        celestialColor = AppColors.MorningSun,
        celestialAuraColor = AppColors.MorningAura
    ),
    AFTERNOON(
        title = "Afternoon",
        greeting = "Good Afternoon!",
        subtitle = "Time for a sweet break. Sign in to order.",
        timeRange = "Afternoon Special",
        badgeLabel = "BRIGHT DAY",
        gradientColors = AppColors.AfternoonGradient,
        celestialColor = AppColors.AfternoonSun,
        celestialAuraColor = AppColors.AfternoonAura
    ),
    EVENING(
        title = "Evening",
        greeting = "Good Evening!",
        subtitle = "Hot samosas & festive boxes for loved ones.",
        timeRange = "Evening Treats",
        badgeLabel = "SUNSET",
        gradientColors = AppColors.EveningGradient,
        celestialColor = AppColors.EveningSun,
        celestialAuraColor = AppColors.EveningAura
    ),
    NIGHT(
        title = "Night",
        greeting = "Good Night!",
        subtitle = "Late cravings? Pre-order for tomorrow morning.",
        timeRange = "Night Cravings",
        badgeLabel = "MOONLIT",
        gradientColors = AppColors.NightGradient,
        celestialColor = AppColors.NightMoon,
        celestialAuraColor = AppColors.NightAura
    );

    companion object {
        fun fromCurrentHour(
            hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        ): TimeOfDayPeriod {
            return when (hour) {
                in 5..11 -> MORNING
                in 12..16 -> AFTERNOON
                in 17..20 -> EVENING
                else -> NIGHT
            }
        }
    }
}

/**
 * Procedurally draws the adorable S K Sweets Mascot (Ladoo / Peda character)
 * with time-adaptive emotional expressions:
 * - Morning: Big cheerful smile & sparkling eyes
 * - Afternoon: Cool black sunglasses
 * - Evening: Dapper red bow-tie
 * - Night: Sleeping curved eyes with cute sleeping nightcap & floating "z z Z"
 */
@Composable
fun SweetMascotCanvas(
    period: TimeOfDayPeriod,
    modifier: Modifier = Modifier,
    size: Dp = AppDimens.dp100
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w * 0.5f
        val cy = h * 0.55f
        val radius = w * 0.38f

        drawOval(
            color = AppColors.MascotShadow,
            topLeft = Offset(cx - radius * 0.85f, cy + radius * 0.72f),
            size = Size(radius * 1.7f, radius * 0.3f)
        )

        val bodyBrush = Brush.radialGradient(
            colors = listOf(
                AppColors.MascotHighlight,
                period.mascotColor,
                AppColors.MascotShade.copy(alpha = 0.85f)
            ),
            center = Offset(cx - radius * 0.25f, cy - radius * 0.3f),
            radius = radius * 1.2f
        )
        drawCircle(
            brush = bodyBrush,
            radius = radius,
            center = Offset(cx, cy)
        )

        val garnishColor = AppColors.MascotPistachio
        val saffronColor = AppColors.MascotSaffron
        drawCircle(
            garnishColor,
            radius = radius * 0.045f,
            center = Offset(cx - radius * 0.45f, cy - radius * 0.35f)
        )
        drawCircle(
            saffronColor,
            radius = radius * 0.04f,
            center = Offset(cx + radius * 0.35f, cy - radius * 0.4f)
        )
        drawCircle(
            garnishColor,
            radius = radius * 0.05f,
            center = Offset(cx + radius * 0.1f, cy - radius * 0.55f)
        )
        drawCircle(
            saffronColor,
            radius = radius * 0.04f,
            center = Offset(cx - radius * 0.15f, cy + radius * 0.45f)
        )

        val cheekRadius = radius * 0.16f
        drawCircle(
            color = period.mascotCheekColor.copy(alpha = 0.65f),
            radius = cheekRadius,
            center = Offset(cx - radius * 0.52f, cy + radius * 0.08f)
        )
        drawCircle(
            color = period.mascotCheekColor.copy(alpha = 0.65f),
            radius = cheekRadius,
            center = Offset(cx + radius * 0.52f, cy + radius * 0.08f)
        )

        when (period) {
            TimeOfDayPeriod.MORNING -> {
                val eyeRadius = radius * 0.11f
                drawCircle(
                    AppColors.MascotDarkCharcoal,
                    eyeRadius,
                    Offset(cx - radius * 0.32f, cy - radius * 0.08f)
                )
                drawCircle(
                    AppColors.MascotDarkCharcoal,
                    eyeRadius,
                    Offset(cx + radius * 0.32f, cy - radius * 0.08f)
                )
                drawCircle(
                    Color.White,
                    eyeRadius * 0.38f,
                    Offset(cx - radius * 0.35f, cy - radius * 0.12f)
                )
                drawCircle(
                    Color.White,
                    eyeRadius * 0.38f,
                    Offset(cx + radius * 0.29f, cy - radius * 0.12f)
                )

                val smilePath = Path().apply {
                    moveTo(cx - radius * 0.28f, cy + radius * 0.12f)
                    quadraticTo(cx, cy + radius * 0.38f, cx + radius * 0.28f, cy + radius * 0.12f)
                }
                drawPath(
                    smilePath,
                    AppColors.MascotDarkCharcoal,
                    style = Stroke(width = radius * 0.08f, cap = StrokeCap.Round)
                )
            }

            TimeOfDayPeriod.AFTERNOON -> {
                val glassWidth = radius * 0.42f
                val glassHeight = radius * 0.28f
                drawRoundRect(
                    color = AppColors.MascotSunglasses,
                    topLeft = Offset(cx - radius * 0.65f, cy - radius * 0.22f),
                    size = Size(glassWidth, glassHeight),
                    cornerRadius = CornerRadius(radius * 0.08f, radius * 0.08f)
                )
                drawLine(
                    color = AppColors.MascotGlareWhite,
                    start = Offset(cx - radius * 0.58f, cy - radius * 0.18f),
                    end = Offset(cx - radius * 0.35f, cy + radius * 0.02f),
                    strokeWidth = radius * 0.04f,
                    cap = StrokeCap.Round
                )
                drawRoundRect(
                    color = AppColors.MascotSunglasses,
                    topLeft = Offset(cx + radius * 0.23f, cy - radius * 0.22f),
                    size = Size(glassWidth, glassHeight),
                    cornerRadius = CornerRadius(radius * 0.08f, radius * 0.08f)
                )
                drawLine(
                    color = AppColors.MascotGlareWhite,
                    start = Offset(cx + radius * 0.30f, cy - radius * 0.18f),
                    end = Offset(cx + radius * 0.53f, cy + radius * 0.02f),
                    strokeWidth = radius * 0.04f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = AppColors.MascotSunglasses,
                    start = Offset(cx - radius * 0.25f, cy - radius * 0.12f),
                    end = Offset(cx + radius * 0.25f, cy - radius * 0.12f),
                    strokeWidth = radius * 0.06f,
                    cap = StrokeCap.Round
                )
                val smirkPath = Path().apply {
                    moveTo(cx - radius * 0.18f, cy + radius * 0.18f)
                    quadraticTo(
                        cx + radius * 0.08f,
                        cy + radius * 0.32f,
                        cx + radius * 0.26f,
                        cy + radius * 0.14f
                    )
                }
                drawPath(
                    smirkPath,
                    AppColors.MascotDarkCharcoal,
                    style = Stroke(width = radius * 0.08f, cap = StrokeCap.Round)
                )
            }

            TimeOfDayPeriod.EVENING -> {
                val eyeRadius = radius * 0.11f
                drawCircle(
                    AppColors.MascotDarkCharcoal,
                    eyeRadius,
                    Offset(cx - radius * 0.32f, cy - radius * 0.08f)
                )
                drawCircle(
                    AppColors.MascotDarkCharcoal,
                    eyeRadius,
                    Offset(cx + radius * 0.32f, cy - radius * 0.08f)
                )
                drawCircle(
                    Color.White,
                    eyeRadius * 0.38f,
                    Offset(cx - radius * 0.35f, cy - radius * 0.12f)
                )
                drawCircle(
                    Color.White,
                    eyeRadius * 0.38f,
                    Offset(cx + radius * 0.29f, cy - radius * 0.12f)
                )
                val smilePath = Path().apply {
                    moveTo(cx - radius * 0.24f, cy + radius * 0.14f)
                    quadraticTo(cx, cy + radius * 0.34f, cx + radius * 0.24f, cy + radius * 0.14f)
                }
                drawPath(
                    smilePath,
                    AppColors.MascotDarkCharcoal,
                    style = Stroke(width = radius * 0.08f, cap = StrokeCap.Round)
                )

                val bowY = cy + radius * 0.78f
                val bowLeft = Path().apply {
                    moveTo(cx, bowY)
                    lineTo(cx - radius * 0.28f, bowY - radius * 0.14f)
                    lineTo(cx - radius * 0.28f, bowY + radius * 0.14f)
                    close()
                }
                val bowRight = Path().apply {
                    moveTo(cx, bowY)
                    lineTo(cx + radius * 0.28f, bowY - radius * 0.14f)
                    lineTo(cx + radius * 0.28f, bowY + radius * 0.14f)
                    close()
                }
                drawPath(bowLeft, AppColors.MascotBowtie)
                drawPath(bowRight, AppColors.MascotBowtie)
                drawCircle(AppColors.MascotBowtieKnot, radius * 0.07f, Offset(cx, bowY))
            }

            TimeOfDayPeriod.NIGHT -> {
                val leftEyePath = Path().apply {
                    moveTo(cx - radius * 0.42f, cy - radius * 0.04f)
                    quadraticTo(
                        cx - radius * 0.30f,
                        cy + radius * 0.06f,
                        cx - radius * 0.18f,
                        cy - radius * 0.04f
                    )
                }
                val rightEyePath = Path().apply {
                    moveTo(cx + radius * 0.18f, cy - radius * 0.04f)
                    quadraticTo(
                        cx + radius * 0.30f,
                        cy + radius * 0.06f,
                        cx + radius * 0.42f,
                        cy - radius * 0.04f
                    )
                }
                drawPath(
                    leftEyePath,
                    AppColors.MascotDarkCharcoal,
                    style = Stroke(width = radius * 0.07f, cap = StrokeCap.Round)
                )
                drawPath(
                    rightEyePath,
                    AppColors.MascotDarkCharcoal,
                    style = Stroke(width = radius * 0.07f, cap = StrokeCap.Round)
                )

                drawCircle(
                    AppColors.MascotDarkCharcoal,
                    radius * 0.07f,
                    Offset(cx, cy + radius * 0.20f)
                )

                val hatPath = Path().apply {
                    moveTo(cx - radius * 0.55f, cy - radius * 0.65f)
                    lineTo(cx + radius * 0.55f, cy - radius * 0.65f)
                    lineTo(cx + radius * 0.15f, cy - radius * 1.35f)
                    close()
                }
                drawPath(hatPath, AppColors.MascotNightcap)

                drawRoundRect(
                    color = AppColors.MascotNightcapPompom,
                    topLeft = Offset(cx - radius * 0.62f, cy - radius * 0.72f),
                    size = Size(radius * 1.24f, radius * 0.16f),
                    cornerRadius = CornerRadius(radius * 0.08f, radius * 0.08f)
                )

                drawCircle(
                    AppColors.MascotMoonStar,
                    radius * 0.12f,
                    Offset(cx + radius * 0.15f, cy - radius * 1.35f)
                )
            }
        }
    }
}

/**
 * Royal Circular Brand Emblem for S K Sweets & Bakes
 */
@Composable
fun SKBrandLogoEmblem(
    size: Dp = AppDimens.dp80,
    modifier: Modifier = Modifier,
    borderWidth: Dp = AppDimens.dp0,
    borderColor: Color = Color.Transparent,
    elevation: Dp = AppDimens.dp4
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "S K Sweets & Bakes Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Modern Royal Header Banner displaying the dynamic time-of-day greeting
 * with animated celestial body (Sun/Moon), glowing aura, floating clouds/stars,
 * brand emblem, and time selector chip bar.
 */
@Composable
fun SKTimeOfDayHeroBanner(
    currentPeriod: TimeOfDayPeriod,
    modifier: Modifier = Modifier,
    onPeriodSelected: ((TimeOfDayPeriod) -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(AppDimens.dp24),
        shadowElevation = AppDimens.dp8,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.dp24))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(currentPeriod.gradientColors))
                .padding(bottom = AppDimens.dp12)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
            ) {
                val w = size.width
                val h = size.height

                val celestialX = w * 0.82f
                val celestialY = h * 0.26f
                val celestialR = w * 0.13f

                drawCircle(
                    color = currentPeriod.celestialAuraColor,
                    radius = celestialR * 1.55f,
                    center = Offset(celestialX, celestialY)
                )

                if (currentPeriod == TimeOfDayPeriod.NIGHT) {
                    drawCircle(
                        color = currentPeriod.celestialColor,
                        radius = celestialR,
                        center = Offset(celestialX, celestialY)
                    )
                    drawCircle(
                        color = currentPeriod.gradientColors[0],
                        radius = celestialR * 0.82f,
                        center = Offset(
                            celestialX + celestialR * 0.42f,
                            celestialY - celestialR * 0.20f
                        )
                    )
                    val stars = listOf(
                        Offset(w * 0.68f, h * 0.15f),
                        Offset(w * 0.74f, h * 0.44f),
                        Offset(w * 0.92f, h * 0.18f),
                        Offset(w * 0.58f, h * 0.32f),
                        Offset(w * 0.64f, h * 0.52f)
                    )
                    stars.forEach { pt ->
                        drawCircle(Color.White.copy(alpha = 0.85f), radius = 2.5f, center = pt)
                    }
                } else {
                    drawCircle(
                        color = currentPeriod.celestialColor,
                        radius = celestialR,
                        center = Offset(celestialX, celestialY)
                    )
                }

                if (currentPeriod == TimeOfDayPeriod.MORNING || currentPeriod == TimeOfDayPeriod.AFTERNOON) {
                    val cloudColor = Color.White.copy(alpha = 0.45f)
                    drawCircle(
                        cloudColor,
                        radius = AppDimens.dp20.toPx(),
                        center = Offset(w * 0.28f, h * 0.62f)
                    )
                    drawCircle(
                        cloudColor,
                        radius = AppDimens.dp14.toPx(),
                        center = Offset(w * 0.21f, h * 0.64f)
                    )
                    drawCircle(
                        cloudColor,
                        radius = AppDimens.dp16.toPx(),
                        center = Offset(w * 0.34f, h * 0.64f)
                    )
                    drawRoundRect(
                        color = cloudColor,
                        topLeft = Offset(w * 0.18f, h * 0.62f),
                        size = Size(w * 0.18f, AppDimens.dp12.toPx()),
                        cornerRadius = CornerRadius(AppDimens.dp6.toPx(), AppDimens.dp6.toPx())
                    )
                }

                val hillPath = Path().apply {
                    moveTo(0f, h * 0.88f)
                    quadraticTo(w * 0.35f, h * 0.76f, w * 0.65f, h * 0.86f)
                    quadraticTo(w * 0.85f, h * 0.94f, w, h * 0.85f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(hillPath, AppColors.MascotHillShadow)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp14)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SKBrandLogoEmblem(
                            size = AppDimens.dp38,
                            borderWidth = AppDimens.dp1_5,
                            elevation = AppDimens.dp2
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                        Column {
                            Text(
                                text = "S K Sweets & Bakes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = AppFontSizes.letterSpacingExtraLoose
                            )
                            Text(
                                text = "Purity Since 1998",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = AppFontSizes.sp10
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp12),
                        color = Color.Black.copy(alpha = 0.28f),
                        border = BorderStroke(AppDimens.dp1, Color.White.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = AppDimens.dp8,
                                vertical = AppDimens.dp4
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(AppDimens.dp12)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp4))
                            Text(
                                text = currentPeriod.badgeLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = AppFontSizes.sp10
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppDimens.dp14))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentPeriod.greeting,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        Text(
                            text = currentPeriod.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.92f),
                            lineHeight = AppFontSizes.lh18
                        )
                    }

                    Spacer(modifier = Modifier.width(AppDimens.dp10))

                    Box(contentAlignment = Alignment.TopEnd) {
                        SweetMascotCanvas(
                            period = currentPeriod,
                            size = AppDimens.dp105
                        )
                        if (currentPeriod == TimeOfDayPeriod.NIGHT) {
                            Text(
                                text = "z z Z",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = AppColors.MascotMoonStar,
                                modifier = Modifier.padding(
                                    top = AppDimens.dp4,
                                    end = AppDimens.dp4
                                )
                            )
                        }
                    }
                }

                if (onPeriodSelected != null) {
                    Spacer(modifier = Modifier.height(AppDimens.dp10))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp6)
                    ) {
                        items(TimeOfDayPeriod.values()) { period ->
                            val isSelected = period == currentPeriod
                            Surface(
                                shape = RoundedCornerShape(AppDimens.dp16),
                                color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.22f),
                                border = BorderStroke(
                                    AppDimens.dp1,
                                    if (isSelected) Color.White else Color.White.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(AppDimens.dp16))
                                    .clickable { onPeriodSelected(period) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = AppDimens.dp10,
                                        vertical = AppDimens.dp4
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when (period) {
                                            TimeOfDayPeriod.MORNING -> "🌅 Morning"
                                            TimeOfDayPeriod.AFTERNOON -> "☀️ Afternoon"
                                            TimeOfDayPeriod.EVENING -> "🌇 Evening"
                                            TimeOfDayPeriod.NIGHT -> "🌙 Night"
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (isSelected) AppColors.MascotDarkCharcoal else Color.White,
                                        fontSize = AppFontSizes.sp10
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact Greeting Avatar Circle as showcased in the mockups.
 * Suitable for Home top-bar greeting card and My Account Profile header.\n */
@Composable
fun SKGreetingAvatar(
    period: TimeOfDayPeriod = TimeOfDayPeriod.fromCurrentHour(),
    size: Dp = AppDimens.dp48,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(AppDimens.dp2, AppColors.GoldAccent),
        shadowElevation = AppDimens.dp4,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(period.gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            SweetMascotCanvas(
                period = period,
                size = size * 0.85f
            )
        }
    }
}

/**
 * Glassmorphic Floating Capsule Bottom Navigation Bar
 * Reference: Modern iOS/VisionOS command pill bar requested by user.
 * Adaptive features:
 * - Automatically scales tab sizes and padding across screen sizes from 240px to 1440px
 * - Responsive inner active pill highlight with widthIn constraints
 * - Compact label typography on small displays
 */
@Composable
fun SKLiquidBottomNavigation(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    items: List<LiquidNavItemSpec>,
    modifier: Modifier = Modifier
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val screenSize = LocalScreenSizeClass.current
    val isSmallScreen = screenSize.isSmallScreen

    Surface(
        shape = RoundedCornerShape(AppDimens.dp36),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = AppDimens.dp16,
        tonalElevation = AppDimens.dp6,
        border = BorderStroke(
            AppDimens.dp1,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.dp36))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (isSmallScreen) AppDimens.dp4 else AppDimens.dp8,
                    vertical = if (isSmallScreen) AppDimens.dp4 else AppDimens.dp6
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LiquidNavItemView(
                        item = item,
                        isSelected = selectedIndex == index,
                        isSmallScreen = isSmallScreen,
                        activeColor = activeColor,
                        onSelect = { onItemSelected(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LiquidNavItemView(
    item: LiquidNavItemSpec,
    isSelected: Boolean,
    isSmallScreen: Boolean,
    activeColor: Color,
    onSelect: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 420f),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(AppDimens.dp16))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onSelect() }
            .padding(vertical = if (isSmallScreen) AppDimens.dp2 else AppDimens.dp4)
    ) {
        Surface(
            shape = RoundedCornerShape(AppDimens.dp16),
            color = if (isSelected) {
                activeColor.copy(alpha = 0.18f)
            } else {
                Color.Transparent
            },
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .widthIn(max = AppDimens.dp56)
                .height(if (isSmallScreen) AppDimens.dp28 else AppDimens.dp32)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                BadgedBox(
                    badge = {
                        if (item.badgeCount > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text(
                                    "${item.badgeCount}",
                                    fontSize = if (isSmallScreen) AppFontSizes.sp8 else AppFontSizes.sp9,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) {
                            activeColor
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.70f)
                        },
                        modifier = Modifier.size(if (isSmallScreen) AppDimens.dp18 else AppDimens.dp22)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isSmallScreen) AppDimens.dp2 else AppDimens.dp4))

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) {
                activeColor
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
            },
            fontSize = if (isSmallScreen) AppFontSizes.sp9 else AppFontSizes.sp11,
            maxLines = 1,
            softWrap = false
        )
    }
}

data class LiquidNavItemSpec(
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int = 0
)
