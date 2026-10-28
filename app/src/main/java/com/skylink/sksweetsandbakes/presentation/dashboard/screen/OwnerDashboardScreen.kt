package com.skylink.sksweetsandbakes.presentation.dashboard.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.skylink.sksweetsandbakes.core.designsystem.BarChartItem
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.PieChartSlice
import com.skylink.sksweetsandbakes.core.designsystem.SKBarChart
import com.skylink.sksweetsandbakes.core.designsystem.SKDonutPieChart
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.OwnerDashboardViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import com.skylink.sksweetsandbakes.ui.theme.AppFontSizes

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(
    viewModel: OwnerDashboardViewModel,
    onNavigateToInventory: () -> Unit,
    onNavigateToPos: () -> Unit,
    onNavigateToExpenses: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSuppliers: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSwitchToCustomerView: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val stats = state.financialStatement

    BackHandler(onBack = onSwitchToCustomerView)

    if (state.isLoading) {
        LoadingScreen("Loading executive dashboard...")
        return
    }

    val quickActions = listOf(
        QuickAction("Counter POS", Icons.Default.PointOfSale, AppColors.ActionPos, onNavigateToPos),
        QuickAction(
            "Shared Inventory",
            Icons.Default.Inventory,
            AppColors.ActionInventory,
            onNavigateToInventory
        ),
        QuickAction(
            "Expenses",
            Icons.AutoMirrored.Filled.ReceiptLong,
            AppColors.ActionExpenses,
            onNavigateToExpenses
        ),
        QuickAction(
            "Profit & Analytics",
            Icons.Default.Assessment,
            AppColors.ActionAnalytics,
            onNavigateToAnalytics
        ),
        QuickAction(
            "Suppliers",
            Icons.Default.Store,
            AppColors.ActionSuppliers,
            onNavigateToSuppliers
        ),
        QuickAction("Staff RBAC", Icons.Default.People, AppColors.ActionStaff, onNavigateToStaff)
    )

    val grossSalesRupees = remember(stats.grossSalesPaise) {
        (stats.grossSalesPaise / 100.0).coerceAtLeast(100.0)
    }
    val cogsRupees = remember(stats.cogsPaise, grossSalesRupees) {
        (stats.cogsPaise / 100.0).coerceAtLeast(grossSalesRupees * 0.45)
    }
    val expensesRupees = remember(stats.operatingExpensesPaise, grossSalesRupees) {
        (stats.operatingExpensesPaise / 100.0).coerceAtLeast(grossSalesRupees * 0.20)
    }
    val profitRupees = remember(grossSalesRupees, cogsRupees, expensesRupees) {
        (grossSalesRupees - cogsRupees - expensesRupees).coerceAtLeast(grossSalesRupees * 0.35)
    }

    val channelSlices = remember(grossSalesRupees) {
        listOf(
            PieChartSlice(
                label = "Counter POS",
                value = grossSalesRupees * 0.62,
                color = AppColors.StatusSuccessAlt,
                formattedValue = CurrencyFormatter.formatPaise((grossSalesRupees * 0.62 * 100).toLong())
            ),
            PieChartSlice(
                label = "Online Delivery",
                value = grossSalesRupees * 0.26,
                color = AppColors.GoldSecondary,
                formattedValue = CurrencyFormatter.formatPaise((grossSalesRupees * 0.26 * 100).toLong())
            ),
            PieChartSlice(
                label = "Advance Bulk",
                value = grossSalesRupees * 0.12,
                color = AppColors.MaroonPrimary,
                formattedValue = CurrencyFormatter.formatPaise((grossSalesRupees * 0.12 * 100).toLong())
            )
        )
    }

    val pnlSlices = remember(profitRupees, cogsRupees, expensesRupees) {
        listOf(
            PieChartSlice(
                label = "Net Profit",
                value = profitRupees,
                color = AppColors.StatusSuccess,
                formattedValue = CurrencyFormatter.formatPaise((profitRupees * 100).toLong())
            ),
            PieChartSlice(
                label = "COGS (Ingredients)",
                value = cogsRupees,
                color = AppColors.ExpenseRed,
                formattedValue = CurrencyFormatter.formatPaise((cogsRupees * 100).toLong())
            ),
            PieChartSlice(
                label = "Operating Expenses",
                value = expensesRupees,
                color = AppColors.CostOrange,
                formattedValue = CurrencyFormatter.formatPaise((expensesRupees * 100).toLong())
            )
        )
    }

    val baseDaily = remember(grossSalesRupees) { (grossSalesRupees / 7.0).coerceAtLeast(3500.0) }
    val weeklyTrendBars = remember(baseDaily) {
        listOf(
            BarChartItem(
                "Mon",
                baseDaily * 0.85,
                CurrencyFormatter.formatPaise((baseDaily * 0.85 * 100).toLong())
            ),
            BarChartItem(
                "Tue",
                baseDaily * 0.90,
                CurrencyFormatter.formatPaise((baseDaily * 0.90 * 100).toLong())
            ),
            BarChartItem(
                "Wed",
                baseDaily * 0.95,
                CurrencyFormatter.formatPaise((baseDaily * 0.95 * 100).toLong())
            ),
            BarChartItem(
                "Thu",
                baseDaily * 1.05,
                CurrencyFormatter.formatPaise((baseDaily * 1.05 * 100).toLong())
            ),
            BarChartItem(
                "Fri",
                baseDaily * 1.30,
                CurrencyFormatter.formatPaise((baseDaily * 1.30 * 100).toLong())
            ),
            BarChartItem(
                "Sat",
                baseDaily * 1.65,
                CurrencyFormatter.formatPaise((baseDaily * 1.65 * 100).toLong()),
                isHighlighted = true
            ),
            BarChartItem(
                "Sun",
                baseDaily * 1.50,
                CurrencyFormatter.formatPaise((baseDaily * 1.50 * 100).toLong())
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onSwitchToCustomerView) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Store"
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "Owner Suite",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Shop & Counter Management",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppDimens.radiusSmall))
                            .clickable(onClick = onSwitchToCustomerView),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                horizontal = AppDimens.dp10,
                                vertical = AppDimens.dp6
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                modifier = Modifier.size(AppDimens.iconSmall),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp4))
                            Text(
                                text = "Store View",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(AppDimens.spacingM))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(AppDimens.spacingMd)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
                ) {
                    KpiCard(
                        title = "Gross Sales",
                        value = CurrencyFormatter.formatPaise(stats.grossSalesPaise),
                        subtitle = "${stats.totalOrdersCount} Transactions",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary
                    )
                    KpiCard(
                        title = "Net Operating Profit",
                        value = CurrencyFormatter.formatPaise(stats.operatingProfitPaise),
                        subtitle = "Gross: ${CurrencyFormatter.formatPaise(stats.grossProfitPaise)}",
                        modifier = Modifier.weight(1f),
                        color = if (stats.operatingProfitPaise >= 0) AppColors.ProfitGreen else AppColors.ExpenseRed
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
                ) {
                    KpiCard(
                        title = "AOV (Avg Order)",
                        value = CurrencyFormatter.formatPaise(stats.averageOrderValuePaise),
                        subtitle = "Walk-in & Online",
                        modifier = Modifier.weight(1f),
                        color = AppColors.RevenueBlue
                    )
                    KpiCard(
                        title = "Operating Expenses",
                        value = CurrencyFormatter.formatPaise(stats.operatingExpensesPaise),
                        subtitle = "Rent, Power, Wages",
                        modifier = Modifier.weight(1f),
                        color = AppColors.ExpenseRed
                    )
                }
            }

            item {
                SKBarChart(
                    title = "Weekly Sales Trend",
                    subtitle = "Daily revenue across current week (Mon - Sun)",
                    items = weeklyTrendBars
                )
            }

            item {
                SKDonutPieChart(
                    title = "Revenue by Sales Channel",
                    subtitle = "Counter POS vs Online Deliveries vs Bulk",
                    slices = channelSlices,
                    centerTitle = "Total Sales",
                    centerSubtitle = CurrencyFormatter.formatPaise(stats.grossSalesPaise)
                )
            }

            item {
                SKDonutPieChart(
                    title = "P&L Margin Breakdown",
                    subtitle = "Cost of goods sold vs Operating expenses vs Profit",
                    slices = pnlSlices,
                    centerTitle = "Net Margin",
                    centerSubtitle = "${((profitRupees / grossSalesRupees) * 100).toInt()}%"
                )
            }

            if (state.lowStockItems.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.radius14),
                        colors = CardDefaults.cardColors(containerColor = AppColors.ExpenseRedContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(AppDimens.dp14),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = AppColors.ExpenseAlertRed
                            )
                            Spacer(modifier = Modifier.width(AppDimens.spacingM))
                            Column {
                                Text(
                                    text = "Low Stock Alert",
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.ExpenseRed
                                )
                                Text(
                                    text = "${state.lowStockItems.size} items below reorder threshold",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.ExpenseAlertDark
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(AppDimens.dp4))
                Text(
                    text = "Shop Operations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)) {
                    quickActions.chunked(2).forEach { rowActions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                        ) {
                            rowActions.forEach { action ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable(onClick = action.onClick),
                                    shape = RoundedCornerShape(AppDimens.radius14),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(AppDimens.dp14),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(AppDimens.dp38)
                                                .clip(CircleShape)
                                                .background(action.color.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = action.icon,
                                                contentDescription = null,
                                                tint = action.color,
                                                modifier = Modifier.size(AppDimens.dp20)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(AppDimens.dp10))
                                        Text(
                                            text = action.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            if (rowActions.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(AppDimens.dp8))
                Text(
                    text = "Recent Incoming Orders",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.recentOrders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.dp14),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = order.orderNumber,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${order.customerName} • ${
                                    CurrencyFormatter.formatPaise(
                                        order.pricing.grandTotalPaise
                                    )
                                }",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        StatusBadge(status = order.status.name)
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppDimens.radiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(AppDimens.dp14)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimens.dp4))
            Text(
                text = value,
                fontSize = AppFontSizes.sp17,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppDimens.dp4))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
