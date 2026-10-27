package com.skylink.sksweetsandbakes.data.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.PosSale
import com.skylink.sksweetsandbakes.domain.model.PosSaleItem
import com.skylink.sksweetsandbakes.domain.repository.InventoryRepository
import com.skylink.sksweetsandbakes.domain.repository.PosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PosRepositoryImpl(
    private val inventoryRepository: InventoryRepository
) : PosRepository {

    private val _sales = MutableStateFlow<List<PosSale>>(
        listOf(
            PosSale(
                saleId = "pos_1001",
                saleNumber = "POS-20261023-001",
                cashierId = "staff_02",
                cashierName = "Cashier Counter 1",
                items = listOf(
                    PosSaleItem(
                        productId = "prod_mysore_pak",
                        name = "Desi Ghee Mysore Pak",
                        quantity = 500,
                        unit = "g",
                        unitPricePaise = 74000L,
                        lineTotalPaise = 37000L
                    ),
                    PosSaleItem(
                        productId = "prod_special_mixture",
                        name = "Royal Dry Fruit Mixture",
                        quantity = 250,
                        unit = "g",
                        unitPricePaise = 48000L,
                        lineTotalPaise = 12000L
                    )
                ),
                subtotalPaise = 49000L,
                discountPaise = 0L,
                totalPaise = 49000L,
                paymentMethod = "UPI_QR",
                timestamp = System.currentTimeMillis() - 7200000L
            )
        )
    )

    override fun getRecentSales(): Flow<List<PosSale>> = _sales.asStateFlow()

    override suspend fun processSale(sale: PosSale): Result<String> {
        val saleId = if (sale.saleId.isBlank()) "pos_${System.currentTimeMillis()}" else sale.saleId
        val num =
            if (sale.saleNumber.isBlank()) "POS-${System.currentTimeMillis() % 100000}" else sale.saleNumber
        val newSale = sale.copy(saleId = saleId, saleNumber = num)

        for (item in sale.items) {
            inventoryRepository.adjustStock(
                productId = item.productId,
                quantityChange = -item.quantity,
                reason = "Counter Sale $num",
                operatorId = sale.cashierId
            )
        }

        val cur = _sales.value.toMutableList()
        cur.add(0, newSale)
        _sales.value = cur
        return Result.Success(saleId)
    }
}
