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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.skylink.sksweetsandbakes.R
import com.skylink.sksweetsandbakes.core.designsystem.EmptyState
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.ImageUtils
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.CatalogViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@Composable
fun CategoriesScreen(
    viewModel: CatalogViewModel,
    onCategoryClick: (categoryId: String, title: String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SKSweetsTopBar(title = "All Categories", onBackClick = onBackClick) }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.dp16),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            items(state.categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(category.categoryId, category.name) },
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppDimens.dp120)
                        ) {
                            AsyncImage(
                                model = ImageUtils.resolveModel(category.imageUrl),
                                contentDescription = category.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier.padding(AppDimens.dp12)) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            Text(
                                text = category.description,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListScreen(
    categoryId: String?,
    categoryTitle: String?,
    viewModel: CatalogViewModel,
    onProductClick: (productId: String) -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(categoryId) {
        viewModel.loadProductsForCategory(categoryId, categoryTitle)
    }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SKSweetsTopBar(title = state.selectedCategoryTitle, onBackClick = onBackClick) }
    ) { padding ->
        if (state.isLoading) {
            LoadingScreen()
        } else if (state.products.isEmpty()) {
            EmptyState(
                title = "No Products in this Category",
                subtitle = "Check back soon as our chefs prepare fresh stock."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(AppDimens.dp16),
                verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
            ) {
                items(state.products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductClick(product.productId) },
                        shape = RoundedCornerShape(AppDimens.dp16),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(modifier = Modifier.padding(AppDimens.dp14)) {
                            Box(
                                modifier = Modifier
                                    .size(AppDimens.dp90)
                                    .clip(RoundedCornerShape(AppDimens.dp12))
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                AsyncImage(
                                    model = ImageUtils.resolveModel(product.images.firstOrNull()),
                                    contentDescription = product.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(AppDimens.dp14))
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp2))
                                Text(
                                    text = product.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp6))
                                Text(
                                    text = "${CurrencyFormatter.formatPaise(product.unitPricePaise)} / ${product.displayUnit}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
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
fun SearchScreen(
    viewModel: CatalogViewModel,
    onProductClick: (productId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Search Sweets", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppDimens.dp16)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Search Kaju Katli, Mysore Pak, Laddu...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppDimens.dp12)
            )

            Spacer(modifier = Modifier.height(AppDimens.dp16))

            if (state.searchQuery.isBlank()) {
                EmptyState(
                    title = "Search Our Fresh Menu",
                    subtitle = "Search by sweet name, ingredients, or bakery item.",
                    drawableRes = R.drawable.img_empty_search_plate
                )
            } else if (state.searchResults.isEmpty()) {
                EmptyState(
                    title = "No Sweets Found",
                    subtitle = "Try searching for another sweet or browse categories.",
                    drawableRes = R.drawable.img_empty_search_plate
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)) {
                    items(state.searchResults) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProductClick(product.productId) },
                            shape = RoundedCornerShape(AppDimens.dp12),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(AppDimens.dp12),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(AppDimens.dp60)
                                        .clip(RoundedCornerShape(AppDimens.dp8))
                                ) {
                                    AsyncImage(
                                        model = ImageUtils.resolveModel(product.images.firstOrNull()),
                                        contentDescription = product.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.width(AppDimens.dp12))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = CurrencyFormatter.formatPaise(product.unitPricePaise),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
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