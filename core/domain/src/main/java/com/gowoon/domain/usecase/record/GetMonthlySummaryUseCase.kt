package com.gowoon.domain.usecase.record

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.model.record.MonthlySummary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetMonthlySummaryUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(): Flow<Result<List<MonthlySummary>>> {
        val year = LocalDate.now().year
        return recordRepository.getMonthlySummaryList(year)
    }
}