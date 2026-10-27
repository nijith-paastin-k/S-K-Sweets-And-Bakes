package com.skylink.sksweetsandbakes.presentation.dashboard.screen

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.DateUtils
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.PosViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    viewModel: PosViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            SKSweetsTopBar(
                title = "Counter POS • Walk-in Sales",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            // If on Tab 0 (Catalog) and cart has items, show a sticky checkout summary bar
            if (selectedTab == 0 && state.cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp8),
                    shape = RoundedCornerShape(AppDimens.dp18),
                    shadowElevation = AppDimens.dp8,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp10),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${state.cartItems.size} items in bill",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = CurrencyFormatter.formatPaise(state.totalPaise),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Button(
                            onClick = { selectedTab = 1 },
                            shape = RoundedCornerShape(AppDimens.dp12)
                        ) {
                            Text("View Bill & Pay >", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Navigation: Catalog vs Active Bill
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "1. Select Sweets (${state.products.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "2. Current Bill (${state.cartItems.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (state.cartItems.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                // Tab 0: Clean, Spacious Product Catalog Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.dp12),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp12),
                    contentPadding = PaddingValues(AppDimens.dp16),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.products) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val defaultQty =
                                        if (product.measurementType == MeasurementType.WEIGHT) 500 else product.minQuantity
                                    viewModel.addItemToSale(product, defaultQty)
                                },
                            shape = RoundedCornerShape(AppDimens.dp14),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(AppDimens.dp14)) {
                                Text(
                                    text = product.name,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp6))
                                Text(
                                    text = "${CurrencyFormatter.formatPaise(product.unitPricePaise)} / ${product.displayUnit}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    text = "Avail: ${product.availableStock} ${product.baseUnit}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp8))
                                Surface(
                                    shape = RoundedCornerShape(AppDimens.dp8),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "+ Tap to Add",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = AppDimens.dp6)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Tab 1: Full-Width Active Bill with Checkout Options
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppDimens.dp16),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (state.cartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp54),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp12))
                                Text(
                                    text = "Current Bill is Empty",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    text = "Switch to 'Select Sweets' tab to add items to bill",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp16))
                                OutlinedButton(onClick = { selectedTab = 0 }) {
                                    Text("Browse Sweets")
                                }
                            }
                        }
                    } else {
                        // Bill Items List
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                        ) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Bill Items (${state.cartItems.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    TextButton(onClick = {
                                        // Remove all items
                                        while (state.cartItems.isNotEmpty()) {
                                            viewModel.removeItemFromSale(0)
                                        }
                                    }) {
                                        Text("Clear All", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                                HorizontalDivider(modifier = Modifier.padding(bottom = AppDimens.dp8))
                            }

                            itemsIndexed(state.cartItems) { index, item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp12),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(AppDimens.dp12),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = item.name,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = "${item.quantity} ${item.unit}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Text(
                                            text = CurrencyFormatter.formatPaise(item.lineTotalPaise),
                                            fontWeight = FontWeight.ExtraBold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                                        IconButton(
                                            onClick = { viewModel.removeItemFromSale(index) },
                                            modifier = Modifier.size(AppDimens.dp32)
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Remove",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(AppDimens.dp18)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Section: Payment Mode & Complete Sale
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = AppDimens.dp12),
                            shape = RoundedCornerShape(AppDimens.dp16),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(AppDimens.dp16)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total Payable Amount",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatPaise(state.totalPaise),
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(AppDimens.dp12))

                                Text(
                                    text = "Select Payment Mode:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp8))

                                // Spacious 3-chip payment selector
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                                ) {
                                    listOf(
                                        Triple("UPI_QR", "UPI QR", Icons.Default.QrCode2),
                                        Triple("CASH", "Cash", Icons.Default.Payments),
                                        Triple("CARD", "Card", Icons.Default.CreditCard)
                                    ).forEach { (method, label, icon) ->
                                        val isSelected = state.selectedPaymentMethod == method
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { viewModel.setPaymentMethod(method) },
                                            label = {
                                                Text(
                                                    label,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    icon,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp16)
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(AppDimens.dp14))

                                Button(
                                    onClick = { viewModel.completeSale() },
                                    enabled = state.cartItems.isNotEmpty() && !state.isProcessing,
                                    shape = RoundedCornerShape(AppDimens.dp12),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(AppDimens.dp50)
                                ) {
                                    if (state.isProcessing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(AppDimens.dp20),
                                            color = Color.White
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Receipt,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp18)
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                                        Text(
                                            "Complete Sale & Print Receipt",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Receipt Dialog
        if (state.completedSale != null) {
            val sale = state.completedSale!!
            AlertDialog(
                onDismissRequest = { viewModel.dismissReceipt() },
                icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
                shape = RoundedCornerShape(AppDimens.dp24),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AppColors.ProfitGreen
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                        Text("Counter Receipt • S K Sweets")
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(AppDimens.dp12)
                    ) {
                        Text(
                            text = "S K SWEETS & BAKES",
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Main Market Road, Bengaluru",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Receipt: ${sale.saleNumber} • ${DateUtils.formatDateTime(sale.timestamp)}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Cashier: ${sale.cashierName} • Mode: ${sale.paymentMethod}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily.Monospace
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = AppDimens.dp8))

                        sale.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.name} (${item.quantity}${item.unit})",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = CurrencyFormatter.formatPaise(item.lineTotalPaise),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = AppDimens.dp8))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TOTAL PAID:",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = CurrencyFormatter.formatPaise(sale.totalPaise),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Text(
                            text = "Thank you! Please visit again.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = { viewModel.dismissReceipt() }) {
                        Text("New Sale")
                    }
                }
            )
        }
    }
}
