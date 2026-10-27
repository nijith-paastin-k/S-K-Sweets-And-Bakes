package com.skylink.sksweetsandbakes.presentation.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.skylink.sksweetsandbakes.core.designsystem.BarChartItem
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.PieChartSlice
import com.skylink.sksweetsandbakes.core.designsystem.SKBarChart
import com.skylink.sksweetsandbakes.core.designsystem.SKDonutPieChart
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.presentation.cart.screen.BillRow
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.AnalyticsViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val statement = state.statement

    val periods = listOf("Today", "Yesterday", "Last 7 Days", "Last 30 Days")

    // Dynamic Chart Data based on current financial statement
    val cogsRupees = statement.cogsPaise / 100.0
    val opexRupees = statement.operatingExpensesPaise / 100.0
    val profitRupees = (statement.operatingProfitPaise.coerceAtLeast(0L)) / 100.0
    val grossSalesRupees = (statement.grossSalesPaise.coerceAtLeast(1L)) / 100.0

    val pnlSlices = remember(statement) {
        if (statement.grossSalesPaise > 0L) {
            listOf(
                PieChartSlice(
                    "COGS (Raw Goods)",
                    cogsRupees,
                    AppColors.ExpenseRed,
                    CurrencyFormatter.formatPaise(statement.cogsPaise)
                ),
                PieChartSlice(
                    "Operating Expenses",
                    opexRupees,
                    AppColors.CostOrange,
                    CurrencyFormatter.formatPaise(statement.operatingExpensesPaise)
                ),
                PieChartSlice(
                    "Net Profit",
                    profitRupees,
                    AppColors.ProfitGreen,
                    CurrencyFormatter.formatPaise(statement.operatingProfitPaise)
                )
            )
        } else {
            emptyList()
        }
    }

    val channelSlices = remember(statement) {
        val totalOrders = statement.posSalesCount + statement.onlineSalesCount
        if (totalOrders > 0) {
            listOf(
                PieChartSlice(
                    "Counter POS",
                    statement.posSalesCount.toDouble(),
                    AppColors.MaroonPrimary,
                    "${statement.posSalesCount} bills"
                ),
                PieChartSlice(
                    "Online Deliveries",
                    statement.onlineSalesCount.toDouble(),
                    AppColors.GoldSecondary,
                    "${statement.onlineSalesCount} orders"
                )
            )
        } else {
            emptyList()
        }
    }

    val financialComparisonBars = remember(statement) {
        listOf(
            BarChartItem(
                "Gross",
                (statement.grossSalesPaise / 100.0).coerceAtLeast(0.0),
                CurrencyFormatter.formatPaise(statement.grossSalesPaise),
                AppColors.MaroonPrimary,
                isHighlighted = true
            ),
            BarChartItem(
                "Net Rev",
                (statement.netSalesPaise / 100.0).coerceAtLeast(0.0),
                CurrencyFormatter.formatPaise(statement.netSalesPaise),
                AppColors.RevenueBlue
            ),
            BarChartItem(
                "COGS",
                (statement.cogsPaise / 100.0).coerceAtLeast(0.0),
                CurrencyFormatter.formatPaise(statement.cogsPaise),
                AppColors.ExpenseRed
            ),
            BarChartItem(
                "OpEx",
                (statement.operatingExpensesPaise / 100.0).coerceAtLeast(0.0),
                CurrencyFormatter.formatPaise(statement.operatingExpensesPaise),
                AppColors.CostOrange
            ),
            BarChartItem(
                "Profit",
                (statement.operatingProfitPaise / 100.0).coerceAtLeast(0.0),
                CurrencyFormatter.formatPaise(statement.operatingProfitPaise),
                AppColors.ProfitGreen
            )
        )
    }

    Scaffold(
        topBar = {
            SKSweetsTopBar(
                title = "Financial Analytics & Profit",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        if (state.isLoading) {
            LoadingScreen("Calculating financial statement...")
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.spacingMd),
            verticalArrangement = Arrangement.spacedBy(AppDimens.spacingMd)
        ) {
            // Period Filters
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingSm)) {
                    items(periods) { p ->
                        FilterChip(
                            selected = state.selectedPeriod == p,
                            onClick = { viewModel.onPeriodSelected(p) },
                            label = { Text(p) }
                        )
                    }
                }
            }

            // Top Gross & Operating Profit KPIs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
                ) {
                    KpiCard(
                        title = "Gross Revenue",
                        value = CurrencyFormatter.formatPaise(statement.grossSalesPaise),
                        subtitle = "POS & Online Orders",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary
                    )
                    KpiCard(
                        title = "Net Operating Profit",
                        value = CurrencyFormatter.formatPaise(statement.operatingProfitPaise),
                        subtitle = "After COGS & Expenses",
                        modifier = Modifier.weight(1f),
                        color = if (statement.operatingProfitPaise >= 0) AppColors.ProfitGreen else AppColors.ExpenseRed
                    )
                }
            }

            // Financial Breakdown (Bar Chart)
            item {
                SKBarChart(
                    title = "${state.selectedPeriod} Financial Distribution",
                    subtitle = "Comparison of revenue, cost of goods, overheads, and profit",
                    items = financialComparisonBars
                )
            }

            // P&L Cost Structure (Donut / Pie Chart)
            item {
                SKDonutPieChart(
                    title = "Revenue Allocation & Margin",
                    subtitle = "COGS vs Operating Expenses vs Net Margin",
                    slices = pnlSlices,
                    centerTitle = "Net Margin",
                    centerSubtitle = if (grossSalesRupees > 0) "${((profitRupees / grossSalesRupees) * 100).toInt()}%" else "0%"
                )
            }

            // Channel Performance (Donut / Pie Chart)
            item {
                SKDonutPieChart(
                    title = "Order Channels Distribution",
                    subtitle = "Counter POS walk-ins vs Online orders",
                    slices = channelSlices,
                    centerTitle = "Total Orders",
                    centerSubtitle = "${statement.totalOrdersCount}"
                )
            }

            // Channels Breakdown Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.radiusLarge),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.spacingMd)) {
                        Text(
                            text = "Channel Performance Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp10))
                        BillRow(
                            "Counter Walk-in Sales (POS)",
                            "${statement.posSalesCount} transactions"
                        )
                        BillRow(
                            "Online Customer Orders",
                            "${statement.onlineSalesCount} deliveries/pickups"
                        )
                        BillRow(
                            "Average Order Value (AOV)",
                            CurrencyFormatter.formatPaise(statement.averageOrderValuePaise),
                            isBold = true
                        )
                    }
                }
            }

            // Profit & Loss Statement (P&L) Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.radiusLarge),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.spacingMd)) {
                        Text(
                            text = "Profit & Loss Statement (Authoritative)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.spacingM))

                        BillRow(
                            "Gross Sales",
                            CurrencyFormatter.formatPaise(statement.grossSalesPaise)
                        )
                        if (statement.discountsPaise > 0) {
                            BillRow(
                                "Less: Discounts & Coupons",
                                "-${CurrencyFormatter.formatPaise(statement.discountsPaise)}",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        BillRow(
                            "Net Sales Revenue",
                            CurrencyFormatter.formatPaise(statement.netSalesPaise),
                            isBold = true
                        )

                        Spacer(modifier = Modifier.height(AppDimens.dp6))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(AppDimens.dp6))

                        BillRow(
                            "Cost of Goods Sold (COGS)",
                            "-${CurrencyFormatter.formatPaise(statement.cogsPaise)}",
                            color = AppColors.ExpenseRed
                        )
                        BillRow(
                            "Gross Profit",
                            CurrencyFormatter.formatPaise(statement.grossProfitPaise),
                            isBold = true,
                            color = AppColors.ProfitGreen
                        )

                        Spacer(modifier = Modifier.height(AppDimens.dp6))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(AppDimens.dp6))

                        BillRow(
                            "Operating Expenses (Rent, Power, Wages)",
                            "-${CurrencyFormatter.formatPaise(statement.operatingExpensesPaise)}",
                            color = AppColors.ExpenseRed
                        )
                        BillRow(
                            "Estimated Operating Profit",
                            CurrencyFormatter.formatPaise(statement.operatingProfitPaise),
                            isBold = true,
                            color = AppColors.ProfitGreen
                        )
                    }
                }
            }
        }
    }
}
