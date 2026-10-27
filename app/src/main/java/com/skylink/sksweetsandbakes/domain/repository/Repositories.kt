package com.skylink.sksweetsandbakes.domain.repository

import com.skylink.sksweetsandbakes.domain.auth.repository.AuthRepository as IAuthRepository
import com.skylink.sksweetsandbakes.domain.cart.repository.CartRepository as ICartRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.AnalyticsRepository as IAnalyticsRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.ExpenseRepository as IExpenseRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.InventoryRepository as IInventoryRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.PosRepository as IPosRepository
import com.skylink.sksweetsandbakes.domain.dashboard.repository.ProductRepository as IProductRepository
import com.skylink.sksweetsandbakes.domain.favourites.repository.FavouritesRepository as IFavouritesRepository
import com.skylink.sksweetsandbakes.domain.orders.repository.InvoiceRepository as IInvoiceRepository
import com.skylink.sksweetsandbakes.domain.orders.repository.NotificationRepository as INotificationRepository
import com.skylink.sksweetsandbakes.domain.orders.repository.OrderRepository as IOrderRepository
import com.skylink.sksweetsandbakes.domain.wishlist.repository.WishlistRepository as IWishlistRepository

typealias AuthRepository = IAuthRepository
typealias ProductRepository = IProductRepository
typealias CartRepository = ICartRepository
typealias OrderRepository = IOrderRepository
typealias InventoryRepository = IInventoryRepository
typealias PosRepository = IPosRepository
typealias ExpenseRepository = IExpenseRepository
typealias AnalyticsRepository = IAnalyticsRepository
typealias InvoiceRepository = IInvoiceRepository
typealias NotificationRepository = INotificationRepository
typealias FavouritesRepository = IFavouritesRepository
typealias WishlistRepository = IWishlistRepository
