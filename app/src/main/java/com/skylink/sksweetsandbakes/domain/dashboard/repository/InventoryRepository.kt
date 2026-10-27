package com.skylink.sksweetsandbakes.domain.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Inventory
import com.skylink.sksweetsandbakes.domain.model.InventoryMovement
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getInventoryList(): Flow<List<Inventory>>
    suspend fun getInventory(productId: String): Result<Inventory>
    suspend fun adjustStock(
        productId: String,
        quantityChange: Int,
        reason: String,
        operatorId: String
    ): Result<Unit>

    suspend fun receiveStock(
        productId: String,
        quantity: Int,
        costPaise: Long,
        invoiceRef: String,
        operatorId: String
    ): Result<Unit>

    fun getMovements(productId: String? = null): Flow<List<InventoryMovement>>
}
