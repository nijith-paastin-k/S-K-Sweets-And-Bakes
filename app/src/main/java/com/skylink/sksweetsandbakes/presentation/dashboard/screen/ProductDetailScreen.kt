package com.skylink.sksweetsandbakes.presentation.dashboard.screen


import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.skylink.sksweetsandbakes.core.designsystem.ErrorState
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.ImageUtils
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.ProductDetailViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    onCartClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showCustomWeightDialog by remember { mutableStateOf(false) }
    var customWeightInput by remember { mutableStateOf("") }
    var customWeightUnitKg by remember { mutableStateOf(true) } // true for kg, false for grams

    if (state.isLoading) {
        LoadingScreen()
        return
    }

    val product = state.product
    if (product == null) {
        ErrorState(message = state.errorMessage ?: "Product not found", onRetry = onBackClick)
        return
    }

    Scaffold(
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SKSweetsTopBar(
                title = product.name,
                onBackClick = onBackClick,
                actions = {
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Order ${product.name} from S K Sweets"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Enjoy authentic ${product.name} at S K Sweets & Bakes! sksweets://product/${product.productId}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share sweet"))
                        }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { viewModel.toggleWishlist() }) {
                        Icon(
                            imageVector = if (state.isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (state.isWishlisted) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = {
                                if (state.cartItemCount > 0) {
                                    Badge {
                                        Text(state.cartItemCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
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
                    // Selected info & Price header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Selected: ${
                                    formatWeightOrUnit(
                                        product.measurementType,
                                        state.selectedQuantity,
                                        product.baseUnit
                                    )
                                }",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = CurrencyFormatter.formatPaise(state.calculatedPricePaise),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp8),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = if (product.availableStock > 0) "Fresh In-Store" else "Limited Stock",
                                modifier = Modifier.padding(
                                    horizontal = AppDimens.dp8,
                                    vertical = AppDimens.dp4
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp10))

                    // Dual Action Buttons: "Add to Cart" + "Buy Now" (Direct Checkout)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Add to Cart (Stays on page with snackbar feedback)
                        OutlinedButton(
                            onClick = {
                                viewModel.addToCart {
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Added ${product.name} to cart!",
                                            actionLabel = "View Cart",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onCartClick()
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(AppDimens.dp12),
                            modifier = Modifier
                                .weight(1f)
                                .height(AppDimens.dp50),
                            border = BorderStroke(
                                AppDimens.dp1_5,
                                MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(AppDimens.dp18)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp6))
                            Text(
                                text = if (state.isAddedToCart) "In Cart" else "Add to Cart",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        // 2. Buy Now (Directly proceeds to checkout without moving to cart)
                        Button(
                            onClick = {
                                viewModel.addToCart {
                                    onBuyNowClick()
                                }
                            },
                            shape = RoundedCornerShape(AppDimens.dp12),
                            modifier = Modifier
                                .weight(1f)
                                .height(AppDimens.dp50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                modifier = Modifier.size(AppDimens.dp18)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp6))
                            Text(
                                text = "Buy Now",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = AppDimens.dp24)
        ) {
            // Hero Photo with Circular Royal Emblem Badge
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp260)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    AsyncImage(
                        model = ImageUtils.resolveModel(product.images.firstOrNull()),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                }
            }

            // Info Section
            item {
                Column(modifier = Modifier.padding(AppDimens.dp16)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(status = if (product.availableStock > 0) "IN STOCK" else "OUT OF STOCK")
                        Text(
                            text = "SKU: ${product.sku}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.dp8))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = product.categoryName,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp12))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Granular Quantity Selector (Weight: 250g, 500g, 1kg, 2.75kg, 3kg, custom stepper)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.dp16)
                ) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    val quantityHeader = when (product.measurementType) {
                        MeasurementType.WEIGHT -> "Select Weight (presets or custom up to 5 kg):"
                        MeasurementType.VOLUME -> "Select Volume:"
                        MeasurementType.PIECE -> "Select Piece Count:"
                        MeasurementType.FIXED_PACK -> "Select Number of Packs:"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = quantityHeader,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = AppDimens.dp8)
                        )
                        if (product.measurementType == MeasurementType.WEIGHT) {
                            OutlinedButton(
                                onClick = {
                                    customWeightInput = ""
                                    showCustomWeightDialog = true
                                },
                                contentPadding = PaddingValues(
                                    horizontal = AppDimens.dp10,
                                    vertical = AppDimens.dp4
                                ),
                                shape = RoundedCornerShape(AppDimens.dp8)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp14)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp4))
                                Text("Custom", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(AppDimens.dp10))

                    // Weight Presets including 2.75 kg, 3 kg, 3.5 kg, 4 kg, 5 kg
                    val quantityOptions: List<Pair<String, Int>> = when (product.measurementType) {
                        MeasurementType.WEIGHT -> listOf(
                            "250 g" to 250,
                            "500 g" to 500,
                            "750 g" to 750,
                            "1 kg" to 1000,
                            "1.25 kg" to 1250,
                            "1.5 kg" to 1500,
                            "1.75 kg" to 1750,
                            "2 kg" to 2000,
                            "2.25 kg" to 2250,
                            "2.5 kg" to 2500,
                            "2.75 kg" to 2750,
                            "3 kg" to 3000,
                            "3.5 kg" to 3500,
                            "4 kg" to 4000,
                            "5 kg" to 5000
                        )

                        MeasurementType.VOLUME -> listOf(
                            "250 ml" to 250,
                            "500 ml" to 500,
                            "1 L" to 1000,
                            "2 L" to 2000,
                            "3 L" to 3000
                        )

                        MeasurementType.PIECE -> listOf(
                            "2 Pieces" to 2,
                            "4 Pieces" to 4,
                            "6 Pieces" to 6,
                            "10 Pieces" to 10,
                            "15 Pieces" to 15,
                            "20 Pieces" to 20
                        )

                        MeasurementType.FIXED_PACK -> listOf(
                            "1 Pack" to 1,
                            "2 Packs" to 2,
                            "3 Packs" to 3,
                            "5 Packs" to 5,
                            "10 Packs" to 10
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp8),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        quantityOptions.forEach { (label, qty) ->
                            val isSelected = state.selectedQuantity == qty
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.onQuantitySelected(qty) },
                                label = {
                                    Text(
                                        text = label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                    }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    // Stepper for fast fine adjustment (+/- 250g or +/- 1 unit)
                    Spacer(modifier = Modifier.height(AppDimens.dp14))
                    Card(
                        shape = RoundedCornerShape(AppDimens.dp12),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.dp12),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Fine-tune Quantity:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = formatWeightOrUnit(
                                        product.measurementType,
                                        state.selectedQuantity,
                                        product.baseUnit
                                    ),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                            ) {
                                val step =
                                    if (product.measurementType == MeasurementType.WEIGHT) 250 else 1
                                IconButton(
                                    onClick = {
                                        val next = state.selectedQuantity - step
                                        if (next >= product.minQuantity) viewModel.onQuantitySelected(
                                            next
                                        )
                                    },
                                    enabled = state.selectedQuantity > product.minQuantity,
                                    modifier = Modifier
                                        .size(AppDimens.dp36)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }

                                Surface(
                                    shape = RoundedCornerShape(AppDimens.dp8),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = formatWeightOrUnit(
                                            product.measurementType,
                                            state.selectedQuantity,
                                            product.baseUnit
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = AppDimens.dp10,
                                            vertical = AppDimens.dp6
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val next = state.selectedQuantity + step
                                        if (next <= product.maxQuantity) viewModel.onQuantitySelected(
                                            next
                                        )
                                    },
                                    enabled = state.selectedQuantity < product.maxQuantity,
                                    modifier = Modifier
                                        .size(AppDimens.dp36)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))
                    HorizontalDivider()
                }
            }

            // Shelf Life & Ingredients Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimens.dp16),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Ingredients & Quality Promise",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Text(
                            text = "Ingredients: ${product.ingredients.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (product.allergens.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            Text(
                                text = "Allergens: Contains ${product.allergens.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Text(
                            text = "Shelf Life: ${product.shelfLifeDays} Days • ${product.storageInstructions}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Customer Reviews Section WITH IMAGE ATTACHMENTS
            item {
                Column(modifier = Modifier.padding(horizontal = AppDimens.dp16)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customer Reviews & Photos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${state.reviews.size} verified reviews",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.dp8))
                }
            }

            items(state.reviews) { review ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp6),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp14)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = review.customerName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(review.rating) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(AppDimens.dp15)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        Text(
                            text = review.comment,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Customer Photo Attachments
                        if (review.images.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(AppDimens.dp10))
                            Text(
                                text = "Photos by customer:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp6))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)) {
                                items(review.images) { photoUrl ->
                                    Box(
                                        modifier = Modifier
                                            .size(AppDimens.dp72)
                                            .clip(RoundedCornerShape(AppDimens.dp8))
                                            .background(MaterialTheme.colorScheme.surface)
                                    ) {
                                        AsyncImage(
                                            model = photoUrl,
                                            contentDescription = "Review sweet photo",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Weight Entry Dialog (Supports 2.75 KG, 3 KG, etc.)
        if (showCustomWeightDialog) {
            AlertDialog(
                onDismissRequest = { showCustomWeightDialog = false },
                icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
                shape = RoundedCornerShape(AppDimens.dp24),
                title = { Text("Enter Custom Sweet Weight", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(
                            text = "Enter any exact weight you need (e.g. 2.75 for 2.75 kg or 3000 for 3 kg):",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp12))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { customWeightUnitKg = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (customWeightUnitKg) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                )
                            ) {
                                Text(
                                    "In Kilograms (kg)",
                                    fontWeight = if (customWeightUnitKg) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            OutlinedButton(
                                onClick = { customWeightUnitKg = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (!customWeightUnitKg) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                )
                            ) {
                                Text(
                                    "In Grams (g)",
                                    fontWeight = if (!customWeightUnitKg) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp12))

                        OutlinedTextField(
                            value = customWeightInput,
                            onValueChange = { customWeightInput = it },
                            label = { Text(if (customWeightUnitKg) "Weight (e.g. 2.75 or 3)" else "Weight in Grams (e.g. 2750 or 3000)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val raw = customWeightInput.toDoubleOrNull()
                            if (raw != null && raw > 0) {
                                val grams = if (customWeightUnitKg) Math.round(raw * 1000.0)
                                    .toInt() else raw.toInt()
                                viewModel.onQuantitySelected(grams)
                                showCustomWeightDialog = false
                            }
                        }
                    ) {
                        Text("Apply Weight")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCustomWeightDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

private fun formatWeightOrUnit(
    measurementType: MeasurementType,
    quantity: Int,
    baseUnit: String
): String {
    return when (measurementType) {
        MeasurementType.WEIGHT -> {
            if (quantity >= 1000) {
                val kg = quantity / 1000.0
                if (kg % 1.0 == 0.0) "${kg.toInt()} kg" else "$kg kg"
            } else {
                "$quantity g"
            }
        }

        MeasurementType.VOLUME -> {
            if (quantity >= 1000) "${quantity / 1000.0} L" else "$quantity ml"
        }

        MeasurementType.PIECE -> {
            if (quantity == 1) "1 Piece" else "$quantity Pieces"
        }

        MeasurementType.FIXED_PACK -> {
            if (quantity == 1) "1 Pack" else "$quantity Packs"
        }
    }
}