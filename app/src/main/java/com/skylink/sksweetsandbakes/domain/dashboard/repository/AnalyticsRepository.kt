package com.skylink.sksweetsandbakes.domain.dashboard.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.dashboard.usecase.FinancialStatement

interface AnalyticsRepository {
    suspend fun getFinancialStatement(daysAgo: Int): Result<FinancialStatement>
}
