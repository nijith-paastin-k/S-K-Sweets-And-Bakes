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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.core.util.DateUtils
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.InventoryViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var adjustingItem by remember { mutableStateOf<Inventory?>(null) }
    var adjustmentQty by remember { mutableStateOf("") }
    var adjustmentReason by remember { mutableStateOf("") }
    var isAddingStock by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Shared Inventory Ledger", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SecondaryTabRow(
                selectedTabIndex = selectedTab
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Stock Overview") },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Movements Audit") },
                    icon = { Icon(Icons.Default.History, contentDescription = null) }
                )
            }

            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(AppDimens.dp16),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                ) {
                    items(state.inventoryList) { item ->
                        val isLowStock = item.onHandStock <= item.lowStockThreshold
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp14),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isLowStock) AppColors.ExpenseRedContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(AppDimens.dp14)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.productName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isLowStock) {
                                        StatusBadge(status = "LOW STOCK")
                                    }
                                }
                                Spacer(modifier = Modifier.height(AppDimens.dp8))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "On Hand",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            text = "${item.onHandStock} ${item.unit}",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Reserved (Online)",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            text = "${item.reservedStock} ${item.unit}",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "Available to Sell",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            text = "${item.availableStock} ${item.unit}",
                                            fontWeight = FontWeight.ExtraBold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(AppDimens.dp10))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            adjustingItem = item
                                            isAddingStock = true
                                            adjustmentQty = ""
                                            adjustmentReason = "Morning Batch Production"
                                        },
                                        shape = RoundedCornerShape(AppDimens.dp8)
                                    ) {
                                        Icon(
                                            Icons.Default.Tune,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                                        Text("Adjust Stock")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Movements Audit
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(AppDimens.dp16),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                ) {
                    items(state.movements) { mov ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp12),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(AppDimens.dp12)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = mov.productName,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${if (mov.quantityChange >= 0) "+" else ""}${mov.quantityChange} g",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (mov.quantityChange >= 0) AppColors.ProfitGreen else AppColors.ExpenseAlertRed
                                    )
                                }
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    text = "Type: ${mov.type.name} • ${mov.reason}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Ref: ${mov.referenceId} • ${DateUtils.formatDateTime(mov.timestamp)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stock Adjustment Dialog
        if (adjustingItem != null) {
            val item = adjustingItem!!
            AlertDialog(
                onDismissRequest = { adjustingItem = null },
                icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
                shape = RoundedCornerShape(AppDimens.dp24),
                title = { Text("Adjust Stock: ${item.productName}", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(
                            "Current On-Hand: ${item.onHandStock} ${item.unit}",
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp12))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { isAddingStock = true },
                                modifier = Modifier.weight(1f),
                                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isAddingStock) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                )
                            ) {
                                Text("+ Add Stock")
                            }
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            OutlinedButton(
                                onClick = { isAddingStock = false },
                                modifier = Modifier.weight(1f),
                                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (!isAddingStock) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                )
                            ) {
                                Text("- Deduct / Waste")
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp12))

                        OutlinedTextField(
                            value = adjustmentQty,
                            onValueChange = { adjustmentQty = it },
                            label = { Text("Quantity (${item.unit})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp8))

                        OutlinedTextField(
                            value = adjustmentReason,
                            onValueChange = { adjustmentReason = it },
                            label = { Text("Reason for Adjustment") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val qty = adjustmentQty.toIntOrNull() ?: 0
                            val finalQty = if (isAddingStock) qty else -qty
                            viewModel.adjustStock(item.productId, finalQty, adjustmentReason)
                            adjustingItem = null
                        }
                    ) {
                        Text("Apply Adjustment")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { adjustingItem = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
