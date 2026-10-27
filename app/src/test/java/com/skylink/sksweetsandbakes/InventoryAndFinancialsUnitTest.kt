package com.skylink.sksweetsandbakes

import com.skylink.sksweetsandbakes.core.security.RolePermissions
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.domain.cart.usecase.ValidateOrderUseCase
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.CalculateFinancialsUseCase
import com.skylink.sksweetsandbakes.domain.model.CartItem
import com.skylink.sksweetsandbakes.domain.model.Coupon
import com.skylink.sksweetsandbakes.domain.model.DiscountType
import com.skylink.sksweetsandbakes.domain.model.Expense
import com.skylink.sksweetsandbakes.domain.model.ExpenseCategory
import com.skylink.sksweetsandbakes.domain.model.FulfillmentType
import com.skylink.sksweetsandbakes.domain.model.MeasurementType
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.model.OrderPricing
import com.skylink.sksweetsandbakes.domain.model.OrderStatus
import com.skylink.sksweetsandbakes.domain.model.PosSale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InventoryAndFinancialsUnitTest {

    private lateinit var validateOrderUseCase: ValidateOrderUseCase
    private lateinit var calculateFinancialsUseCase: CalculateFinancialsUseCase

    @Before
    fun setUp() {
        validateOrderUseCase = ValidateOrderUseCase()
        calculateFinancialsUseCase = CalculateFinancialsUseCase()
    }

    @Test
    fun testOrderPricingCalculation_WithPercentageCoupon() {
        val items = listOf(
            CartItem(
                productId = "prod_kaju",
                productName = "Kaju Katli",
                measurementType = MeasurementType.WEIGHT,
                selectedQuantity = 500, // 500g
                unitPricePaise = 96000L // ₹960/kg -> 48,000 paise (₹480)
            ),
            CartItem(
                productId = "prod_mysore",
                productName = "Mysore Pak",
                measurementType = MeasurementType.WEIGHT,
                selectedQuantity = 500, // 500g
                unitPricePaise = 74000L // ₹740/kg -> 37,000 paise (₹370)
            )
        )

        // Subtotal = 48,000 + 37,000 = 85,000 paise (₹850)
        // Coupon: 10% off (8,500 paise discount)
        // Subtotal after discount = 76,500 paise
        // Delivery fee: ₹40 (4,000 paise) since subtotal < ₹1,000
        // Packaging fee: ₹15 (1,500 paise)
        // Tax: 5% of 76,500 = 3,825 paise
        // Grand total: 76,500 + 4,000 + 1,500 + 3,825 = 85,825 paise (₹858.25)
        val coupon = Coupon(
            code = "FESTIVE10",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 10,
            maxDiscountPaise = 15000L,
            minOrderValuePaise = 50000L,
            isActive = true
        )

        val pricing = validateOrderUseCase.calculateCartPricing(
            items = items,
            fulfillmentType = FulfillmentType.DELIVERY,
            appliedCoupon = coupon
        )

        assertEquals(85000L, pricing.subtotalPaise)
        assertEquals(8500L, pricing.discountPaise)
        assertEquals(4000L, pricing.deliveryFeePaise)
        assertEquals(1500L, pricing.packagingFeePaise)
        assertEquals(3825L, pricing.taxPaise)
        assertEquals(85825L, pricing.grandTotalPaise)
    }

    @Test
    fun testFinancialStatements_GrossProfit_OperatingProfit() {
        val orders = listOf(
            Order(
                orderId = "ord_1",
                status = OrderStatus.CONFIRMED,
                pricing = OrderPricing(
                    subtotalPaise = 100000L, // ₹1,000
                    discountPaise = 10000L  // ₹100
                )
            )
        )
        val posSales = listOf(
            PosSale(
                saleId = "pos_1",
                subtotalPaise = 50000L, // ₹500
                discountPaise = 0L,
                totalPaise = 50000L
            )
        )
        val expenses = listOf(
            Expense(
                expenseId = "exp_1",
                category = ExpenseCategory.RENT,
                amountPaise = 20000L // ₹200
            )
        )

        // Total Gross = 100,000 + 50,000 = 150,000 paise (₹1,500)
        // Total Discounts = 10,000 paise
        // Net Sales = 140,000 paise (₹1,400)
        // COGS @ 60% = 84,000 paise (₹840)
        // Gross Profit = 140,000 - 84,000 = 56,000 paise (₹560)
        // Operating Profit = 56,000 - 20,000 = 36,000 paise (₹360)
        val statement = calculateFinancialsUseCase.generateStatement(
            orders = orders,
            posSales = posSales,
            expenses = expenses,
            estimatedCogsRatio = 0.60
        )

        assertEquals(150000L, statement.grossSalesPaise)
        assertEquals(10000L, statement.discountsPaise)
        assertEquals(140000L, statement.netSalesPaise)
        assertEquals(84000L, statement.cogsPaise)
        assertEquals(56000L, statement.grossProfitPaise)
        assertEquals(20000L, statement.operatingExpensesPaise)
        assertEquals(36000L, statement.operatingProfitPaise)
    }

    @Test
    fun testRoleSecurityPermissions_CashierCannotViewExpensesOrProfits() {
        val cashierRole = UserRole.CASHIER
        val ownerRole = UserRole.OWNER
        val managerRole = UserRole.MANAGER

        // Cashier permissions
        assertTrue(RolePermissions.canProcessPos(cashierRole))
        assertFalse(RolePermissions.canViewExpenses(cashierRole))
        assertFalse(RolePermissions.canViewFinancialReports(cashierRole))
        assertFalse(RolePermissions.canManageStaff(cashierRole))

        // Owner permissions
        assertTrue(RolePermissions.canProcessPos(ownerRole))
        assertTrue(RolePermissions.canViewExpenses(ownerRole))
        assertTrue(RolePermissions.canViewFinancialReports(ownerRole))
        assertTrue(RolePermissions.canManageStaff(ownerRole))

        // Manager permissions
        assertTrue(RolePermissions.canProcessPos(managerRole))
        assertTrue(RolePermissions.canEditProductsAndPricing(managerRole))
        assertFalse(RolePermissions.canViewExpenses(managerRole))
        assertFalse(RolePermissions.canManageStaff(managerRole))
    }
}
