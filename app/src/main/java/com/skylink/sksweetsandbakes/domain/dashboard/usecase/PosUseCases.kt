package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.PosSale
import com.skylink.sksweetsandbakes.domain.repository.PosRepository
import kotlinx.coroutines.flow.Flow

class ProcessPosSaleUseCase(
    private val posRepository: PosRepository
) {
    suspend operator fun invoke(sale: PosSale): Result<String> = posRepository.processSale(sale)
}

class GetRecentPosSalesUseCase(
    private val posRepository: PosRepository
) {
    operator fun invoke(): Flow<List<PosSale>> = posRepository.getRecentSales()
}
