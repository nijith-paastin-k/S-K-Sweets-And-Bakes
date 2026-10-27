package com.skylink.sksweetsandbakes.data.seed

import com.skylink.sksweetsandbakes.core.util.ImageUtils
import com.skylink.sksweetsandbakes.domain.model.Category
import com.skylink.sksweetsandbakes.domain.model.Coupon
import com.skylink.sksweetsandbakes.domain.model.DiscountType
import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.model.ExpenseCategory
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Product
import com.skylink.sksweetsandbakes.domain.model.Supplier

object SeedData {

    val categories = listOf(
        Category(
            categoryId = "cat_ghee_sweets",
            name = "Traditional Ghee Sweets",
            description = "Rich, aromatic sweets prepared with pure desi cow ghee",
            imageUrl = ImageUtils.getDrawableUri("img_assorted_ghee_sweets"),
            displayOrder = 1,
            isActive = true
        ),
        Category(
            categoryId = "cat_kaju_sweets",
            name = "Kaju & Dry Fruit Sweets",
            description = "Finest cashew and dry fruit delicacies with silver leaf",
            imageUrl = ImageUtils.getDrawableUri("img_kaju_katli_brass_plate"),
            displayOrder = 2,
            isActive = true
        ),
        Category(
            categoryId = "cat_bengali_sweets",
            name = "Bengali Sweets",
            description = "Soft, spongy cottage cheese sweets made fresh every morning",
            imageUrl = ImageUtils.getDrawableUri("img_gulab_jamun_rasmalai_brass"),
            displayOrder = 3,
            isActive = true
        ),
        Category(
            categoryId = "cat_savouries",
            name = "Hot & Crispy Savouries",
            description = "Crispy South Indian mixtures, murukku, and samosas",
            imageUrl = ImageUtils.getDrawableUri("img_murukku_mixture_combo"),
            displayOrder = 4,
            isActive = true
        ),
        Category(
            categoryId = "cat_bakery",
            name = "Bakes & Cakes",
            description = "Freshly baked cookies, eggless pastries, and rusks",
            imageUrl = ImageUtils.getDrawableUri("img_crispy_pastries_plate"),
            displayOrder = 5,
            isActive = true
        ),
        Category(
            categoryId = "cat_festive_packs",
            name = "Festive Gift Boxes",
            description = "Grand festive hampers and assorted sweet packs for celebrations",
            imageUrl = ImageUtils.getDrawableUri("img_royal_mithai_gift_box"),
            displayOrder = 6,
            isActive = true
        )
    )

    val products = listOf(
        Product(
            productId = "prod_kaju_katli",
            sku = "SKS-KAJU-001",
            name = "Kaju Katli Special",
            categoryId = "cat_kaju_sweets",
            categoryName = "Kaju & Dry Fruit Sweets",
            description = "Mouth-melting diamond cut cashew fudge made with premium Goan cashews and pure edible silver vark.",
            images = listOf(
                ImageUtils.getDrawableUri("img_kaju_katli_pyramid"),
                ImageUtils.getDrawableUri("img_kaju_katli_brass_plate")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 96000L,
            minQuantity = 250,
            maxQuantity = 10000,
            quantityIncrement = 250,
            shelfLifeDays = 20,
            storageInstructions = "Store in a cool dry place. Keep refrigerated after opening.",
            ingredients = listOf("Goan Cashews", "Pure Sugar", "Silver Vark", "Cardamom"),
            allergens = listOf("Tree Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 25000,
            availableStock = 25000,
            lowStockThreshold = 5000
        ),
        Product(
            productId = "prod_kaju_roll",
            sku = "SKS-KAJU-013",
            name = "Pista Stuffed Kaju Roll",
            categoryId = "cat_kaju_sweets",
            categoryName = "Kaju & Dry Fruit Sweets",
            description = "Silky cashew paste cylinder wrapped around a luscious emerald green pistachio filling with silver vark.",
            images = listOf(
                ImageUtils.getDrawableUri("img_kaju_katli_brass_plate"),
                ImageUtils.getDrawableUri("img_kaju_katli_pyramid")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 105000L,
            minQuantity = 250,
            maxQuantity = 10000,
            quantityIncrement = 250,
            shelfLifeDays = 20,
            storageInstructions = "Store in cool dry conditions.",
            ingredients = listOf("Cashews", "Pistachios", "Sugar", "Silver Vark"),
            allergens = listOf("Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 18000,
            availableStock = 18000,
            lowStockThreshold = 3000
        ),
        Product(
            productId = "prod_pepper_cashews",
            sku = "SKS-KAJU-015",
            name = "Pepper-Spiced Roasted Cashews",
            categoryId = "cat_kaju_sweets",
            categoryName = "Kaju & Dry Fruit Sweets",
            description = "Jumbo whole cashews slow-roasted in pure ghee and dusted with Malabar black pepper and pink rock salt.",
            images = listOf(
                ImageUtils.getDrawableUri("img_pepper_cashews")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 120000L,
            minQuantity = 200,
            maxQuantity = 5000,
            quantityIncrement = 200,
            shelfLifeDays = 45,
            storageInstructions = "Store in an airtight container.",
            ingredients = listOf("Jumbo Cashews", "Pure Cow Ghee", "Black Pepper", "Rock Salt"),
            allergens = listOf("Tree Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 20000,
            availableStock = 20000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_mysore_pak",
            sku = "SKS-GHEE-002",
            name = "Desi Ghee Mysore Pak",
            categoryId = "cat_ghee_sweets",
            categoryName = "Traditional Ghee Sweets",
            description = "Traditional royal recipe with porous, melt-in-mouth texture infused with aromatic desi cow ghee.",
            images = listOf(
                ImageUtils.getDrawableUri("img_mysore_pak_brass_plate"),
                ImageUtils.getDrawableUri("img_mysore_pak_namkeen_combo")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 78000L,
            minQuantity = 250,
            maxQuantity = 10000,
            quantityIncrement = 250,
            shelfLifeDays = 25,
            storageInstructions = "Store at room temperature in an airtight container.",
            ingredients = listOf("Gram Flour (Besan)", "Desi Cow Ghee", "Sugar", "Cardamom"),
            allergens = listOf("Dairy"),
            isFeatured = true,
            isActive = true,
            onHandStock = 30000,
            availableStock = 30000,
            lowStockThreshold = 5000
        ),
        Product(
            productId = "prod_motichoor_laddu",
            sku = "SKS-GHEE-003",
            name = "Pure Ghee Motichoor Laddu",
            categoryId = "cat_ghee_sweets",
            categoryName = "Traditional Ghee Sweets",
            description = "Tiny golden gram flour pearls fried in cow ghee, soaked in fragrant saffron syrup and shaped with melon seeds.",
            images = listOf(
                ImageUtils.getDrawableUri("img_motichoor_ladoo"),
                ImageUtils.getDrawableUri("img_assorted_ghee_sweets")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 64000L,
            minQuantity = 250,
            maxQuantity = 10000,
            quantityIncrement = 250,
            shelfLifeDays = 10,
            storageInstructions = "Consume within 10 days. Keep in a dry container.",
            ingredients = listOf("Besan Pearls", "Desi Ghee", "Saffron", "Melon Seeds", "Cardamom"),
            allergens = listOf("Dairy"),
            isFeatured = true,
            isActive = true,
            onHandStock = 20000,
            availableStock = 20000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_jalebi",
            sku = "SKS-GHEE-004",
            name = "Glossy Desi Ghee Jalebi",
            categoryId = "cat_ghee_sweets",
            categoryName = "Traditional Ghee Sweets",
            description = "Crispy, juicy pretzel-shaped spirals fried in pure ghee and steeped in saffron-scented sugar nectar.",
            images = listOf(
                ImageUtils.getDrawableUri("img_jalebi_brass_plate")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 52000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 3,
            storageInstructions = "Best consumed hot and fresh on the same day.",
            ingredients = listOf(
                "Refined Wheat Flour",
                "Pure Ghee",
                "Sugar Syrup",
                "Saffron",
                "Rose Essence"
            ),
            allergens = listOf("Gluten", "Dairy"),
            isFeatured = true,
            isActive = true,
            onHandStock = 15000,
            availableStock = 15000,
            lowStockThreshold = 3000
        ),
        Product(
            productId = "prod_badam_halwa",
            sku = "SKS-GHEE-005",
            name = "Saffron Almond Badam Halwa",
            categoryId = "cat_ghee_sweets",
            categoryName = "Traditional Ghee Sweets",
            description = "Decadent royal halwa made with stone-ground California almonds, pure ghee, and Kashmiri saffron threads.",
            images = listOf(
                ImageUtils.getDrawableUri("img_badam_halwa"),
                ImageUtils.getDrawableUri("img_festive_brass_thali")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 135000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 15,
            storageInstructions = "Refrigerate after opening. Warm gently before serving.",
            ingredients = listOf("Almonds", "Pure Ghee", "Saffron", "Sugar", "Cardamom"),
            allergens = listOf("Tree Nuts", "Dairy"),
            isFeatured = true,
            isActive = true,
            onHandStock = 12000,
            availableStock = 12000,
            lowStockThreshold = 2500
        ),
        Product(
            productId = "prod_soan_papdi",
            sku = "SKS-GHEE-006",
            name = "Flaky Desi Ghee Soan Papdi",
            categoryId = "cat_ghee_sweets",
            categoryName = "Traditional Ghee Sweets",
            description = "Crisp, flaky, ribbon-textured confection with roasted pistachios, almonds, and aromatic green cardamom.",
            images = listOf(
                ImageUtils.getDrawableUri("img_soan_papdi")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 56000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 60,
            storageInstructions = "Store in an airtight container away from moisture.",
            ingredients = listOf(
                "Gram Flour",
                "Refined Flour",
                "Ghee",
                "Sugar",
                "Pistachios",
                "Almonds"
            ),
            allergens = listOf("Dairy", "Nuts", "Gluten"),
            isFeatured = false,
            isActive = true,
            onHandStock = 25000,
            availableStock = 25000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_rasmalai",
            sku = "SKS-BEN-012",
            name = "Royal Kesari Rasmalai",
            categoryId = "cat_bengali_sweets",
            categoryName = "Bengali Sweets",
            description = "Delicate flattened chenna discs immersed in chilled thick saffron milk garnished with slivered pistachios.",
            images = listOf(
                ImageUtils.getDrawableUri("img_saffron_rasmalai"),
                ImageUtils.getDrawableUri("img_gulab_jamun_rasmalai_delight"),
                ImageUtils.getDrawableUri("img_gulab_jamun_rasmalai_brass")
            ),
            measurementType = MeasurementType.PIECE,
            baseUnit = "piece",
            displayUnit = "piece",
            unitPricePaise = 4500L,
            minQuantity = 2,
            maxQuantity = 30,
            quantityIncrement = 1,
            shelfLifeDays = 2,
            storageInstructions = "Must be refrigerated immediately below 4°C.",
            ingredients = listOf(
                "Fresh Chenna",
                "Whole Milk",
                "Kashmiri Saffron",
                "Pistachios",
                "Sugar"
            ),
            allergens = listOf("Dairy", "Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 120,
            availableStock = 120,
            lowStockThreshold = 20
        ),
        Product(
            productId = "prod_gulab_jamun",
            sku = "SKS-BEN-015",
            name = "Glistening Gulab Jamun",
            categoryId = "cat_bengali_sweets",
            categoryName = "Bengali Sweets",
            description = "Mawa and paneer spheres fried until deep golden and simmered in aromatic cardamom and rose chashni.",
            images = listOf(
                ImageUtils.getDrawableUri("img_gulab_jamun_brass_bowl"),
                ImageUtils.getDrawableUri("img_gulab_jamun_rasmalai_combo")
            ),
            measurementType = MeasurementType.PIECE,
            baseUnit = "piece",
            displayUnit = "piece",
            unitPricePaise = 3500L,
            minQuantity = 2,
            maxQuantity = 30,
            quantityIncrement = 1,
            shelfLifeDays = 5,
            storageInstructions = "Serve warm with syrup. Keep refrigerated.",
            ingredients = listOf(
                "Khoya (Mawa)",
                "Paneer",
                "Ghee",
                "Sugar Syrup",
                "Rose Water",
                "Cardamom"
            ),
            allergens = listOf("Dairy"),
            isFeatured = true,
            isActive = true,
            onHandStock = 150,
            availableStock = 150,
            lowStockThreshold = 25
        ),
        Product(
            productId = "prod_kalakand",
            sku = "SKS-BEN-016",
            name = "Garnished Malai Kalakand",
            categoryId = "cat_bengali_sweets",
            categoryName = "Bengali Sweets",
            description = "Classic moist, granular milk fudge delicately flavored with cardamom and crowned with pistachio flakes.",
            images = listOf(
                ImageUtils.getDrawableUri("img_kalakand_ceramic_plate")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 72000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 4,
            storageInstructions = "Keep refrigerated.",
            ingredients = listOf("Full Cream Milk", "Chenna", "Sugar", "Cardamom", "Pistachios"),
            allergens = listOf("Dairy", "Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 20000,
            availableStock = 20000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_saffron_peda",
            sku = "SKS-BEN-017",
            name = "Pistachio-Saffron Mathura Peda",
            categoryId = "cat_bengali_sweets",
            categoryName = "Bengali Sweets",
            description = "Traditional slow-caramelized mawa rounds dusted with ground cardamom and topped with toasted pistachios.",
            images = listOf(
                ImageUtils.getDrawableUri("img_saffron_peda_platter")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 68000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 15,
            storageInstructions = "Store in a cool dry place.",
            ingredients = listOf("Mawa", "Sugar", "Saffron", "Pistachios", "Cardamom"),
            allergens = listOf("Dairy", "Nuts"),
            isFeatured = false,
            isActive = true,
            onHandStock = 22000,
            availableStock = 22000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_rasgulla",
            sku = "SKS-BEN-004",
            name = "Kolkata Spongy Rasgulla",
            categoryId = "cat_bengali_sweets",
            categoryName = "Bengali Sweets",
            description = "Feather-light cottage cheese balls cooked in light bubbling sugar syrup till springy and soft.",
            images = listOf(
                ImageUtils.getDrawableUri("img_gulab_jamun_rasmalai_brass")
            ),
            measurementType = MeasurementType.PIECE,
            baseUnit = "piece",
            displayUnit = "piece",
            unitPricePaise = 3000L,
            minQuantity = 2,
            maxQuantity = 30,
            quantityIncrement = 1,
            shelfLifeDays = 3,
            storageInstructions = "Store chilled in syrup. Do not freeze.",
            ingredients = listOf("Cow Milk Chenna", "Sugar", "Water", "Rose Water"),
            allergens = listOf("Dairy"),
            isFeatured = false,
            isActive = true,
            onHandStock = 100,
            availableStock = 100,
            lowStockThreshold = 20
        ),
        Product(
            productId = "prod_samosa",
            sku = "SKS-SAV-018",
            name = "Golden Crispy Punjabi Samosas",
            categoryId = "cat_savouries",
            categoryName = "Hot & Crispy Savouries",
            description = "Flaky golden triangular pastry stuffed with spiced potato, sweet green peas, and whole roasted coriander.",
            images = listOf(
                ImageUtils.getDrawableUri("img_golden_samosas"),
                ImageUtils.getDrawableUri("img_samosa_filter_coffee")
            ),
            measurementType = MeasurementType.PIECE,
            baseUnit = "piece",
            displayUnit = "piece",
            unitPricePaise = 2500L,
            minQuantity = 2,
            maxQuantity = 50,
            quantityIncrement = 1,
            shelfLifeDays = 1,
            storageInstructions = "Serve hot with mint and tamarind chutneys.",
            ingredients = listOf(
                "Wheat Flour",
                "Potatoes",
                "Green Peas",
                "Coriander",
                "Cumin",
                "Garam Masala"
            ),
            allergens = listOf("Gluten"),
            isFeatured = true,
            isActive = true,
            onHandStock = 200,
            availableStock = 200,
            lowStockThreshold = 30
        ),
        Product(
            productId = "prod_murukku",
            sku = "SKS-SAV-014",
            name = "Butter Kai Murukku Spirals",
            categoryId = "cat_savouries",
            categoryName = "Hot & Crispy Savouries",
            description = "Crispy hand-twisted traditional rice and urad dal savory spirals fried in pure groundnut oil with white butter.",
            images = listOf(
                ImageUtils.getDrawableUri("img_murukku_spiral_stack"),
                ImageUtils.getDrawableUri("img_murukku_mixture_combo")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 42000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 40,
            storageInstructions = "Keep tightly sealed in an airtight jar.",
            ingredients = listOf("Rice Flour", "Urad Dal", "White Butter", "Cumin", "Asafoetida"),
            allergens = listOf("Dairy"),
            isFeatured = true,
            isActive = true,
            onHandStock = 25000,
            availableStock = 25000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_special_mixture",
            sku = "SKS-SAV-005",
            name = "Royal South Indian Mixture",
            categoryId = "cat_savouries",
            categoryName = "Hot & Crispy Savouries",
            description = "A crunchy festive blend of crispy sev, boondi, fried cashews, peanuts, and roasted curry leaves.",
            images = listOf(
                ImageUtils.getDrawableUri("img_south_indian_mixture"),
                ImageUtils.getDrawableUri("img_murukku_mixture_combo")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 46000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 45,
            storageInstructions = "Keep in an airtight container.",
            ingredients = listOf(
                "Gram Flour",
                "Cashews",
                "Groundnuts",
                "Curry Leaves",
                "Chili Powder",
                "Asafoetida"
            ),
            allergens = listOf("Nuts", "Peanuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 35000,
            availableStock = 35000,
            lowStockThreshold = 5000
        ),
        Product(
            productId = "prod_spicy_boondi",
            sku = "SKS-SAV-019",
            name = "Spicy Kara Boondi Mixture",
            categoryId = "cat_savouries",
            categoryName = "Hot & Crispy Savouries",
            description = "Crunchy spiced besan drops tossed with roasted peanuts, curry leaves, garlic, and hot red chili.",
            images = listOf(
                ImageUtils.getDrawableUri("img_spicy_boondi_mixture")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 40000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 45,
            storageInstructions = "Store in an airtight container.",
            ingredients = listOf("Besan", "Peanuts", "Curry Leaves", "Garlic", "Red Chili", "Salt"),
            allergens = listOf("Peanuts"),
            isFeatured = false,
            isActive = true,
            onHandStock = 28000,
            availableStock = 28000,
            lowStockThreshold = 4000
        ),
        Product(
            productId = "prod_banana_chips",
            sku = "SKS-SAV-020",
            name = "Kerala Golden Banana Chips",
            categoryId = "cat_savouries",
            categoryName = "Hot & Crispy Savouries",
            description = "Thinly sliced raw Nendran plantains fried in pure cold-pressed coconut oil and sprinkled with sea salt.",
            images = listOf(
                ImageUtils.getDrawableUri("img_banana_chips_brass_bowl")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 48000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 30,
            storageInstructions = "Keep sealed away from moisture.",
            ingredients = listOf("Nendran Bananas", "Pure Coconut Oil", "Turmeric", "Sea Salt"),
            allergens = emptyList(),
            isFeatured = false,
            isActive = true,
            onHandStock = 20000,
            availableStock = 20000,
            lowStockThreshold = 3500
        ),
        Product(
            productId = "prod_ribbon_pakoda",
            sku = "SKS-SAV-021",
            name = "Golden Ribbon Pakoda",
            categoryId = "cat_savouries",
            categoryName = "Hot & Crispy Savouries",
            description = "Melt-in-mouth ribbon-shaped savory crisps prepared with seasoned rice flour, butter, and cumin.",
            images = listOf(
                ImageUtils.getDrawableUri("img_ribbon_pakoda")
            ),
            measurementType = MeasurementType.WEIGHT,
            baseUnit = "g",
            displayUnit = "kg",
            unitPricePaise = 42000L,
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            shelfLifeDays = 35,
            storageInstructions = "Store in an airtight box.",
            ingredients = listOf(
                "Rice Flour",
                "Besan",
                "Butter",
                "Chili Powder",
                "Sesame Seeds",
                "Asafoetida"
            ),
            allergens = listOf("Dairy", "Sesame"),
            isFeatured = false,
            isActive = true,
            onHandStock = 22000,
            availableStock = 22000,
            lowStockThreshold = 3500
        ),
        Product(
            productId = "prod_pastries",
            sku = "SKS-BAK-022",
            name = "Golden Crispy Eggless Pastries",
            categoryId = "cat_bakery",
            categoryName = "Bakes & Cakes",
            description = "Thousand-layer golden puff pastries baked to crispy perfection with sweet cardamom and almond dust.",
            images = listOf(
                ImageUtils.getDrawableUri("img_crispy_pastries_plate"),
                ImageUtils.getDrawableUri("img_eggless_pastries")
            ),
            measurementType = MeasurementType.FIXED_PACK,
            baseUnit = "pack",
            displayUnit = "pack",
            unitPricePaise = 22000L,
            minQuantity = 1,
            maxQuantity = 10,
            quantityIncrement = 1,
            shelfLifeDays = 10,
            storageInstructions = "Store in an airtight tin.",
            ingredients = listOf("Refined Flour", "Butter", "Cardamom", "Sugar Glaze", "Almonds"),
            allergens = listOf("Gluten", "Dairy", "Nuts"),
            isFeatured = false,
            isActive = true,
            onHandStock = 30,
            availableStock = 30,
            lowStockThreshold = 5
        ),
        Product(
            productId = "prod_festive_box_royal",
            sku = "SKS-BOX-007",
            name = "Royal Festive Assorted Sweet Box (1 kg)",
            categoryId = "cat_festive_packs",
            categoryName = "Festive Gift Boxes",
            description = "Luxury gold-foiled gift box containing 250g Kaju Katli, 250g Mysore Pak, 250g Motichoor Laddu, and 250g Badam Halwa.",
            images = listOf(
                ImageUtils.getDrawableUri("img_royal_mithai_gift_box"),
                ImageUtils.getDrawableUri("img_luxurious_sweets_box"),
                ImageUtils.getDrawableUri("banner_festive_diwali_box")
            ),
            measurementType = MeasurementType.FIXED_PACK,
            baseUnit = "pack",
            displayUnit = "pack",
            unitPricePaise = 115000L,
            minQuantity = 1,
            maxQuantity = 25,
            quantityIncrement = 1,
            shelfLifeDays = 15,
            storageInstructions = "Store in a cool dry place.",
            ingredients = listOf("Cashews", "Ghee", "Milk", "Dry Fruits", "Sugar"),
            allergens = listOf("Dairy", "Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 50,
            availableStock = 50,
            lowStockThreshold = 10
        ),
        Product(
            productId = "prod_diwali_hamper",
            sku = "SKS-BOX-023",
            name = "Grand Diwali Sweets & Dry Fruits Hamper (1.5 kg)",
            categoryId = "cat_festive_packs",
            categoryName = "Festive Gift Boxes",
            description = "The ultimate celebration hamper featuring handcrafted ghee sweets, roasted cashews, badam halwa, and festive diya lights.",
            images = listOf(
                ImageUtils.getDrawableUri("img_luxury_sweets_nuts_box"),
                ImageUtils.getDrawableUri("img_diwali_maroon_box_tray"),
                ImageUtils.getDrawableUri("banner_diwali_diyas")
            ),
            measurementType = MeasurementType.FIXED_PACK,
            baseUnit = "pack",
            displayUnit = "pack",
            unitPricePaise = 185000L,
            minQuantity = 1,
            maxQuantity = 20,
            quantityIncrement = 1,
            shelfLifeDays = 20,
            storageInstructions = "Store in a cool dry area away from direct sunlight.",
            ingredients = listOf(
                "Kaju Katli",
                "Mysore Pak",
                "Roasted Cashews",
                "Badam Halwa",
                "Festive Diyas"
            ),
            allergens = listOf("Dairy", "Tree Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 40,
            availableStock = 40,
            lowStockThreshold = 8
        ),
        Product(
            productId = "prod_luxury_sweet_box",
            sku = "SKS-BOX-024",
            name = "Regal Gold Sweets Gift Box (750 g)",
            categoryId = "cat_festive_packs",
            categoryName = "Festive Gift Boxes",
            description = "Artisanal assortment of silver vark kaju katli, badam halwa, and rich ghee peda in an ornate keepsake brass box.",
            images = listOf(
                ImageUtils.getDrawableUri("img_luxury_sweet_box"),
                ImageUtils.getDrawableUri("img_luxurious_sweets_box_alt")
            ),
            measurementType = MeasurementType.FIXED_PACK,
            baseUnit = "pack",
            displayUnit = "pack",
            unitPricePaise = 95000L,
            minQuantity = 1,
            maxQuantity = 30,
            quantityIncrement = 1,
            shelfLifeDays = 15,
            storageInstructions = "Keep in ambient room temperature.",
            ingredients = listOf("Cashews", "Desi Ghee", "Almonds", "Saffron", "Cardamom"),
            allergens = listOf("Dairy", "Tree Nuts"),
            isFeatured = true,
            isActive = true,
            onHandStock = 60,
            availableStock = 60,
            lowStockThreshold = 12
        )
    )

    val coupons = listOf(
        Coupon(
            couponId = "coup_diwali",
            code = "FESTIVE10",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 10,
            maxDiscountPaise = 15000L,
            minOrderValuePaise = 50000L,
            isActive = true
        ),
        Coupon(
            couponId = "coup_sweet50",
            code = "SWEET50",
            discountType = DiscountType.FLAT,
            discountValue = 50,
            maxDiscountPaise = 5000L,
            minOrderValuePaise = 30000L,
            isActive = true
        )
    )

    val suppliers = listOf(
        Supplier(
            supplierId = "sup_dairy_01",
            name = "Nandini Milk & Dairy Products",
            contactPerson = "S. Murthy",
            phoneNumber = "+91 80 2839 4001",
            email = "procurement@nandinidairy.com",
            address = "KMF Dairy Complex, Bengaluru",
            gstin = "29AAACN1234A1Z5"
        ),
        Supplier(
            supplierId = "sup_cashew_02",
            name = "Goa Cashew Traders & Processing Ltd",
            contactPerson = "R. Naik",
            phoneNumber = "+91 832 245 6789",
            email = "sales@goacashewtraders.com",
            address = "Industrial Estate, Panaji, Goa",
            gstin = "30AAACG5678B2Z1"
        )
    )

    val sampleExpenses = listOf(
        Expense(
            expenseId = "exp_001",
            category = ExpenseCategory.PACKAGING,
            title = "500 Gold Embossed Sweet Gift Boxes",
            amountPaise = 1500000L,
            date = "2026-10-22",
            paidTo = "Royal Packaging Ltd",
            recordedBy = "Manager",
            createdAt = System.currentTimeMillis() - 86400000L
        ),
        Expense(
            expenseId = "exp_002",
            category = ExpenseCategory.ELECTRICITY,
            title = "Commercial Sweet Kitchen Power Bill",
            amountPaise = 2400000L,
            date = "2026-10-20",
            paidTo = "Electricity Board",
            recordedBy = "Owner",
            createdAt = System.currentTimeMillis() - 86400000L * 4
        )
    )
}
