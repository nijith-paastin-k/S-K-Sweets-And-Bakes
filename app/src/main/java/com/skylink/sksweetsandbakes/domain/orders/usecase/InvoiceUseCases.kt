package com.skylink.sksweetsandbakes.domain.orders.usecase

import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.domain.model.Order
import com.skylink.sksweetsandbakes.domain.repository.InvoiceRepository
import java.io.File

class GenerateInvoicePdfUseCase(
    private val invoiceRepository: InvoiceRepository
) {
    suspend operator fun invoke(order: Order): Result<File> {
        return invoiceRepository.generateInvoicePdf(order)
    }
}
