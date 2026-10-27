package com.skylink.sksweetsandbakes

import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculatePricingUseCase
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Authoritative unit tests for Section 7: Product Measurement and Pricing.
 * Tests weight, volume, piece, and fixed pack decimal-safe integer paise arithmetic.
 */
class PricingEngineUnitTest {

    private lateinit var pricingUseCase: CalculatePricingUseCase

    @Before
    fun setUp() {
        pricingUseCase = CalculatePricingUseCase()
    }

    @Test
    fun testWeightBasedPricing_250g_at_800_per_kg() {
        // ₹800 per kg = 80,000 paise. 250g = (250 * 80000) / 1000 = 20,000 paise (₹200)
        val paise = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.WEIGHT,
            unitPricePaise = 80000L,
            quantity = 250
        )
        assertEquals(20000L, paise)
    }

    @Test
    fun testWeightBasedPricing_500g_at_960_per_kg() {
        // ₹960 per kg = 96,000 paise. 500g = 48,000 paise (₹480)
        val paise = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.WEIGHT,
            unitPricePaise = 96000L,
            quantity = 500
        )
        assertEquals(48000L, paise)
    }

    @Test
    fun testWeightBasedPricing_750g_at_600_per_kg() {
        // ₹600 per kg = 60,000 paise. 750g = 45,000 paise (₹450)
        val paise = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.WEIGHT,
            unitPricePaise = 60000L,
            quantity = 750
        )
        assertEquals(45000L, paise)
    }

    @Test
    fun testWeightBasedPricing_1kg_125kg_15kg_2kg_225kg_25kg_275kg_3kg() {
        val pricePerKgPaise = 74000L // Mysore pak at ₹740/kg

        assertEquals(
            74000L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 1000)
        )
        assertEquals(
            92500L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 1250)
        )
        assertEquals(
            111000L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 1500)
        )
        assertEquals(
            129500L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 1750)
        )
        assertEquals(
            148000L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 2000)
        )
        assertEquals(
            166500L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 2250)
        )
        assertEquals(
            185000L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 2500)
        )

        // 2.75 kg = 2,750g = (2750 * 74000) / 1000 = 203,500 paise (₹2,035.00)
        assertEquals(
            203500L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 2750)
        )

        // 3 kg = 3,000g = (3000 * 74000) / 1000 = 222,000 paise (₹2,220.00)
        assertEquals(
            222000L,
            pricingUseCase.calculateLineTotalPaise(MeasurementType.WEIGHT, pricePerKgPaise, 3000)
        )
    }

    @Test
    fun testWeightDisplayFormatting() {
        assertEquals("500 g", pricingUseCase.formatWeightDisplay(500))
        assertEquals("1 kg", pricingUseCase.formatWeightDisplay(1000))
        assertEquals("2.75 kg", pricingUseCase.formatWeightDisplay(2750))
        assertEquals("3 kg", pricingUseCase.formatWeightDisplay(3000))
        assertEquals("3.5 kg", pricingUseCase.formatWeightDisplay(3500))
    }

    @Test
    fun testVolumeBasedPricing_BadamMilk() {
        // ₹240 per Litre = 24,000 paise. 500ml = 12,000 paise (₹120)
        val price500ml = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.VOLUME,
            unitPricePaise = 24000L,
            quantity = 500
        )
        assertEquals(12000L, price500ml)

        // 1 Litre (1000ml) = 24,000 paise
        val price1L = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.VOLUME,
            unitPricePaise = 24000L,
            quantity = 1000
        )
        assertEquals(24000L, price1L)
    }

    @Test
    fun testPieceBasedPricing_Rasgulla() {
        // ₹30 per piece = 3,000 paise. 6 pieces = 18,000 paise (₹180)
        val price6Pieces = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.PIECE,
            unitPricePaise = 3000L,
            quantity = 6
        )
        assertEquals(18000L, price6Pieces)
    }

    @Test
    fun testFixedPackPricing_GiftBox() {
        // ₹1,150 per pack = 115,000 paise. 3 packs = 345,000 paise (₹3,450)
        val price3Packs = pricingUseCase.calculateLineTotalPaise(
            measurementType = MeasurementType.FIXED_PACK,
            unitPricePaise = 115000L,
            quantity = 3
        )
        assertEquals(345000L, price3Packs)
    }

    @Test
    fun testQuantityValidation_Min_Max_Increments() {
        val product = Product(
            productId = "test_p1",
            minQuantity = 250,
            maxQuantity = 5000,
            quantityIncrement = 250,
            baseUnit = "g"
        )

        // Valid
        assertTrue(pricingUseCase.validateQuantity(product, 250).isSuccess)
        assertTrue(pricingUseCase.validateQuantity(product, 500).isSuccess)
        assertTrue(pricingUseCase.validateQuantity(product, 2750).isSuccess)
        assertTrue(pricingUseCase.validateQuantity(product, 3000).isSuccess)
        assertTrue(pricingUseCase.validateQuantity(product, 5000).isSuccess)

        // Below min
        assertTrue(pricingUseCase.validateQuantity(product, 200).isFailure)

        // Above max
        assertTrue(pricingUseCase.validateQuantity(product, 5250).isFailure)

        // Invalid increment (e.g. 333g when step is 250g)
        assertTrue(pricingUseCase.validateQuantity(product, 333).isFailure)
    }
}
