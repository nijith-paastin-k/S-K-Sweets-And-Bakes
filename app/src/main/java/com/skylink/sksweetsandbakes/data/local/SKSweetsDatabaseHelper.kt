package com.skylink.sksweetsandbakes.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.model.OrderPricing
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.model.UserProfile

/**
 * Local SQLite database helper providing complete offline persistence
 * for Products, Categories, Orders, User Profile, and Saved Addresses.
 * Enables the app to work seamlessly even when no internet connection is available.
 */
class SKSweetsDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "sksweets_local.db"
        const val DATABASE_VERSION = 3
        const val TABLE_PRODUCTS = "products"
        const val COL_PROD_ID = "product_id"
        const val COL_PROD_SKU = "sku"
        const val COL_PROD_NAME = "name"
        const val COL_PROD_CAT_ID = "category_id"
        const val COL_PROD_CAT_NAME = "category_name"
        const val COL_PROD_DESC = "description"
        const val COL_PROD_IMAGE = "image_url"
        const val COL_PROD_MEASUREMENT = "measurement_type"
        const val COL_PROD_PRICE = "price_paise"
        const val COL_PROD_STOCK = "available_stock"
        const val COL_PROD_IS_FEATURED = "is_featured"
        const val TABLE_CATEGORIES = "categories"
        const val COL_CAT_ID = "category_id"
        const val COL_CAT_NAME = "name"
        const val COL_CAT_DESC = "description"
        const val COL_CAT_IMAGE = "image_url"
        const val COL_CAT_ORDER = "display_order"
        const val TABLE_ADDRESSES = "delivery_addresses"
        const val COL_ADDR_ID = "address_id"
        const val COL_ADDR_USER_ID = "user_id"
        const val COL_ADDR_NAME = "recipient_name"
        const val COL_ADDR_PHONE = "phone_number"
        const val COL_ADDR_LINE1 = "address_line1"
        const val COL_ADDR_LINE2 = "address_line2"
        const val COL_ADDR_CITY = "city"
        const val COL_ADDR_PINCODE = "pincode"
        const val COL_ADDR_IS_DEFAULT = "is_default"
        const val TABLE_PROFILE = "user_profile"
        const val COL_USER_ID = "user_id"
        const val COL_USER_NAME = "display_name"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PHONE = "phone_number"
        const val COL_USER_ROLE = "role"
        const val COL_USER_AVATAR = "avatar_url"
        const val TABLE_ORDERS = "orders"
        const val COL_ORDER_ID = "order_id"
        const val COL_ORDER_NUM = "order_number"
        const val COL_ORDER_CUSTOMER_ID = "customer_id"
        const val COL_ORDER_TOTAL = "grand_total_paise"
        const val COL_ORDER_STATUS = "status"
        const val COL_ORDER_DATE = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_PRODUCTS (
                $COL_PROD_ID TEXT PRIMARY KEY,
                $COL_PROD_SKU TEXT,
                $COL_PROD_NAME TEXT,
                $COL_PROD_CAT_ID TEXT,
                $COL_PROD_CAT_NAME TEXT,
                $COL_PROD_DESC TEXT,
                $COL_PROD_IMAGE TEXT,
                $COL_PROD_MEASUREMENT TEXT,
                $COL_PROD_PRICE INTEGER,
                $COL_PROD_STOCK INTEGER,
                $COL_PROD_IS_FEATURED INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_CATEGORIES (
                $COL_CAT_ID TEXT PRIMARY KEY,
                $COL_CAT_NAME TEXT,
                $COL_CAT_DESC TEXT,
                $COL_CAT_IMAGE TEXT,
                $COL_CAT_ORDER INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_ADDRESSES (
                $COL_ADDR_ID TEXT PRIMARY KEY,
                $COL_ADDR_USER_ID TEXT,
                $COL_ADDR_NAME TEXT,
                $COL_ADDR_PHONE TEXT,
                $COL_ADDR_LINE1 TEXT,
                $COL_ADDR_LINE2 TEXT,
                $COL_ADDR_CITY TEXT,
                $COL_ADDR_PINCODE TEXT,
                $COL_ADDR_IS_DEFAULT INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_PROFILE (
                $COL_USER_ID TEXT PRIMARY KEY,
                $COL_USER_NAME TEXT,
                $COL_USER_EMAIL TEXT,
                $COL_USER_PHONE TEXT,
                $COL_USER_ROLE TEXT,
                $COL_USER_AVATAR TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_ORDERS (
                $COL_ORDER_ID TEXT PRIMARY KEY,
                $COL_ORDER_NUM TEXT,
                $COL_ORDER_CUSTOMER_ID TEXT,
                $COL_ORDER_TOTAL INTEGER,
                $COL_ORDER_STATUS TEXT,
                $COL_ORDER_DATE INTEGER
            )
            """.trimIndent()
        )

        seedDatabase(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADDRESSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        onCreate(db)
    }

    private fun seedDatabase(db: SQLiteDatabase) {
        SeedData.categories.forEach { cat ->
            val cv = ContentValues().apply {
                put(COL_CAT_ID, cat.categoryId)
                put(COL_CAT_NAME, cat.name)
                put(COL_CAT_DESC, cat.description)
                put(COL_CAT_IMAGE, cat.imageUrl)
                put(COL_CAT_ORDER, cat.displayOrder)
            }
            db.insertWithOnConflict(TABLE_CATEGORIES, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }

        SeedData.products.forEach { prod ->
            val cv = ContentValues().apply {
                put(COL_PROD_ID, prod.productId)
                put(COL_PROD_SKU, prod.sku)
                put(COL_PROD_NAME, prod.name)
                put(COL_PROD_CAT_ID, prod.categoryId)
                put(COL_PROD_CAT_NAME, prod.categoryName)
                put(COL_PROD_DESC, prod.description)
                put(COL_PROD_IMAGE, prod.images.firstOrNull() ?: "")
                put(COL_PROD_MEASUREMENT, prod.measurementType.name)
                put(COL_PROD_PRICE, prod.unitPricePaise)
                put(COL_PROD_STOCK, prod.availableStock)
                put(COL_PROD_IS_FEATURED, if (prod.isFeatured) 1 else 0)
            }
            db.insertWithOnConflict(TABLE_PRODUCTS, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }

        val addrCv = ContentValues().apply {
            put(COL_ADDR_ID, "addr_01")
            put(COL_ADDR_USER_ID, "usr_demo_01")
            put(COL_ADDR_NAME, "Ramesh Kumar")
            put(COL_ADDR_PHONE, "+91 98765 43210")
            put(COL_ADDR_LINE1, "#14, 2nd Cross, Sweet Garden Layout")
            put(COL_ADDR_LINE2, "Near Anjaneya Temple")
            put(COL_ADDR_CITY, "Bengaluru")
            put(COL_ADDR_PINCODE, "560001")
            put(COL_ADDR_IS_DEFAULT, 1)
        }
        db.insertWithOnConflict(TABLE_ADDRESSES, null, addrCv, SQLiteDatabase.CONFLICT_REPLACE)

        val userCv = ContentValues().apply {
            put(COL_USER_ID, "usr_demo_01")
            put(COL_USER_NAME, "Ramesh Kumar")
            put(COL_USER_EMAIL, "ramesh.kumar@example.com")
            put(COL_USER_PHONE, "+91 98765 43210")
            put(COL_USER_ROLE, "customer")
            put(
                COL_USER_AVATAR,
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"
            )
        }
        db.insertWithOnConflict(TABLE_PROFILE, null, userCv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAllProducts(): List<Product> {
        val list = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null)
        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndexOrThrow(COL_PROD_ID))
                val sku = c.getString(c.getColumnIndexOrThrow(COL_PROD_SKU))
                val name = c.getString(c.getColumnIndexOrThrow(COL_PROD_NAME))
                val catId = c.getString(c.getColumnIndexOrThrow(COL_PROD_CAT_ID))
                val catName = c.getString(c.getColumnIndexOrThrow(COL_PROD_CAT_NAME))
                val desc = c.getString(c.getColumnIndexOrThrow(COL_PROD_DESC))
                val image = c.getString(c.getColumnIndexOrThrow(COL_PROD_IMAGE))
                val measStr = c.getString(c.getColumnIndexOrThrow(COL_PROD_MEASUREMENT))
                val price = c.getLong(c.getColumnIndexOrThrow(COL_PROD_PRICE))
                val stock = c.getInt(c.getColumnIndexOrThrow(COL_PROD_STOCK))
                val isFeatured = c.getInt(c.getColumnIndexOrThrow(COL_PROD_IS_FEATURED)) == 1

                val measurement = try {
                    MeasurementType.valueOf(measStr)
                } catch (e: Exception) {
                    MeasurementType.WEIGHT
                }

                val baseUnit = when (measurement) {
                    MeasurementType.PIECE -> "piece"
                    MeasurementType.FIXED_PACK -> "pack"
                    MeasurementType.VOLUME -> "ml"
                    MeasurementType.WEIGHT -> "g"
                }

                val displayUnit = when (measurement) {
                    MeasurementType.PIECE -> "piece"
                    MeasurementType.FIXED_PACK -> "pack"
                    MeasurementType.VOLUME -> "L"
                    MeasurementType.WEIGHT -> "kg"
                }

                val minQty = when (measurement) {
                    MeasurementType.PIECE -> 1
                    MeasurementType.FIXED_PACK -> 1
                    else -> 250
                }

                val maxQty = when (measurement) {
                    MeasurementType.PIECE -> 50
                    MeasurementType.FIXED_PACK -> 25
                    MeasurementType.VOLUME -> 5000
                    MeasurementType.WEIGHT -> 10000
                }

                val stepQty = when (measurement) {
                    MeasurementType.PIECE, MeasurementType.FIXED_PACK -> 1
                    else -> 250
                }

                list.add(
                    Product(
                        productId = id,
                        sku = sku,
                        name = name,
                        categoryId = catId,
                        categoryName = catName,
                        description = desc,
                        images = listOf(image),
                        measurementType = measurement,
                        baseUnit = baseUnit,
                        displayUnit = displayUnit,
                        minQuantity = minQty,
                        maxQuantity = maxQty,
                        quantityIncrement = stepQty,
                        unitPricePaise = price,
                        availableStock = stock,
                        onHandStock = stock,
                        isFeatured = isFeatured,
                        isActive = true
                    )
                )
            }
        }
        return list
    }

    fun getAllCategories(): List<Category> {
        val list = mutableListOf<Category>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, "$COL_CAT_ORDER ASC")
        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndexOrThrow(COL_CAT_ID))
                val name = c.getString(c.getColumnIndexOrThrow(COL_CAT_NAME))
                val desc = c.getString(c.getColumnIndexOrThrow(COL_CAT_DESC))
                val image = c.getString(c.getColumnIndexOrThrow(COL_CAT_IMAGE))
                val order = c.getInt(c.getColumnIndexOrThrow(COL_CAT_ORDER))

                list.add(
                    Category(
                        categoryId = id,
                        name = name,
                        description = desc,
                        imageUrl = image,
                        displayOrder = order,
                        isActive = true
                    )
                )
            }
        }
        return list
    }

    fun getSavedAddresses(userId: String): List<DeliveryAddress> {
        val list = mutableListOf<DeliveryAddress>()
        val db = readableDatabase
        val targetId = if (userId.isNotBlank()) userId else "usr_demo_01"
        val cursor = db.query(
            TABLE_ADDRESSES,
            null,
            "$COL_ADDR_USER_ID = ? OR $COL_ADDR_USER_ID = 'usr_demo_01'",
            arrayOf(targetId),
            null,
            null,
            "$COL_ADDR_IS_DEFAULT DESC"
        )
        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndexOrThrow(COL_ADDR_ID))
                val name = c.getString(c.getColumnIndexOrThrow(COL_ADDR_NAME))
                val phone = c.getString(c.getColumnIndexOrThrow(COL_ADDR_PHONE))
                val line1 = c.getString(c.getColumnIndexOrThrow(COL_ADDR_LINE1))
                val line2 = c.getString(c.getColumnIndexOrThrow(COL_ADDR_LINE2))
                val city = c.getString(c.getColumnIndexOrThrow(COL_ADDR_CITY))
                val pincode = c.getString(c.getColumnIndexOrThrow(COL_ADDR_PINCODE))
                val isDef = c.getInt(c.getColumnIndexOrThrow(COL_ADDR_IS_DEFAULT)) == 1

                list.add(
                    DeliveryAddress(
                        addressId = id,
                        recipientName = name,
                        phoneNumber = phone,
                        addressLine1 = line1,
                        addressLine2 = line2,
                        city = city,
                        pincode = pincode,
                        isDefault = isDef
                    )
                )
            }
        }
        return list
    }

    fun saveAddress(userId: String, address: DeliveryAddress): String {
        val db = writableDatabase
        val targetUser = if (userId.isNotBlank()) userId else "usr_demo_01"
        val cv = ContentValues().apply {
            put(COL_ADDR_ID, address.addressId)
            put(COL_ADDR_USER_ID, targetUser)
            put(COL_ADDR_NAME, address.recipientName)
            put(COL_ADDR_PHONE, address.phoneNumber)
            put(COL_ADDR_LINE1, address.addressLine1)
            put(COL_ADDR_LINE2, address.addressLine2)
            put(COL_ADDR_CITY, address.city)
            put(COL_ADDR_PINCODE, address.pincode)
            put(COL_ADDR_IS_DEFAULT, if (address.isDefault) 1 else 0)
        }
        db.insertWithOnConflict(TABLE_ADDRESSES, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        return address.addressId
    }

    fun deleteAddress(addressId: String) {
        val db = writableDatabase
        db.delete(TABLE_ADDRESSES, "$COL_ADDR_ID = ?", arrayOf(addressId))
    }

    fun getUserProfile(userId: String): UserProfile? {
        val db = readableDatabase
        val targetId = if (userId.isNotBlank()) userId else "usr_demo_01"
        val cursor =
            db.query(TABLE_PROFILE, null, "$COL_USER_ID = ?", arrayOf(targetId), null, null, null)
        cursor.use { c ->
            if (c.moveToFirst()) {
                val id = c.getString(c.getColumnIndexOrThrow(COL_USER_ID))
                val name = c.getString(c.getColumnIndexOrThrow(COL_USER_NAME))
                val email = c.getString(c.getColumnIndexOrThrow(COL_USER_EMAIL))
                val phone = c.getString(c.getColumnIndexOrThrow(COL_USER_PHONE))
                val role = c.getString(c.getColumnIndexOrThrow(COL_USER_ROLE))
                val avatar = c.getString(c.getColumnIndexOrThrow(COL_USER_AVATAR))
                return UserProfile(
                    userId = id,
                    displayName = name,
                    email = email,
                    phoneNumber = phone,
                    role = role,
                    avatarUrl = avatar
                )
            }
        }
        return null
    }

    fun saveUserProfile(profile: UserProfile) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_USER_ID, if (profile.userId.isNotBlank()) profile.userId else "usr_demo_01")
            put(COL_USER_NAME, profile.displayName)
            put(COL_USER_EMAIL, profile.email)
            put(COL_USER_PHONE, profile.phoneNumber)
            put(COL_USER_ROLE, profile.role)
            put(COL_USER_AVATAR, profile.avatarUrl)
        }
        db.insertWithOnConflict(TABLE_PROFILE, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getLocalOrders(customerId: String): List<Order> {
        val list = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ORDERS, null, null, null, null, null, "$COL_ORDER_DATE DESC")
        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndexOrThrow(COL_ORDER_ID))
                val num = c.getString(c.getColumnIndexOrThrow(COL_ORDER_NUM))
                val custId = c.getString(c.getColumnIndexOrThrow(COL_ORDER_CUSTOMER_ID))
                val total = c.getLong(c.getColumnIndexOrThrow(COL_ORDER_TOTAL))
                val statusStr = c.getString(c.getColumnIndexOrThrow(COL_ORDER_STATUS))
                val date = c.getLong(c.getColumnIndexOrThrow(COL_ORDER_DATE))

                val status = try {
                    OrderStatus.valueOf(statusStr)
                } catch (e: Exception) {
                    OrderStatus.PENDING_PAYMENT
                }

                list.add(
                    Order(
                        orderId = id,
                        orderNumber = num,
                        customerId = custId,
                        pricing = OrderPricing(grandTotalPaise = total),
                        status = status,
                        createdAt = date
                    )
                )
            }
        }
        return list
    }

    fun saveOrder(order: Order) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_ORDER_ID, order.orderId)
            put(COL_ORDER_NUM, order.orderNumber)
            put(COL_ORDER_CUSTOMER_ID, order.customerId)
            put(COL_ORDER_TOTAL, order.pricing.grandTotalPaise)
            put(COL_ORDER_STATUS, order.status.name)
            put(COL_ORDER_DATE, order.createdAt)
        }
        db.insertWithOnConflict(TABLE_ORDERS, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }
}
