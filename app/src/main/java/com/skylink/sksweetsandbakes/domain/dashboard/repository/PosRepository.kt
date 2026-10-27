package com.skylink.sksweetsandbakes.domain.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.PosSale
import kotlinx.coroutines.flow.Flow

interface PosRepository {
    fun getRecentSales(): Flow<List<PosSale>>
    suspend fun processSale(sale: PosSale): Result<String>
}
