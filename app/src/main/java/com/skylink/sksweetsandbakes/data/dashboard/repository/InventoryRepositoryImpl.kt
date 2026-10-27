package com.skylink.sksweetsandbakes.data.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.domain.model.InventoryMovement
import com.skylink.sksweetsandbakes.domain.model.MovementType
import com.skylink.sksweetsandbakes.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InventoryRepositoryImpl : InventoryRepository {

    private val _inventory = MutableStateFlow<Map<String, Inventory>>(
        SeedData.products.associate { prod ->
            prod.productId to Inventory(
                productId = prod.productId,
                productName = prod.name,
                onHandStock = prod.onHandStock,
                reservedStock = 0,
                availableStock = prod.availableStock,
                unit = prod.baseUnit,
                lowStockThreshold = prod.lowStockThreshold
            )
        }
    )

    private val _movements = MutableStateFlow<List<InventoryMovement>>(
        listOf(
            InventoryMovement(
                movementId = "mov_001",
                productId = "prod_kaju_katli",
                productName = "Kaju Katli Special",
                type = MovementType.RECEIVE,
                quantityChange = 25000,
                previousOnHand = 0,
                newOnHand = 25000,
                referenceId = "PO-2026-101",
                operatorId = "staff_01",
                operatorName = "Anand (Inventory)",
                reason = "Fresh Morning Batch Received"
            )
        )
    )

    override fun getInventoryList(): Flow<List<Inventory>> {
        return _inventory.map { it.values.toList() }
    }

    override suspend fun getInventory(productId: String): Result<Inventory> {
        val inv = _inventory.value[productId]
        return if (inv != null) Result.Success(inv)
        else Result.Error(NoSuchElementException("Inventory for $productId not found"))
    }

    override suspend fun adjustStock(
        productId: String,
        quantityChange: Int,
        reason: String,
        operatorId: String
    ): Result<Unit> {
        val currentMap = _inventory.value.toMutableMap()
        val currentInv = currentMap[productId]
            ?: return Result.Error(NoSuchElementException("Product not found"))

        val newOnHand = currentInv.onHandStock + quantityChange
        if (newOnHand < 0) {
            return Result.Error(IllegalArgumentException("Stock cannot be negative. Current: ${currentInv.onHandStock}"))
        }

        val updatedInv = currentInv.copy(
            onHandStock = newOnHand,
            availableStock = newOnHand - currentInv.reservedStock,
            lastUpdated = System.currentTimeMillis()
        )
        currentMap[productId] = updatedInv
        _inventory.value = currentMap

        val movement = InventoryMovement(
            movementId = "mov_${System.currentTimeMillis()}",
            productId = productId,
            productName = currentInv.productName,
            type = if (quantityChange >= 0) MovementType.MANUAL_ADJUSTMENT else MovementType.WASTAGE,
            quantityChange = quantityChange,
            previousOnHand = currentInv.onHandStock,
            newOnHand = newOnHand,
            referenceId = "ADJ-${System.currentTimeMillis() % 10000}",
            operatorId = operatorId,
            reason = reason
        )
        val curMov = _movements.value.toMutableList()
        curMov.add(0, movement)
        _movements.value = curMov

        return Result.Success(Unit)
    }

    override suspend fun receiveStock(
        productId: String,
        quantity: Int,
        costPaise: Long,
        invoiceRef: String,
        operatorId: String
    ): Result<Unit> {
        return adjustStock(productId, quantity, "Purchase Received: $invoiceRef", operatorId)
    }

    override fun getMovements(productId: String?): Flow<List<InventoryMovement>> {
        return _movements.map { list ->
            if (productId == null || productId.isBlank()) list
            else list.filter { it.productId == productId }
        }
    }
}
