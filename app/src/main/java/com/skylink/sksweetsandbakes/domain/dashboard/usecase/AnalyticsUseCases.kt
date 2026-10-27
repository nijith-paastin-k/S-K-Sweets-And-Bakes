package com.skylink.sksweetsandbakes.domain.dashboard.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.repository.AnalyticsRepository

class GetFinancialStatementUseCase(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(daysAgo: Int): Result<FinancialStatement> =
        analyticsRepository.getFinancialStatement(daysAgo)
}
