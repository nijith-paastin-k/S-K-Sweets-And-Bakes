package com.skylink.sksweetsandbakes.presentation.favourites.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.skylink.sksweetsandbakes.core.designsystem.EmptyState
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.presentation.favourites.viewmodel.FavouritesViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
    onProductClick: (productId: String) -> Unit,
    onExploreSweetsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "My Favourites", onBackClick = onBackClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            LoadingScreen()
        } else if (state.favouriteProducts.isEmpty()) {
            EmptyState(
                title = "Your Favourites List is Empty",
                subtitle = "Save sweets and bakes you love most for instant re-ordering.",
                icon = Icons.Default.Favorite,
                actionButtonText = "Explore Sweets",
                onActionClick = onExploreSweetsClick
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(AppDimens.dp16),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.dp14),
                verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
            ) {
                items(state.favouriteProducts) { product ->
                    FavouriteProductCard(
                        product = product,
                        onClick = { onProductClick(product.productId) },
                        onRemove = { viewModel.toggleFavourite(product.productId) },
                        onAddToCart = { viewModel.addToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavouriteProductCard(
    product: Product,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppDimens.dp16),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimens.dp130)
            ) {
                AsyncImage(
                    model = product.images.firstOrNull() ?: "",
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(AppDimens.dp6)
                        .size(AppDimens.dp32)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove Favourite",
                        tint = Color.White,
                        modifier = Modifier.size(AppDimens.dp18)
                    )
                }
            }

            Column(modifier = Modifier.padding(AppDimens.dp12)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(AppDimens.dp4))
                Text(
                    text = "${CurrencyFormatter.formatPaise(product.unitPricePaise)} / ${product.displayUnit}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(AppDimens.dp10))
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp36),
                    shape = RoundedCornerShape(AppDimens.dp8)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(AppDimens.dp16)
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text("Add to Cart", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
