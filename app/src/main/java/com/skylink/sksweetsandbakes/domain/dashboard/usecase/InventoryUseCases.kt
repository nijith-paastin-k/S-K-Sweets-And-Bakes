package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.domain.model.InventoryMovement
import com.skylink.sksweetsandbakes.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class GetInventoryListUseCase(
    private val inventoryRepository: InventoryRepository
) {
    operator fun invoke(): Flow<List<Inventory>> = inventoryRepository.getInventoryList()
    suspend fun getInventory(productId: String): Result<Inventory> =
        inventoryRepository.getInventory(productId)

    fun getMovements(productId: String? = null): Flow<List<InventoryMovement>> =
        inventoryRepository.getMovements(productId)
}

class AdjustStockUseCase(
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(
        productId: String,
        quantityChange: Int,
        reason: String,
        operatorId: String
    ): Result<Unit> = inventoryRepository.adjustStock(productId, quantityChange, reason, operatorId)
}
