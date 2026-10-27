package com.skylink.sksweetsandbakes.domain.orders.repository

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Order
import java.io.File

interface InvoiceRepository {
    suspend fun generateInvoicePdf(order: Order): Result<File>
}
