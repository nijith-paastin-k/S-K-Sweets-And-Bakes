package com.skylink.sksweetsandbakes.presentation.dashboard.screen


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.skylink.sksweetsandbakes.core.designsystem.EmptyState
import com.skylink.sksweetsandbakes.core.designsystem.LiquidNavItemSpec
import com.skylink.sksweetsandbakes.core.designsystem.LoadingScreen
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.designsystem.SKGreetingAvatar
import com.skylink.sksweetsandbakes.core.designsystem.SKLiquidBottomNavigation
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.core.designsystem.TimeOfDayPeriod
import com.skylink.sksweetsandbakes.core.util.CurrencyFormatter
import com.skylink.sksweetsandbakes.core.util.DateUtils
import com.skylink.sksweetsandbakes.core.util.ImageUtils
import com.skylink.sksweetsandbakes.core.util.LocationHelper
import com.skylink.sksweetsandbakes.core.util.UserLocationInfo
import com.skylink.sksweetsandbakes.data.local.AppThemeMode
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.presentation.cart.screen.BillRow
import com.skylink.sksweetsandbakes.presentation.cart.screen.CartItemRow
import com.skylink.sksweetsandbakes.presentation.cart.viewmodel.CartViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.CatalogViewModel
import com.skylink.sksweetsandbakes.presentation.dashboard.viewmodel.HomeViewModel
import com.skylink.sksweetsandbakes.presentation.orders.viewmodel.OrdersViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import com.skylink.sksweetsandbakes.ui.theme.AppFontSizes
import com.skylink.sksweetsandbakes.ui.theme.adaptiveProductGridCells
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PromoBanner(
    val title: String,
    val subtitle: String,
    val badge: String,
    val gradientColors: List<Color>,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    catalogViewModel: CatalogViewModel? = null,
    ordersViewModel: OrdersViewModel? = null,
    cartViewModel: CartViewModel? = null,
    userProfile: com.skylink.sksweetsandbakes.domain.model.UserProfile? = null,
    currentThemeMode: AppThemeMode,
    onThemeModeChanged: (AppThemeMode) -> Unit,
    onProductClick: (productId: String) -> Unit,
    onCategoryClick: (categoryId: String, title: String) -> Unit,
    onAllCategoriesClick: () -> Unit = { onCategoryClick("", "All Categories") },
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {},
    onOrderClick: (orderId: String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onOrdersClick: () -> Unit = {},
    onPosClick: () -> Unit,
    onOwnerDashboardClick: () -> Unit,
    onInventoryClick: () -> Unit,
    onAddressesClick: () -> Unit,
    onWishlistClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val ordersState = ordersViewModel?.uiState?.collectAsState()?.value
    val cartState = cartViewModel?.uiState?.collectAsState()?.value
    val catalogState = catalogViewModel?.uiState?.collectAsState()?.value

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }
    val scope = rememberCoroutineScope()
    var selectedCategoryTabIndex by remember { mutableIntStateOf(0) }
    var currentBottomNavIndex by remember { mutableIntStateOf(0) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showProfilePhotoPreview by remember { mutableStateOf(false) }

    var userLocation by remember { mutableStateOf(LocationHelper.getCachedLocation(context)) }
    var isDetectingLocation by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var customLocationInput by remember { mutableStateOf("") }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        scope.launch {
            isDetectingLocation = true
            userLocation = LocationHelper.getLiveLocation(context, forceGps = granted)
            isDetectingLocation = false
        }
    }

    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasFine && !hasCoarse) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        isDetectingLocation = true
        userLocation = LocationHelper.getLiveLocation(context)
        isDetectingLocation = false
    }

    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val effectiveCartCount = cartState?.cart?.items?.size ?: state.cartCount

    val promoBanners = listOf(
        PromoBanner(
            title = "Pure Desi Ghee\nDelicacies",
            subtitle = "Use code FESTIVE10 at checkout",
            badge = "FESTIVE SPECIAL • 10% OFF",
            gradientColors = listOf(AppColors.MaroonPrimary, AppColors.GoldSecondary),
            imageUrl = ImageUtils.getDrawableUri("banner_diwali_diyas")
        ),
        PromoBanner(
            title = "Royal Kaju Katli\nGift Boxes",
            subtitle = "Handcrafted with premium Goan cashews",
            badge = "BESTSELLER SWEET",
            gradientColors = listOf(AppColors.MaroonDark, AppColors.GoldDark),
            imageUrl = ImageUtils.getDrawableUri("img_kaju_katli_pyramid")
        ),
        PromoBanner(
            title = "Grand Festive Sweets\nCelebration Hampers",
            subtitle = "Assorted luxury Indian sweets for your family",
            badge = "FESTIVE HAMPERS",
            gradientColors = listOf(AppColors.EveningGradient[0], AppColors.MaroonPrimary),
            imageUrl = ImageUtils.getDrawableUri("banner_festive_sweets_display")
        ),
        PromoBanner(
            title = "Free Express Delivery\nTo Your Doorstep",
            subtitle = "On all sweet orders above ₹1,000",
            badge = "FAST DELIVERY",
            gradientColors = listOf(AppColors.StatusSuccess, AppColors.MaroonPrimary),
            imageUrl = ImageUtils.getDrawableUri("banner_festive_diwali_box")
        )
    )

    if (state.isLoading) {
        LoadingScreen("Loading fresh sweets...")
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .widthIn(min = AppDimens.dp200, max = AppDimens.dp310)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(AppDimens.dp20)
                ) {
                    SKBrandLogoEmblem(
                        size = AppDimens.dp76,
                        modifier = Modifier.padding(bottom = AppDimens.dp2)
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp12))
                    Text(
                        text = "S K Sweets & Bakes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Pure Desi Ghee & Fresh Bakes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Spacer(modifier = Modifier.height(AppDimens.dp12))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home & Featured Sweets") },
                    selected = currentBottomNavIndex == 0,
                    onClick = {
                        currentBottomNavIndex = 0
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Category, contentDescription = null) },
                    label = { Text("All Categories") },
                    selected = currentBottomNavIndex == 1,
                    onClick = {
                        currentBottomNavIndex = 1
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
                    label = { Text("My Orders & Tracking") },
                    selected = currentBottomNavIndex == 2,
                    onClick = {
                        currentBottomNavIndex = 2
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = {
                        BadgedBox(
                            badge = {
                                if (state.wishlistIds.isNotEmpty()) {
                                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                        Text("${state.wishlistIds.size}", color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = AppColors.FavoriteRed
                            )
                        }
                    },
                    label = { Text("My Wishlist") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onWishlistClick()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    label = { Text("Saved Delivery Addresses") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onAddressesClick()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile & Settings") },
                    selected = currentBottomNavIndex == 4,
                    onClick = {
                        currentBottomNavIndex = 4
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                val isStaffOrOwner = userProfile?.role?.lowercase() in listOf(
                    "owner",
                    "manager",
                    "cashier",
                    "staff",
                    "admin"
                )
                if (isStaffOrOwner) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = AppDimens.dp8))

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Default.PointOfSale,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        label = { Text("Counter POS Checkout") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onPosClick()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Default.Inventory,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        label = { Text("Shared Inventory Ledger") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onInventoryClick()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Default.Assessment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        label = { Text("Owner Business Suite") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onOwnerDashboardClick()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = AppDimens.dp8))

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    label = {
                        Text(
                            "Sign Out / Logout",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showSignOutDialog = true
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(modifier = Modifier.padding(AppDimens.dp16)) {
                    Text(
                        "App Theme Mode:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp8))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                    ) {
                        FilterChip(
                            selected = currentThemeMode == AppThemeMode.LIGHT,
                            onClick = { onThemeModeChanged(AppThemeMode.LIGHT) },
                            label = { Text("Light", fontSize = AppFontSizes.sp12) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.LightMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp14)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = currentThemeMode == AppThemeMode.DARK,
                            onClick = { onThemeModeChanged(AppThemeMode.DARK) },
                            label = { Text("Dark", fontSize = AppFontSizes.sp12) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DarkMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp14)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = currentThemeMode == AppThemeMode.SYSTEM,
                            onClick = { onThemeModeChanged(AppThemeMode.SYSTEM) },
                            label = { Text("Auto", fontSize = AppFontSizes.sp12) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Brightness4,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp14)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SKBrandLogoEmblem(
                                size = AppDimens.dp42
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp10))
                            Column {
                                val topTitle = when (currentBottomNavIndex) {
                                    1 -> "Categories"
                                    2 -> "My Orders"
                                    3 -> "Shopping Cart"
                                    4 -> "My Account"
                                    else -> "S K Sweets & Bakes"
                                }
                                val topSubtitle = when (currentBottomNavIndex) {
                                    1 -> "Handcrafted Sweets & Bakes"
                                    2 -> "Track Your Orders"
                                    3 -> "${effectiveCartCount} items selected"
                                    4 -> "Profile & Preferences"
                                    else -> "Authentic Taste Since 1985"
                                }
                                Text(
                                    text = topTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = topSubtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onSearchClick) {
                            Icon(Icons.Default.Search, contentDescription = "Search Sweets")
                        }
                        IconButton(onClick = { currentBottomNavIndex = 3 }) {
                            BadgedBox(
                                badge = {
                                    if (effectiveCartCount > 0) {
                                        Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                                            Text("$effectiveCartCount", color = Color.White)
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                val navItems = listOf(
                    LiquidNavItemSpec(icon = Icons.Default.Home, label = "Home"),
                    LiquidNavItemSpec(icon = Icons.Default.Category, label = "Categories"),
                    LiquidNavItemSpec(icon = Icons.Default.ShoppingBag, label = "Orders"),
                    LiquidNavItemSpec(
                        icon = Icons.Default.ShoppingCart,
                        label = "Cart",
                        badgeCount = effectiveCartCount
                    ),
                    LiquidNavItemSpec(icon = Icons.Default.Person, label = "Profile")
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = AppDimens.dp14, vertical = AppDimens.dp6),
                    contentAlignment = Alignment.Center
                ) {
                    SKLiquidBottomNavigation(
                        selectedIndex = currentBottomNavIndex,
                        onItemSelected = { currentBottomNavIndex = it },
                        items = navItems
                    )
                }
            }
        ) { paddingValues ->
            when (currentBottomNavIndex) {
                0 -> {
                    val pagerState = rememberPagerState(pageCount = { promoBanners.size })

                    LaunchedEffect(pagerState) {
                        while (true) {
                            delay(4000)
                            val nextPage = (pagerState.currentPage + 1) % promoBanners.size
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(bottom = AppDimens.dp24)
                    ) {
                        item {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                                border = BorderStroke(
                                    AppDimens.dp1,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(AppDimens.dp18),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp6)
                                    .clickable { showLocationDialog = true }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = AppDimens.dp14,
                                            vertical = AppDimens.dp10
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f, fill = false)
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                                modifier = Modifier.size(AppDimens.dp32)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    if (isDetectingLocation) {
                                                        CircularProgressIndicator(
                                                            modifier = Modifier.size(AppDimens.dp16),
                                                            strokeWidth = AppDimens.dp2,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                    } else {
                                                        Icon(
                                                            Icons.Default.LocationOn,
                                                            contentDescription = "Location",
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(AppDimens.dp18)
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                                            Column {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = "DELIVERING TO",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        letterSpacing = AppFontSizes.letterSpacingTrackingWide,
                                                        fontSize = AppFontSizes.sp10
                                                    )
                                                    Spacer(modifier = Modifier.width(AppDimens.dp4))
                                                    Surface(
                                                        shape = RoundedCornerShape(AppDimens.dp6),
                                                        color = MaterialTheme.colorScheme.primary.copy(
                                                            alpha = 0.12f
                                                        )
                                                    ) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier.padding(
                                                                horizontal = AppDimens.dp5,
                                                                vertical = AppDimens.dp2
                                                            )
                                                        ) {
                                                            Text(
                                                                text = userLocation.source,
                                                                fontSize = AppFontSizes.sp9,
                                                                fontWeight = FontWeight.Bold,
                                                                color = MaterialTheme.colorScheme.primary
                                                            )
                                                            Icon(
                                                                Icons.Default.KeyboardArrowDown,
                                                                contentDescription = "Change",
                                                                tint = MaterialTheme.colorScheme.primary,
                                                                modifier = Modifier.size(AppDimens.dp12)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        val liveDateFormatted =
                                            remember(currentTimeMillis / 86400000L) {
                                                java.text.SimpleDateFormat(
                                                    "EEE, dd MMM",
                                                    java.util.Locale.getDefault()
                                                ).format(java.util.Date(currentTimeMillis))
                                            }
                                        val liveTimeFormatted =
                                            remember(currentTimeMillis / 1000L) {
                                                java.text.SimpleDateFormat(
                                                    "hh:mm:ss a",
                                                    java.util.Locale.getDefault()
                                                ).format(java.util.Date(currentTimeMillis))
                                            }

                                        Surface(
                                            shape = RoundedCornerShape(AppDimens.dp10),
                                            color = MaterialTheme.colorScheme.primaryContainer.copy(
                                                alpha = 0.85f
                                            ),
                                            border = BorderStroke(
                                                AppDimens.dp1,
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(
                                                    horizontal = AppDimens.dp8,
                                                    vertical = AppDimens.dp3
                                                ),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "📅 $liveDateFormatted",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontSize = AppFontSizes.sp9_5
                                                )
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(top = AppDimens.dp1)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(AppDimens.dp6)
                                                            .background(
                                                                AppColors.StatusSuccessGlow,
                                                                CircleShape
                                                            )
                                                    )
                                                    Spacer(modifier = Modifier.width(AppDimens.dp4))
                                                    Text(
                                                        text = liveTimeFormatted,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontSize = AppFontSizes.sp10
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(AppDimens.dp6))

                                    Text(
                                        text = userLocation.fullDetailedAddress,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = AppFontSizes.lh16
                                    )

                                    Spacer(modifier = Modifier.height(AppDimens.dp4))

                                    Text(
                                        text = "⚡ 30-min Express Sweet Delivery • Pure Desi Ghee guarantee",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = AppFontSizes.sp10_5
                                    )
                                }
                            }
                        }
                        item {
                            Card(
                                shape = RoundedCornerShape(AppDimens.dp20),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.65f
                                    )
                                ),
                                border = BorderStroke(
                                    AppDimens.dp1,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = AppDimens.dp16, vertical = AppDimens.dp8)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(AppDimens.dp14),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val currentHour = remember(currentTimeMillis / 60000L) {
                                        java.util.Calendar.getInstance()
                                            .get(java.util.Calendar.HOUR_OF_DAY)
                                    }
                                    val currentPeriod = TimeOfDayPeriod.fromCurrentHour(currentHour)
                                    SKGreetingAvatar(
                                        period = currentPeriod,
                                        size = AppDimens.dp56,
                                        onClick = { currentBottomNavIndex = 4 }
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp14))
                                    Column(modifier = Modifier.weight(1f)) {
                                        val displayName =
                                            userProfile?.displayName?.ifBlank { null } ?: "Ramesh"
                                        val firstName =
                                            displayName.split(" ").firstOrNull() ?: "Guest"
                                        Text(
                                            text = "${currentPeriod.greeting.removeSuffix("!")}, $firstName!",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(AppDimens.dp2))
                                        Text(
                                            text = currentPeriod.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                                    val emoji = when (currentPeriod) {
                                        TimeOfDayPeriod.MORNING -> "🌅"
                                        TimeOfDayPeriod.AFTERNOON -> "☀️"
                                        TimeOfDayPeriod.EVENING -> "🌇"
                                        TimeOfDayPeriod.NIGHT -> "🌙"
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(AppDimens.dp12),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.7f
                                        ),
                                        border = BorderStroke(
                                            AppDimens.dp1,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                        )
                                    ) {
                                        Text(
                                            text = "$emoji ${currentPeriod.timeRange}",
                                            fontSize = AppFontSizes.sp10,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(
                                                horizontal = AppDimens.dp8,
                                                vertical = AppDimens.dp4
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Column(modifier = Modifier.padding(top = AppDimens.dp12)) {
                                HorizontalPager(
                                    state = pagerState,
                                    contentPadding = PaddingValues(horizontal = AppDimens.dp16),
                                    pageSpacing = AppDimens.dp12,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(AppDimens.dp175)
                                ) { page ->
                                    val banner = promoBanners[page]
                                    Card(
                                        shape = RoundedCornerShape(AppDimens.dp20),
                                        modifier = Modifier.fillMaxSize(),
                                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Brush.horizontalGradient(banner.gradientColors))
                                                .padding(AppDimens.dp16)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1.3f)) {
                                                    Surface(
                                                        shape = RoundedCornerShape(AppDimens.dp8),
                                                        color = Color.White.copy(alpha = 0.25f)
                                                    ) {
                                                        Text(
                                                            text = banner.badge,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = Color.White,
                                                            modifier = Modifier.padding(
                                                                horizontal = AppDimens.dp8,
                                                                vertical = AppDimens.dp4
                                                            )
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(AppDimens.dp8))
                                                    Text(
                                                        text = banner.title,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color.White,
                                                        lineHeight = AppFontSizes.lh22
                                                    )
                                                    Spacer(modifier = Modifier.height(AppDimens.dp4))
                                                    Text(
                                                        text = banner.subtitle,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Color.White.copy(alpha = 0.9f)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                                Box(
                                                    modifier = Modifier
                                                        .size(AppDimens.dp110)
                                                        .clip(RoundedCornerShape(AppDimens.dp16))
                                                        .background(Color.White.copy(alpha = 0.15f))
                                                ) {
                                                    AsyncImage(
                                                        model = ImageUtils.resolveModel(banner.imageUrl),
                                                        contentDescription = banner.title,
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(AppDimens.dp8))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(promoBanners.size) { iteration ->
                                        val isSelected = pagerState.currentPage == iteration
                                        Box(
                                            modifier = Modifier
                                                .padding(AppDimens.dp3)
                                                .size(if (isSelected) AppDimens.dp8 else AppDimens.dp6)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                        alpha = 0.3f
                                                    )
                                                )
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(AppDimens.dp14))
                            val tabTitles = listOf("All Items") + state.categories.map { it.name }

                            PrimaryScrollableTabRow(
                                selectedTabIndex = selectedCategoryTabIndex,
                                edgePadding = AppDimens.dp16,
                                containerColor = Color.Transparent,
                                divider = {}
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    Tab(
                                        selected = selectedCategoryTabIndex == index,
                                        onClick = { selectedCategoryTabIndex = index },
                                        text = {
                                            Text(
                                                text = title,
                                                fontWeight = if (selectedCategoryTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(AppDimens.dp14))
                            Text(
                                text = "Shop by Category",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = AppDimens.dp16)
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp10))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = AppDimens.dp16),
                                horizontalArrangement = Arrangement.spacedBy(AppDimens.dp14)
                            ) {
                                items(state.categories) { category ->
                                    CategoryItem(
                                        category = category,
                                        onClick = {
                                            val catIdx =
                                                state.categories.indexOfFirst { it.categoryId == category.categoryId }
                                            if (catIdx >= 0) {
                                                selectedCategoryTabIndex = catIdx + 1
                                            } else {
                                                onCategoryClick(category.categoryId, category.name)
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(AppDimens.dp20))
                            Column(modifier = Modifier.padding(horizontal = AppDimens.dp16)) {
                                Text(
                                    text = "Bestselling Sweets & Bakes",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Prepared fresh every morning in pure cow ghee",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(AppDimens.dp12))
                        }

                        val displayedProducts = if (selectedCategoryTabIndex == 0) {
                            state.featuredProducts.ifEmpty { state.allProducts.take(8) }
                        } else {
                            val selectedCategory =
                                state.categories.getOrNull(selectedCategoryTabIndex - 1)
                            val catId = selectedCategory?.categoryId
                            val catName = selectedCategory?.name
                            val filtered = state.allProducts.filter { p ->
                                (catId != null && p.categoryId.equals(catId, ignoreCase = true)) ||
                                        (catName != null && p.categoryName.equals(
                                            catName,
                                            ignoreCase = true
                                        ))
                            }
                            filtered
                        }

                        items(displayedProducts, key = { it.productId }) { product ->
                            val isWishlisted = state.wishlistIds.contains(product.productId)
                            ProductCard(
                                product = product,
                                isWishlisted = isWishlisted,
                                onWishlistClick = { viewModel.toggleWishlist(product.productId) },
                                onClick = { onProductClick(product.productId) },
                                modifier = Modifier.padding(
                                    horizontal = AppDimens.dp16,
                                    vertical = AppDimens.dp6
                                )
                            )
                        }
                    }
                }

                1 -> {
                    val categories = catalogState?.categories ?: state.categories
                    LazyVerticalGrid(
                        columns = adaptiveProductGridCells(AppDimens.dp135),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(AppDimens.dp16),
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
                    ) {
                        items(categories) { category ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCategoryClick(
                                            category.categoryId,
                                            category.name
                                        )
                                    },
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

                2 -> {
                    val orders = ordersState?.orders ?: emptyList()
                    if (orders.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyState(
                                title = "No Past Orders",
                                subtitle = "When you place an order for sweets, it will appear here.",
                                icon = Icons.Default.ShoppingBag,
                                actionButtonText = "Browse Sweets",
                                onActionClick = { currentBottomNavIndex = 0 }
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(
                                start = AppDimens.dp16,
                                top = AppDimens.dp16,
                                end = AppDimens.dp16,
                                bottom = AppDimens.dp48
                            ),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
                        ) {
                            items(orders) { order ->
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

                3 -> {
                    val cart = cartState?.cart
                    if (cart == null || cart.isEmpty) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyState(
                                title = "Your Cart is Empty",
                                subtitle = "Explore our handcrafted sweets and add your favourites to cart.",
                                icon = Icons.Default.ShoppingCart,
                                actionButtonText = "Explore Sweets",
                                onActionClick = { currentBottomNavIndex = 0 }
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(AppDimens.dp16),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
                        ) {
                            items(cart.items) { item ->
                                CartItemRow(
                                    item = item,
                                    onIncrement = {
                                        val step =
                                            if (item.measurementType == MeasurementType.WEIGHT) 250 else 1
                                        cartViewModel.onQuantityChanged(
                                            item.productId,
                                            item.selectedQuantity + step
                                        )
                                    },
                                    onDecrement = {
                                        val step =
                                            if (item.measurementType == MeasurementType.WEIGHT) 250 else 1
                                        val next = item.selectedQuantity - step
                                        if (next > 0) {
                                            cartViewModel.onQuantityChanged(item.productId, next)
                                        } else {
                                            cartViewModel.onRemoveItem(item.productId)
                                        }
                                    },
                                    onRemove = {
                                        cartViewModel.onRemoveItem(item.productId)
                                    }
                                )
                            }

                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp14),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(AppDimens.dp14)) {
                                        Text(
                                            text = "Apply Coupon / Promo",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(AppDimens.dp8))

                                        if (cart.appliedCouponCode != null) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        MaterialTheme.colorScheme.primaryContainer,
                                                        RoundedCornerShape(AppDimens.dp10)
                                                    )
                                                    .padding(
                                                        horizontal = AppDimens.dp12,
                                                        vertical = AppDimens.dp8
                                                    ),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "Coupon Applied: ${cart.appliedCouponCode}",
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                    Text(
                                                        text = "You saved ${
                                                            CurrencyFormatter.formatPaise(
                                                                cart.discountPaise
                                                            )
                                                        }",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                }
                                                IconButton(onClick = { cartViewModel.removeCoupon() }) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = "Remove"
                                                    )
                                                }
                                            }
                                        } else {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                OutlinedTextField(
                                                    value = cartState.couponInput,
                                                    onValueChange = {
                                                        cartViewModel.onCouponInputChanged(
                                                            it
                                                        )
                                                    },
                                                    placeholder = { Text("Enter code (e.g. FESTIVE10)") },
                                                    singleLine = true,
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(AppDimens.dp10)
                                                )
                                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                                Button(
                                                    onClick = { cartViewModel.applyCoupon() },
                                                    shape = RoundedCornerShape(AppDimens.dp10)
                                                ) {
                                                    Text("Apply")
                                                }
                                            }
                                        }

                                        if (cartState.couponMessage != null) {
                                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                                            Text(
                                                text = cartState.couponMessage,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (cartState.isCouponError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    }
                                }
                            }

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

                            item {
                                Button(
                                    onClick = onCheckoutClick,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(AppDimens.dp52),
                                    shape = RoundedCornerShape(AppDimens.dp12),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pay ${CurrencyFormatter.formatPaise(cart.grandTotalPaise)}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = AppFontSizes.sp16
                                        )
                                        Text(
                                            text = "Proceed to Checkout >",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = AppFontSizes.sp15
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                4 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(AppDimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.dp16),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(AppDimens.dp16),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val displayName =
                                        userProfile?.displayName?.ifBlank { null } ?: "Ramesh Kumar"
                                    val displayPhone = userProfile?.phoneNumber?.ifBlank { null }
                                        ?: "+91 98765 43210"
                                    val initials = displayName.split(" ")
                                        .mapNotNull { it.firstOrNull()?.toString() }.take(2)
                                        .joinToString("").ifEmpty { "SK" }

                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        border = BorderStroke(
                                            AppDimens.dp2,
                                            MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier
                                            .size(AppDimens.dp60)
                                            .clip(CircleShape)
                                            .clickable { showProfilePhotoPreview = true }
                                    ) {
                                        if (!userProfile?.avatarUrl.isNullOrBlank()) {
                                            AsyncImage(
                                                model = userProfile.avatarUrl,
                                                contentDescription = "Profile Photo",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = initials,
                                                    style = MaterialTheme.typography.titleLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(AppDimens.dp16))
                                    val displayEmail = userProfile?.email?.ifBlank { null }
                                        ?: "customer@sksweets.com"
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = displayName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(AppDimens.dp3))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(AppDimens.dp6))
                                                .clickable {
                                                    try {
                                                        val intent = Intent(
                                                            Intent.ACTION_DIAL,
                                                            Uri.parse(
                                                                "tel:" + displayPhone.replace(
                                                                    " ",
                                                                    ""
                                                                )
                                                            )
                                                        )
                                                        context.startActivity(intent)
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            context,
                                                            "No phone dialer available",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                                .padding(vertical = AppDimens.dp2)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "Call Phone",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(AppDimens.dp13)
                                            )
                                            Spacer(modifier = Modifier.width(AppDimens.dp4))
                                            Text(
                                                text = displayPhone,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(AppDimens.dp6))
                                                .clickable {
                                                    try {
                                                        val intent = Intent(
                                                            Intent.ACTION_SENDTO,
                                                            Uri.parse("mailto:" + displayEmail)
                                                        )
                                                        context.startActivity(intent)
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            context,
                                                            "No email app available",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                                .padding(vertical = AppDimens.dp2)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Email,
                                                contentDescription = "Send Email",
                                                tint = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.size(AppDimens.dp13)
                                            )
                                            Spacer(modifier = Modifier.width(AppDimens.dp4))
                                            Text(
                                                text = displayEmail,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                                        Surface(
                                            shape = RoundedCornerShape(AppDimens.dp6),
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {
                                            Text(
                                                text = "VIP Sweet Connoisseur",
                                                modifier = Modifier.padding(
                                                    horizontal = AppDimens.dp8,
                                                    vertical = AppDimens.dp2
                                                ),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }
                                    IconButton(onClick = onProfileClick) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = "Edit Profile",
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.dp16),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(AppDimens.dp16)) {
                                    Text(
                                        "Theme Mode Preference",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(AppDimens.dp10))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                                    ) {
                                        FilterChip(
                                            selected = currentThemeMode == AppThemeMode.LIGHT,
                                            onClick = { onThemeModeChanged(AppThemeMode.LIGHT) },
                                            label = { Text("Light") },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.LightMode,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp16)
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                        FilterChip(
                                            selected = currentThemeMode == AppThemeMode.DARK,
                                            onClick = { onThemeModeChanged(AppThemeMode.DARK) },
                                            label = { Text("Dark") },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.DarkMode,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp16)
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                        FilterChip(
                                            selected = currentThemeMode == AppThemeMode.SYSTEM,
                                            onClick = { onThemeModeChanged(AppThemeMode.SYSTEM) },
                                            label = { Text("Auto") },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Brightness4,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp16)
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.dp16),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(vertical = AppDimens.dp8)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(onClick = onAddressesClick)
                                            .padding(
                                                horizontal = AppDimens.dp16,
                                                vertical = AppDimens.dp12
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp16))
                                        Text(
                                            "Saved Delivery Addresses",
                                            modifier = Modifier.weight(1f),
                                            fontWeight = FontWeight.Medium
                                        )
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp14)
                                        )
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(horizontal = AppDimens.dp16))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(onClick = onWishlistClick)
                                            .padding(
                                                horizontal = AppDimens.dp16,
                                                vertical = AppDimens.dp12
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = AppColors.FavoriteRed
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp16))
                                        Text(
                                            "My Wishlist",
                                            modifier = Modifier.weight(1f),
                                            fontWeight = FontWeight.Medium
                                        )
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp14)
                                        )
                                    }

                                    val isStaffOrOwnerTab =
                                        userProfile?.role?.lowercase() in listOf(
                                            "owner",
                                            "manager",
                                            "cashier",
                                            "staff",
                                            "admin"
                                        )
                                    if (isStaffOrOwnerTab) {
                                        HorizontalDivider(modifier = Modifier.padding(horizontal = AppDimens.dp16))

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(onClick = onPosClick)
                                                .padding(
                                                    horizontal = AppDimens.dp16,
                                                    vertical = AppDimens.dp12
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.PointOfSale,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.width(AppDimens.dp16))
                                            Text(
                                                "Counter POS Checkout",
                                                modifier = Modifier.weight(1f),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowForwardIos,
                                                contentDescription = null,
                                                modifier = Modifier.size(AppDimens.dp14)
                                            )
                                        }

                                        HorizontalDivider(modifier = Modifier.padding(horizontal = AppDimens.dp16))

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(onClick = onOwnerDashboardClick)
                                                .padding(
                                                    horizontal = AppDimens.dp16,
                                                    vertical = AppDimens.dp12
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Assessment,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.width(AppDimens.dp16))
                                            Text(
                                                "Owner Business Suite",
                                                modifier = Modifier.weight(1f),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowForwardIos,
                                                contentDescription = null,
                                                modifier = Modifier.size(AppDimens.dp14)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            OutlinedButton(
                                onClick = { showSignOutDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(AppDimens.dp48),
                                shape = RoundedCornerShape(AppDimens.dp12),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                border = BorderStroke(
                                    AppDimens.dp1,
                                    MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp18)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                Text("Sign Out / Logout", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showProfilePhotoPreview) {
        val displayName = userProfile?.displayName?.ifBlank { null } ?: "Ramesh Kumar"
        val displayPhone = userProfile?.phoneNumber?.ifBlank { null } ?: "+91 98765 43210"
        val displayEmail = userProfile?.email?.ifBlank { null } ?: "customer@sksweets.com"
        val initials = displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2)
            .joinToString("").ifEmpty { "SK" }

        Dialog(onDismissRequest = { showProfilePhotoPreview = false }) {
            Card(
                shape = RoundedCornerShape(AppDimens.dp24),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(AppDimens.dp2, AppColors.GoldAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.dp16)
            ) {
                Column(
                    modifier = Modifier.padding(AppDimens.dp20),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SKBrandLogoEmblem(size = AppDimens.dp32, elevation = AppDimens.dp2)
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            Text(
                                text = "Profile Photo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = { showProfilePhotoPreview = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        border = BorderStroke(AppDimens.dp3, MaterialTheme.colorScheme.primary),
                        shadowElevation = AppDimens.dp8,
                        modifier = Modifier.size(AppDimens.dp180)
                    ) {
                        if (!userProfile?.avatarUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = userProfile.avatarUrl,
                                contentDescription = displayName,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = initials,
                                    fontSize = AppFontSizes.sp48,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp4))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppDimens.dp6))
                            .clickable {
                                try {
                                    val intent = Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:" + displayPhone.replace(" ", ""))
                                    )
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No phone dialer available",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp4)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.dp14)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                        Text(
                            text = displayPhone,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppDimens.dp6))
                            .clickable {
                                try {
                                    val intent = Intent(
                                        Intent.ACTION_SENDTO,
                                        Uri.parse("mailto:" + displayEmail)
                                    )
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No email app available",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp4)
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(AppDimens.dp14)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                        Text(
                            text = displayEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp20))
                    Button(
                        onClick = {
                            showProfilePhotoPreview = false
                            onProfileClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp12)
                    ) {
                        Text("Edit Profile & Photo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(AppDimens.dp48)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.dp28)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(AppDimens.dp24),
            title = {
                Text(
                    "Set Delivery Address",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)) {
                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp12),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        border = BorderStroke(
                            AppDimens.dp1,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(AppDimens.dp10)) {
                            Text(
                                text = "Current Detailed Address (${userLocation.source}):",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp3))
                            Text(
                                text = userLocation.fullDetailedAddress,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val hasFine = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            val hasCoarse = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            if (hasFine || hasCoarse) {
                                scope.launch {
                                    isDetectingLocation = true
                                    userLocation =
                                        LocationHelper.getLiveLocation(context, forceGps = true)
                                    isDetectingLocation = false
                                    showLocationDialog = false
                                }
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                                showLocationDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            Icons.Default.MyLocation,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.dp18)
                        )
                        Spacer(Modifier.width(AppDimens.dp8))
                        Text("Detect Live GPS Location")
                    }

                    HorizontalDivider()

                    Text(
                        "Or select a quick city address:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val quickAddressPresets = listOf(
                        Triple(
                            "Coimbatore",
                            "RS Puram",
                            "#14, 2nd Cross, Sweet Garden Layout, RS Puram, Coimbatore - 641002, Tamil Nadu"
                        ),
                        Triple(
                            "Chennai",
                            "T. Nagar",
                            "#88, Usman Road, Near Panagal Park, T. Nagar, Chennai - 600017, Tamil Nadu"
                        ),
                        Triple(
                            "Bengaluru",
                            "Koramangala",
                            "#42, 100 Feet Road, 4th Block, Koramangala, Bengaluru - 560034, Karnataka"
                        ),
                        Triple(
                            "Madurai",
                            "Madurai Main",
                            "#25, West Veli Street, Opp. Meenakshi Amman Temple, Madurai - 625001, Tamil Nadu"
                        ),
                        Triple(
                            "Tiruppur",
                            "Kumar Nagar",
                            "#12, Avinashi Road, Kumar Nagar, Tiruppur - 641603, Tamil Nadu"
                        ),
                        Triple(
                            "Salem",
                            "Meyyanur",
                            "#7, Junction Main Road, Meyyanur, Salem - 636004, Tamil Nadu"
                        )
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(AppDimens.dp6)) {
                        items(quickAddressPresets) { (cityName, areaName, fullAddr) ->
                            FilterChip(
                                selected = userLocation.city.equals(cityName, ignoreCase = true),
                                onClick = {
                                    val updated = UserLocationInfo(
                                        area = areaName,
                                        city = cityName,
                                        state = if (cityName == "Bengaluru") "Karnataka" else "Tamil Nadu",
                                        fullDetailedAddress = fullAddr,
                                        source = "Preset"
                                    )
                                    userLocation = updated
                                    LocationHelper.saveSelectedLocation(context, updated)
                                    showLocationDialog = false
                                },
                                label = { Text(cityName, fontSize = AppFontSizes.sp11) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = customLocationInput,
                        onValueChange = { customLocationInput = it },
                        label = { Text("Or Enter Full Address") },
                        placeholder = { Text("e.g. #21, 4th Cross, Gandhi Road, Coimbatore - 641001") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customLocationInput.isNotBlank()) {
                            val parts = customLocationInput.split(",").map { it.trim() }
                            val city = if (parts.size >= 2) parts[parts.size - 2] else parts[0]
                            val area = if (parts.size >= 3) parts[parts.size - 3] else ""
                            val updated = UserLocationInfo(
                                area = area,
                                city = city,
                                state = "Tamil Nadu",
                                fullDetailedAddress = customLocationInput.trim(),
                                source = "Manual"
                            )
                            userLocation = updated
                            LocationHelper.saveSelectedLocation(context, updated)
                        }
                        showLocationDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLocationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            icon = { SKBrandLogoEmblem(size = AppDimens.dp48, elevation = AppDimens.dp2) },
            shape = RoundedCornerShape(AppDimens.dp24),
            title = {
                Text(
                    "Sign Out",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = { Text("Are you sure you want to sign out from S K Sweets & Bakes?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        onSignOutClick()
                    }
                ) {
                    Text(
                        "Sign Out",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun LiquidNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(AppDimens.dp12))
            .clickable(onClick = onClick)
            .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp4)
    ) {
        BadgedBox(
            badge = {
                if (badgeCount > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text("$badgeCount")
                    }
                }
            }
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                modifier = Modifier.size(AppDimens.dp36)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(AppDimens.dp20)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AppDimens.dp2))
        Text(
            text = label,
            fontSize = AppFontSizes.sp11,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(AppDimens.dp84)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.dp72)
                .clip(CircleShape)
                .border(
                    AppDimens.dp2,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            AsyncImage(
                model = ImageUtils.resolveModel(category.imageUrl),
                contentDescription = category.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(AppDimens.dp6))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    isWishlisted: Boolean = false,
    onWishlistClick: () -> Unit = {},
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppDimens.dp16),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.dp95)
                    .clip(RoundedCornerShape(AppDimens.dp12))
            ) {
                AsyncImage(
                    model = ImageUtils.resolveModel(product.images.firstOrNull()),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(AppDimens.dp14))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = product.categoryName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(AppDimens.dp6))
                val priceText = when (product.measurementType) {
                    MeasurementType.WEIGHT -> "${CurrencyFormatter.formatPaise(product.unitPricePaise)} per ${product.displayUnit}"
                    MeasurementType.VOLUME -> "${CurrencyFormatter.formatPaise(product.unitPricePaise)} per ${product.displayUnit}"
                    MeasurementType.PIECE -> "${CurrencyFormatter.formatPaise(product.unitPricePaise)} / piece"
                    MeasurementType.FIXED_PACK -> CurrencyFormatter.formatPaise(product.unitPricePaise)
                }
                Text(
                    text = priceText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(AppDimens.dp4))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AppColors.StatusWarning,
                        modifier = Modifier.size(AppDimens.dp16)
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp4))
                    Text(
                        text = "4.9 (150+ reviews)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            IconButton(
                onClick = onWishlistClick,
                modifier = Modifier.size(AppDimens.dp40)
            ) {
                Icon(
                    imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isWishlisted) "Remove from Wishlist" else "Add to Wishlist",
                    tint = if (isWishlisted) AppColors.FavoriteRed else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.6f
                    ),
                    modifier = Modifier.size(AppDimens.dp24)
                )
            }
        }
    }
}