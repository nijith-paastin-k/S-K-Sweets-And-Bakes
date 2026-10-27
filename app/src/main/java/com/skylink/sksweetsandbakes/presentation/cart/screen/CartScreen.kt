package com.skylink.sksweetsandbakes.presentation.cart.screen


import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.skylink.sksweetsandbakes.R
import com.skylink.sksweetsandbakes.core.designsystem.EmptyState
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.presentation.cart.viewmodel.CartViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onCheckoutClick: () -> Unit,
    onExploreSweetsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val cart = state.cart

    Scaffold(
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = { SKSweetsTopBar(title = "Shopping Cart", onBackClick = onBackClick) },
        bottomBar = {
            if (!cart.isEmpty) {
                Surface(
                    tonalElevation = AppDimens.dp8,
                    shadowElevation = AppDimens.dp8,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp12)
                            .padding(bottom = AppDimens.dp8)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Payable",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = CurrencyFormatter.formatPaise(cart.grandTotalPaise),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Button(
                                onClick = onCheckoutClick,
                                shape = RoundedCornerShape(AppDimens.dp12),
                                modifier = Modifier
                                    .height(AppDimens.dp48)
                                    .width(AppDimens.dp170)
                            ) {
                                Text("Proceed to Checkout", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (cart.isEmpty) {
            EmptyState(
                title = "Your Cart is Empty",
                subtitle = "Browse our mouthwatering sweets, gift boxes, and fresh bakes.",
                icon = Icons.Default.ShoppingCart,
                drawableRes = R.drawable.img_maroon_gift_scroll,
                actionButtonText = "Explore Sweets",
                onActionClick = onExploreSweetsClick
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(AppDimens.dp16),
                verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
            ) {
                items(cart.items) { item ->
                    CartItemRow(
                        item = item,
                        onIncrement = {
                            val step =
                                if (item.measurementType == MeasurementType.WEIGHT) 250 else 1
                            viewModel.onQuantityChanged(
                                item.productId,
                                item.selectedQuantity + step
                            )
                        },
                        onDecrement = {
                            val step =
                                if (item.measurementType == MeasurementType.WEIGHT) 250 else 1
                            viewModel.onQuantityChanged(
                                item.productId,
                                item.selectedQuantity - step
                            )
                        },
                        onRemove = { viewModel.onRemoveItem(item.productId) }
                    )
                }

                // Coupon Code Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp14),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(AppDimens.dp14)) {
                            Text(
                                text = "Offers & Coupons",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp8))

                            if (cart.appliedCouponCode != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Coupon Applied: ${cart.appliedCouponCode}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "You saved ${CurrencyFormatter.formatPaise(cart.discountPaise)}!",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.removeCoupon() }) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove")
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = state.couponInput,
                                        onValueChange = { viewModel.onCouponInputChanged(it) },
                                        placeholder = { Text("Enter code (e.g. FESTIVE10)") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(AppDimens.dp10)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                                    Button(
                                        onClick = { viewModel.applyCoupon() },
                                        shape = RoundedCornerShape(AppDimens.dp10)
                                    ) {
                                        Text("Apply")
                                    }
                                }
                            }

                            if (state.couponMessage != null) {
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    text = state.couponMessage ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (state.isCouponError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                // Bill Summary Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp14),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(AppDimens.dp16)) {
                            Text(
                                text = "Bill Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp12))

                            BillRow(
                                label = "Items Subtotal",
                                value = CurrencyFormatter.formatPaise(cart.subtotalPaise)
                            )

                            if (cart.discountPaise > 0) {
                                BillRow(
                                    label = "Coupon Discount",
                                    value = "-${CurrencyFormatter.formatPaise(cart.discountPaise)}",
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            BillRow(
                                label = "Packaging & Handling",
                                value = CurrencyFormatter.formatPaise(cart.packagingFeePaise)
                            )
                            BillRow(
                                label = "Delivery Fee",
                                value = if (cart.deliveryFeePaise == 0L) "FREE" else CurrencyFormatter.formatPaise(
                                    cart.deliveryFeePaise
                                )
                            )
                            BillRow(
                                label = "Taxes (GST 5%)",
                                value = CurrencyFormatter.formatPaise(cart.taxPaise)
                            )

                            Spacer(modifier = Modifier.height(AppDimens.dp8))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(AppDimens.dp8))

                            BillRow(
                                label = "Grand Total",
                                value = CurrencyFormatter.formatPaise(cart.grandTotalPaise),
                                isBold = true
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.dp14),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(AppDimens.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.dp72)
                    .clip(RoundedCornerShape(AppDimens.dp10))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = item.productImageUrl,
                    contentDescription = item.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(AppDimens.dp12))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                val qtyDisplay = if (item.measurementType == MeasurementType.WEIGHT) {
                    if (item.selectedQuantity >= 1000) "${item.selectedQuantity / 1000.0} kg" else "${item.selectedQuantity} g"
                } else {
                    "${item.selectedQuantity} ${item.baseUnit}"
                }

                Text(
                    text = "Selected: $qtyDisplay",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(AppDimens.dp4))
                Text(
                    text = CurrencyFormatter.formatPaise(item.lineTotalPaise),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(AppDimens.dp8)
                    )
                    .padding(horizontal = AppDimens.dp4, vertical = AppDimens.dp2)
            ) {
                IconButton(
                    onClick = onDecrement,
                    modifier = Modifier.size(AppDimens.dp32)
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(AppDimens.dp16)
                    )
                }
                Spacer(modifier = Modifier.width(AppDimens.dp4))
                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier.size(AppDimens.dp32)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(AppDimens.dp16)
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun BillRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    color: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimens.dp3),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (color != Color.Unspecified) color else if (isBold) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}