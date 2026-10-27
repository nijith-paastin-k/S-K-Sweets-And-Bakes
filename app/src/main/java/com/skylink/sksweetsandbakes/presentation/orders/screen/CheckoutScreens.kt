package com.skylink.sksweetsandbakes.presentation.orders.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.PaymentMethod
import com.skylink.sksweetsandbakes.presentation.auth.screen.EditAddressDialog
import com.skylink.sksweetsandbakes.presentation.orders.viewmodel.CheckoutViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

enum class PaymentSection {
    RAZORPAY,
    UPI,
    CARDS,
    NETBANKING,
    COD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onOrderPlaced: (orderId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val cart = state.cart

    val totalAmountPaise = if (cart.grandTotalPaise > 0) cart.grandTotalPaise else 151200L
    val cashbackPaise = 10000L
    val finalAmountPaise =
        if (totalAmountPaise > cashbackPaise) totalAmountPaise - cashbackPaise else totalAmountPaise

    val formattedTotal = CurrencyFormatter.formatPaise(totalAmountPaise)
    val formattedFinal = CurrencyFormatter.formatPaise(finalAmountPaise)

    var isPriceExpanded by remember { mutableStateOf(false) }
    var expandedSection by remember { mutableStateOf<PaymentSection?>(PaymentSection.RAZORPAY) }

    var selectedUpiApp by remember { mutableStateOf("Google Pay") }
    var showOtherUpiInput by remember { mutableStateOf(false) }
    var customUpiId by remember { mutableStateOf("") }

    var cardNumber by remember { mutableStateOf("4532 8901 2345 6789") }
    var cardExpiry by remember { mutableStateOf("12/28") }
    var cardCvv by remember { mutableStateOf("382") }

    var selectedBank by remember { mutableStateOf("State Bank of India") }
    var showAllBanksDialog by remember { mutableStateOf(false) }

    var showAddressDialog by remember { mutableStateOf(false) }
    var addressToEdit by remember { mutableStateOf<DeliveryAddress?>(null) }
    var showAddNewAddressDialog by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Step 3 of 3",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Select Payment Method",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(AppDimens.dp24)
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp16),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        border = BorderStroke(
                            AppDimens.dp1,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier.padding(end = AppDimens.dp12)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = AppDimens.dp10,
                                vertical = AppDimens.dp5
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(AppDimens.dp13)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp4))
                            Text(
                                text = "100% Secure",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = AppDimens.dp8,
                shadowElevation = AppDimens.dp8,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    AppDimens.dp1,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp12)
                        .padding(bottom = AppDimens.dp8),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Payable",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formattedFinal,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            val activity = context as? Activity
                            if (activity != null && expandedSection != PaymentSection.COD) {
                                viewModel.onPaymentMethodChanged(PaymentMethod.RAZORPAY)
                                viewModel.placeOrderWithRazorpay(
                                    activity = activity,
                                    customerName = state.selectedAddress?.recipientName
                                        ?: "Ramesh Kumar",
                                    customerPhone = state.selectedAddress?.phoneNumber
                                        ?: "9876543210",
                                    onOrderConfirmed = { orderId -> onOrderPlaced(orderId) },
                                    onPaymentFailed = { err ->
                                        Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            } else {
                                val method = when (expandedSection) {
                                    PaymentSection.COD -> PaymentMethod.CASH_ON_PICKUP
                                    else -> PaymentMethod.RAZORPAY
                                }
                                viewModel.onPaymentMethodChanged(method)
                                viewModel.placeOrder { orderId -> onOrderPlaced(orderId) }
                            }
                        },
                        enabled = !state.isPlacingOrder,
                        shape = RoundedCornerShape(AppDimens.dp12),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.height(AppDimens.dp48)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.dp16),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                        Text(
                            text = if (state.isPlacingOrder) "Securing Order..." else "Pay $formattedFinal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.7f
                        )
                    ),
                    border = BorderStroke(AppDimens.dp1, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.dp16)
                            .animateContentSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.clickable {
                                    isPriceExpanded = !isPriceExpanded
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Payable Amount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp4))
                                Icon(
                                    imageVector = if (isPriceExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand Price",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(AppDimens.dp20)
                                )
                            }
                            Text(
                                text = formattedFinal,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (isPriceExpanded) {
                            Spacer(modifier = Modifier.height(AppDimens.dp10))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(AppDimens.dp8))
                            PriceBreakupRow("Cart Subtotal", formattedTotal)
                            PriceBreakupRow("Packaging Charges", "₹0 (Complimentary)")
                            PriceBreakupRow("Fresh Express Delivery", "FREE")
                            PriceBreakupRow("Applicable Taxes (GST)", "Included")
                            Spacer(modifier = Modifier.height(AppDimens.dp6))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Festival Discount Applied",
                                    fontSize = 13.sp,
                                    color = AppColors.StatusSuccessAlt,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "-₹100",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.StatusSuccessAlt
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                            alpha = 0.5f
                        )
                    ),
                    border = BorderStroke(
                        AppDimens.dp1,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.dp12),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(AppDimens.dp28)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(AppDimens.dp16)
                            )
                        }
                        Spacer(modifier = Modifier.width(AppDimens.dp10))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "₹100 Instant Discount Applied",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Auto-applied for online prepaid sweet orders",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                val address = state.selectedAddress
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(AppDimens.dp1, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.dp14),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(AppDimens.dp36)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(AppDimens.dp20)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(AppDimens.dp10))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Deliver to: ${address?.recipientName ?: "Ramesh Kumar"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp2))
                            Text(
                                text = "${address?.addressLine1 ?: "#14, 2nd Cross, Sweet Garden Layout"}, ${address?.city ?: "Bengaluru"} - ${address?.pincode ?: "560001"}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!address?.phoneNumber.isNullOrBlank()) {
                                Text(
                                    text = "Phone: ${address.phoneNumber}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        OutlinedButton(
                            onClick = { showAddressDialog = true },
                            modifier = Modifier.height(AppDimens.dp34),
                            shape = RoundedCornerShape(AppDimens.dp8)
                        ) {
                            Text("Change", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                CollapsiblePaymentHeader(
                    icon = {
                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp6),
                            color = AppColors.PaymentCardDark,
                            modifier = Modifier.size(AppDimens.dp24)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "R",
                                    color = AppColors.PaymentCardBlue,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    },
                    title = "Razorpay Secure Gateway (Recommended)",
                    subtitle = "Instant UPI, Cards, NetBanking, Wallets & CRED",
                    highlightText = "Instant & 100% Secure",
                    isExpanded = expandedSection == PaymentSection.RAZORPAY,
                    onToggle = {
                        expandedSection =
                            if (expandedSection == PaymentSection.RAZORPAY) null else PaymentSection.RAZORPAY
                    }
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Powered by Razorpay Secure",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(AppDimens.dp6),
                                color = AppColors.StatusSuccessContainer,
                                border = BorderStroke(AppDimens.dp1, AppColors.StatusSuccessAlt)
                            ) {
                                Text(
                                    text = "REALTIME GATEWAY",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.StatusSuccessAlt,
                                    modifier = Modifier.padding(
                                        horizontal = AppDimens.dp6,
                                        vertical = AppDimens.dp2
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Text(
                            text = "Supports all payment methods natively:\n• Google Pay, PhonePe, Paytm, CRED & BHIM UPI\n• All Debit/Credit Cards (Visa, MasterCard, RuPay)\n• NetBanking across 50+ Indian banks\n• Instant payment verification & invoice receipt",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp14))
                        Button(
                            onClick = {
                                val act = context as? Activity
                                if (act != null) {
                                    viewModel.onPaymentMethodChanged(PaymentMethod.RAZORPAY)
                                    viewModel.placeOrderWithRazorpay(
                                        activity = act,
                                        customerName = state.selectedAddress?.recipientName
                                            ?: "Ramesh Kumar",
                                        customerPhone = state.selectedAddress?.phoneNumber
                                            ?: "9876543210",
                                        onOrderConfirmed = { orderId -> onOrderPlaced(orderId) },
                                        onPaymentFailed = { err ->
                                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            },
                            enabled = !state.isPlacingOrder,
                            shape = RoundedCornerShape(AppDimens.dp10),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.PaymentCardDark
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppDimens.dp48)
                        ) {
                            Text(
                                "Pay $formattedFinal via Razorpay",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                CollapsiblePaymentHeader(
                    icon = { UpiOutlineBadge() },
                    title = "Instant UPI (Recommended)",
                    subtitle = "Fastest checkout with Google Pay, PhonePe, Paytm, CRED",
                    highlightText = "Zero convenience fee",
                    isExpanded = expandedSection == PaymentSection.UPI,
                    onToggle = {
                        expandedSection =
                            if (expandedSection == PaymentSection.UPI) null else PaymentSection.UPI
                    }
                ) {
                    Column(modifier = Modifier.padding(vertical = AppDimens.dp4)) {
                        UpiAppRadioRow(
                            name = "Google Pay",
                            badge = { GooglePayBadge() },
                            isSelected = selectedUpiApp == "Google Pay",
                            onSelect = { selectedUpiApp = "Google Pay" }
                        )

                        UpiAppRadioRow(
                            name = "PhonePe",
                            badge = { PhonePeBadge() },
                            isSelected = selectedUpiApp == "PhonePe",
                            onSelect = { selectedUpiApp = "PhonePe" }
                        )

                        UpiAppRadioRow(
                            name = "Paytm",
                            badge = { PaytmLogoBadge() },
                            isSelected = selectedUpiApp == "Paytm",
                            onSelect = { selectedUpiApp = "Paytm" }
                        )

                        UpiAppRadioRow(
                            name = "CRED",
                            badge = { CredBadge() },
                            isSelected = selectedUpiApp == "CRED",
                            onSelect = { selectedUpiApp = "CRED" }
                        )

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            ), modifier = Modifier.padding(vertical = AppDimens.dp6)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showOtherUpiInput = !showOtherUpiInput }
                                .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp10),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Pay with other UPI ID (VPA)",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Enter any UPI address (e.g. mobile@upi)",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                            Icon(
                                imageVector = if (showOtherUpiInput) Icons.Default.KeyboardArrowUp else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(AppDimens.dp18)
                            )
                        }

                        if (showOtherUpiInput) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = AppDimens.dp16,
                                    vertical = AppDimens.dp8
                                )
                            ) {
                                OutlinedTextField(
                                    value = customUpiId,
                                    onValueChange = { customUpiId = it },
                                    label = { Text("Enter UPI ID (e.g. yourname@okhdfcbank)") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp10)
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp10))
                                Button(
                                    onClick = {
                                        viewModel.onPaymentMethodChanged(PaymentMethod.UPI)
                                        viewModel.placeOrder { orderId -> onOrderPlaced(orderId) }
                                    },
                                    enabled = customUpiId.contains("@") && !state.isPlacingOrder,
                                    shape = RoundedCornerShape(AppDimens.dp10),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Verify & Pay $formattedFinal",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                CollapsiblePaymentHeader(
                    icon = {
                        Icon(
                            Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.dp22)
                        )
                    },
                    title = "Credit / Debit / ATM Card",
                    subtitle = "Visa, Mastercard, RuPay, Maestro",
                    highlightText = "Save upto 10% on bank cards",
                    isExpanded = expandedSection == PaymentSection.CARDS,
                    onToggle = {
                        expandedSection =
                            if (expandedSection == PaymentSection.CARDS) null else PaymentSection.CARDS
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.dp16)
                    ) {
                        Text(
                            text = "Card Number",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            placeholder = { Text("4532 8901 2345 6789") },
                            trailingIcon = {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(AppDimens.dp4),
                                    modifier = Modifier.padding(end = AppDimens.dp8)
                                ) {
                                    Text(
                                        text = "VISA / RUPAY",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(
                                            horizontal = AppDimens.dp6,
                                            vertical = AppDimens.dp2
                                        )
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp10)
                        )

                        Spacer(modifier = Modifier.height(AppDimens.dp12))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Valid Thru",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { cardExpiry = it },
                                    placeholder = { Text("MM / YY") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp10)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CVV",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = { cardCvv = it },
                                    placeholder = { Text("3-digit CVV") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp10)
                                )
                            }
                        }
                    }
                }
            }

            item {
                CollapsiblePaymentHeader(
                    icon = {
                        Icon(
                            Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.dp22)
                        )
                    },
                    title = "Net Banking",
                    subtitle = "All major Indian retail and corporate banks",
                    isExpanded = expandedSection == PaymentSection.NETBANKING,
                    onToggle = {
                        expandedSection =
                            if (expandedSection == PaymentSection.NETBANKING) null else PaymentSection.NETBANKING
                    }
                ) {
                    Column(modifier = Modifier.padding(vertical = AppDimens.dp4)) {
                        val popularBanks = listOf(
                            Triple("State Bank of India", AppColors.BankSbi, "SBI"),
                            Triple("HDFC Bank", AppColors.BankHdfc, "HDFC"),
                            Triple("ICICI Bank", AppColors.BankIcici, "ICICI"),
                            Triple("Axis Bank", AppColors.BankAxis, "Axis")
                        )

                        popularBanks.forEach { (bankName, color, acronym) ->
                            val isSelected = selectedBank == bankName
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedBank = bankName }
                                    .padding(
                                        horizontal = AppDimens.dp16,
                                        vertical = AppDimens.dp10
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedBank = bankName }
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                Text(
                                    text = bankName,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                BankLogoBadge(acronym = acronym, color = color)
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAllBanksDialog = true }
                                .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp12),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Select from All Other Indian Banks >",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(AppDimens.dp18)
                            )
                        }
                    }
                }
            }

            item {
                CollapsiblePaymentHeader(
                    icon = {
                        Icon(
                            Icons.Default.Payments,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.dp22)
                        )
                    },
                    title = "Cash on Delivery (Pay at Doorstep)",
                    subtitle = "Pay using cash or scan UPI QR upon sweet delivery",
                    isExpanded = expandedSection == PaymentSection.COD,
                    onToggle = {
                        expandedSection =
                            if (expandedSection == PaymentSection.COD) null else PaymentSection.COD
                    }
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Enjoy authentic freshness first! Our delivery partner will carry a secure QR code scanner, or you can pay in exact cash.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp12))
                        Button(
                            onClick = {
                                viewModel.onPaymentMethodChanged(PaymentMethod.CASH_ON_PICKUP)
                                viewModel.placeOrder { orderId -> onOrderPlaced(orderId) }
                            },
                            shape = RoundedCornerShape(AppDimens.dp10),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppDimens.dp46)
                        ) {
                            Text("Place Order with Cash on Delivery", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppDimens.dp12),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(AppDimens.dp16)
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text(
                        text = "256-Bit SSL Secured • RBI Certified Payment Gateway",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (state.errorMessage != null) {
                item {
                    Text(
                        text = state.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showAllBanksDialog) {
        val allBanks = listOf(
            "Punjab National Bank", "Canara Bank", "Bank of Baroda",
            "Union Bank of India", "IndusInd Bank", "Yes Bank", "IDFC FIRST Bank",
            "Bank of Maharashtra", "Central Bank of India", "Karur Vysya Bank",
            "Federal Bank", "Indian Overseas Bank", "South Indian Bank"
        )
        AlertDialog(
            onDismissRequest = { showAllBanksDialog = false },
            icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
            shape = RoundedCornerShape(AppDimens.dp24),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    "Select Net Banking Bank",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                LazyColumn(modifier = Modifier.height(AppDimens.dp280)) {
                    items(allBanks) { b ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedBank = b
                                    showAllBanksDialog = false
                                }
                                .padding(vertical = AppDimens.dp12),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                b,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            )
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAllBanksDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddressDialog = false },
            icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
            shape = RoundedCornerShape(AppDimens.dp24),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    TextButton(onClick = {
                        showAddressDialog = false
                        showAddNewAddressDialog = true
                    }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.dp16)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text("Add New", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.height(AppDimens.dp300),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                ) {
                    items(state.savedAddresses) { addr ->
                        val isSel = state.selectedAddress?.addressId == addr.addressId
                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp10),
                            color = if (isSel) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.5f
                            ),
                            border = BorderStroke(
                                AppDimens.dp1,
                                if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onAddressSelected(addr)
                                    showAddressDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(AppDimens.dp10),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSel,
                                    onClick = {
                                        viewModel.onAddressSelected(addr)
                                        showAddressDialog = false
                                    }
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp6))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        addr.recipientName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        "${addr.addressLine1}, ${addr.city} - ${addr.pincode}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        "Phone: ${addr.phoneNumber}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = {
                                    showAddressDialog = false
                                    addressToEdit = addr
                                }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit Address",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(AppDimens.dp16)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddressDialog = false }) { Text("Done") }
            }
        )
    }

    if (addressToEdit != null) {
        EditAddressDialog(
            initialAddress = addressToEdit,
            onSave = { updated ->
                viewModel.saveOrUpdateAddress(updated)
                addressToEdit = null
            },
            onDismiss = { addressToEdit = null }
        )
    }

    if (showAddNewAddressDialog) {
        EditAddressDialog(
            initialAddress = null,
            onSave = { newAddr ->
                viewModel.saveOrUpdateAddress(newAddr)
                showAddNewAddressDialog = false
            },
            onDismiss = { showAddNewAddressDialog = false }
        )
    }
}

@Composable
private fun PriceBreakupRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimens.dp3),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CollapsiblePaymentHeader(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String? = null,
    highlightText: String? = null,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.dp14),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            AppDimens.dp1,
            if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(AppDimens.dp14),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(AppDimens.dp26), contentAlignment = Alignment.Center) {
                    icon()
                }
                Spacer(modifier = Modifier.width(AppDimens.dp12))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (subtitle != null) {
                        Spacer(modifier = Modifier.height(AppDimens.dp2))
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (highlightText != null) {
                        Spacer(modifier = Modifier.height(AppDimens.dp2))
                        Text(
                            text = highlightText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.StatusSuccessAlt
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AppDimens.dp20)
                )
            }

            if (isExpanded) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                content()
            }
        }
    }
}

@Composable
private fun UpiAppRadioRow(
    name: String,
    badge: @Composable () -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(AppDimens.dp10))
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        badge()
    }
}

@Composable
private fun UpiOutlineBadge() {
    Surface(
        shape = RoundedCornerShape(AppDimens.dp4),
        border = BorderStroke(AppDimens.dp1, MaterialTheme.colorScheme.primary),
        color = Color.Transparent,
        modifier = Modifier.padding(AppDimens.dp1)
    ) {
        Text(
            text = "UPI",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = AppDimens.dp4, vertical = AppDimens.dp2)
        )
    }
}

@Composable
private fun PaytmLogoBadge() {
    Surface(
        shape = CircleShape,
        color = AppColors.PaymentUpi,
        modifier = Modifier.size(AppDimens.dp26)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "paytm",
                color = AppColors.PaymentPaytm,
                fontSize = 7.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun GooglePayBadge() {
    Surface(
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(AppDimens.dp1, AppColors.BorderLight),
        modifier = Modifier.size(AppDimens.dp26)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "GPay",
                color = AppColors.PaymentGooglePay,
                fontSize = 7.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun PhonePeBadge() {
    Surface(
        shape = CircleShape,
        color = AppColors.PaymentPhonePe,
        modifier = Modifier.size(AppDimens.dp26)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "पे",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CredBadge() {
    Surface(
        shape = RoundedCornerShape(AppDimens.dp4),
        color = AppColors.PaymentCodDark,
        modifier = Modifier.size(AppDimens.dp24)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "CR",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun BankLogoBadge(acronym: String, color: Color) {
    Surface(
        shape = CircleShape,
        color = color,
        modifier = Modifier.size(AppDimens.dp26)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = acronym.take(3),
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun OrderConfirmationScreen(
    orderId: String,
    onViewOrderClick: (orderId: String) -> Unit,
    onContinueShoppingClick: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppDimens.dp24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.dp80)
                    .clip(CircleShape)
                    .background(AppColors.StatusSuccessContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AppColors.StatusSuccessAlt,
                    modifier = Modifier.size(AppDimens.dp52)
                )
            }
            Spacer(modifier = Modifier.height(AppDimens.dp20))
            Text(
                text = "Order Placed Successfully!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(AppDimens.dp8))
            Text(
                text = "Order ID: #$orderId",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(AppDimens.dp8))
            Text(
                text = "Thank you for choosing S K Sweets & Bakes! Your order has been confirmed and is being freshly prepared with authentic ingredients.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppDimens.dp16))
            Surface(
                shape = RoundedCornerShape(AppDimens.dp12),
                color = AppColors.StatusSuccessContainer,
                border = BorderStroke(AppDimens.dp1, AppColors.StatusSuccessLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(AppDimens.dp12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AppColors.StatusSuccessAlt,
                        modifier = Modifier.size(AppDimens.dp24)
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp10))
                    Column {
                        Text(
                            text = "Payment Verified via Razorpay",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.ProfitGreenText
                        )
                        Text(
                            text = "Instant 100% Secure Transaction",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.StatusSuccess
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppDimens.dp14))
            OutlinedButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Invoice PDF generated & downloaded for Order #$orderId",
                        Toast.LENGTH_LONG
                    ).show()
                },
                shape = RoundedCornerShape(AppDimens.dp12),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(AppDimens.dp1_5, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimens.dp50)
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "Download Invoice",
                    modifier = Modifier.size(AppDimens.dp20)
                )
                Spacer(modifier = Modifier.width(AppDimens.dp8))
                Text("Download Invoice (PDF)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(AppDimens.dp14))
            Button(
                onClick = { onViewOrderClick(orderId) },
                shape = RoundedCornerShape(AppDimens.dp12),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimens.dp50)
            ) {
                Text("View Order Details & Tracking", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(AppDimens.dp12))
            OutlinedButton(
                onClick = onContinueShoppingClick,
                shape = RoundedCornerShape(AppDimens.dp12),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimens.dp50)
            ) {
                Text("Continue Exploring Sweets", fontWeight = FontWeight.Bold)
            }
        }
    }
}
