package com.skylink.sksweetsandbakes.presentation.orders.screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.skylink.sksweetsandbakes.core.designsystem.EmptyState
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.DateUtils
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import com.skylink.sksweetsandbakes.presentation.cart.screen.BillRow
import com.skylink.sksweetsandbakes.presentation.orders.viewmodel.OrdersViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import com.skylink.sksweetsandbakes.ui.theme.adaptiveContentWidth

@Composable
fun OrderHistoryScreen(
    viewModel: OrdersViewModel,
    onOrderClick: (orderId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SKSweetsTopBar(title = "My Orders", onBackClick = onBackClick) }
    ) { padding ->
        if (state.isLoadingOrders && state.orders.isEmpty()) {
            LoadingScreen()
        } else if (state.orders.isEmpty()) {
            EmptyState(
                title = "No Past Orders",
                subtitle = "When you place an order for sweets, it will appear here.",
                icon = Icons.Default.ShoppingBag
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .adaptiveContentWidth(AppDimens.dp640),
                contentPadding = PaddingValues(AppDimens.dp16),
                verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
            ) {
                items(state.orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOrderClick(order.orderId) },
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
                                    text = order.orderNumber,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                StatusBadge(status = order.status.name)
                            }
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            Text(
                                text = DateUtils.formatDateTime(order.createdAt),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp10))

                            Text(
                                text = order.items.joinToString(", ") { "${it.productName} (${it.selectedQuantity}${it.baseUnit})" },
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp10))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total: ${CurrencyFormatter.formatPaise(order.pricing.grandTotalPaise)}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "View Details >",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderDetailScreen(
    orderId: String,
    viewModel: OrdersViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        viewModel.loadOrderDetail(orderId)
    }

    val state by viewModel.uiState.collectAsState()
    val cleanId = orderId.trim()
    val order = state.currentOrder ?: state.orders.find {
        it.orderId.equals(cleanId, ignoreCase = true) ||
                it.orderNumber.equals(cleanId, ignoreCase = true) ||
                cleanId.contains(it.orderId, ignoreCase = true) ||
                it.orderId.contains(cleanId, ignoreCase = true)
    } ?: state.orders.firstOrNull()
    var showCancelDialog by remember { mutableStateOf(false) }

    if (state.isLoadingDetail && order == null) {
        LoadingScreen("Loading order...")
        return
    }

    if (order == null && !state.isLoadingDetail && !state.isLoadingOrders) {
        Scaffold(
            topBar = { SKSweetsTopBar(title = "Order Details", onBackClick = onBackClick) }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = "Order Not Found",
                    subtitle = state.errorMessage
                        ?: "We could not find the details for this order.",
                    actionButtonText = "Back to Orders",
                    onActionClick = onBackClick
                )
            }
        }
        return
    }

    if (order == null) {
        LoadingScreen("Loading order...")
        return
    }

    Scaffold(
        topBar = {
            SKSweetsTopBar(
                title = "Order #${order.orderNumber}",
                onBackClick = onBackClick
            )
        },
        modifier = Modifier.navigationBarsPadding()
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .adaptiveContentWidth(AppDimens.dp640),
            contentPadding = PaddingValues(AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            // Live Status Stepper Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                text = "Order Progress",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            StatusBadge(status = order.status.name)
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp16))

                        OrderTrackingStepper(currentStatus = order.status)
                    }
                }
            }

            // Items Purchased Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Items in this Order",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp10))

                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = AppDimens.dp4),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.productName} × ${item.selectedQuantity} ${item.baseUnit}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = CurrencyFormatter.formatPaise(item.lineTotalPaise),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Delivery Details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Fulfillment Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Text(
                            text = "Mode: ${order.fulfillmentType.name}",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (order.deliveryAddress != null) {
                            val addr = order.deliveryAddress
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            Text(
                                text = "Deliver To: ${addr.recipientName} (${addr.phoneNumber})",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${addr.addressLine1}, ${addr.city} - ${addr.pincode}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Payment & Bill Details + Download Invoice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Payment & Invoice Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp10))
                        BillRow(
                            "Items Subtotal",
                            CurrencyFormatter.formatPaise(order.pricing.subtotalPaise)
                        )
                        if (order.pricing.discountPaise > 0) {
                            BillRow(
                                "Discount",
                                "-${CurrencyFormatter.formatPaise(order.pricing.discountPaise)}",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        BillRow(
                            "Packaging Fee",
                            CurrencyFormatter.formatPaise(order.pricing.packagingFeePaise)
                        )
                        BillRow(
                            "Delivery Fee",
                            CurrencyFormatter.formatPaise(order.pricing.deliveryFeePaise)
                        )
                        BillRow(
                            "Taxes (GST 5%)",
                            CurrencyFormatter.formatPaise(order.pricing.taxPaise)
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp6))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(AppDimens.dp6))
                        BillRow(
                            "Total Amount Paid",
                            CurrencyFormatter.formatPaise(order.pricing.grandTotalPaise),
                            isBold = true
                        )

                        // Download Invoice Button
                        Spacer(modifier = Modifier.height(AppDimens.dp14))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(AppDimens.dp14))

                        Button(
                            onClick = { viewModel.downloadInvoice(context, order) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppDimens.dp48),
                            shape = RoundedCornerShape(AppDimens.dp12),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            enabled = !state.isGeneratingInvoice
                        ) {
                            if (state.isGeneratingInvoice) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(AppDimens.dp20),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = AppDimens.dp2
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                Text("Generating Invoice PDF...")
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download Invoice",
                                    modifier = Modifier.size(AppDimens.dp20)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                Text(
                                    text = "Download Invoice (PDF)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Order Cancellation Action
            if (order.status == OrderStatus.CONFIRMED || order.status == OrderStatus.PENDING_PAYMENT) {
                item {
                    OutlinedButton(
                        onClick = { showCancelDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppDimens.dp48),
                        shape = RoundedCornerShape(AppDimens.dp12),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel Order")
                    }
                }
            }
        }

        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
                shape = RoundedCornerShape(AppDimens.dp24),
                title = { Text("Cancel Order?", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to cancel this order? Any payment made will be refunded within 24-48 hours.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.cancelOrder(order.orderId, "Customer requested cancellation")
                            showCancelDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Confirm Cancellation")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelDialog = false }) {
                        Text("Keep Order")
                    }
                }
            )
        }
    }
}

@Composable
fun OrderTrackingStepper(currentStatus: OrderStatus) {
    val steps = listOf(
        OrderStatus.CONFIRMED to "Confirmed",
        OrderStatus.PREPARING to "Preparing",
        OrderStatus.OUT_FOR_DELIVERY to "Out for Delivery",
        OrderStatus.COMPLETED to "Delivered"
    )

    val currentStepIndex = when (currentStatus) {
        OrderStatus.PENDING_PAYMENT -> -1
        OrderStatus.CONFIRMED -> 0
        OrderStatus.PREPARING -> 1
        OrderStatus.READY_FOR_PICKUP, OrderStatus.OUT_FOR_DELIVERY -> 2
        OrderStatus.COMPLETED -> 3
        OrderStatus.CANCELLED, OrderStatus.REFUND_PENDING, OrderStatus.REFUNDED -> -2
    }

    if (currentStepIndex == -2) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppDimens.dp8))
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(AppDimens.dp12),
            contentAlignment = Alignment.Center
        ) {
            val statusDisplay = currentStatus.name.replace('_', ' ')
            Text(
                text = "Order Status: $statusDisplay",
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, pair ->
            val isCompleted = index <= currentStepIndex
            val isCurrent = index == currentStepIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(AppDimens.dp32)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> MaterialTheme.colorScheme.primary
                                isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(AppDimens.dp16)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimens.dp6))

                Text(
                    text = pair.second,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
