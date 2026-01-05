package com.gowoon.domain.usecase.record

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.model.record.MonthlySummary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthlySummaryUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(year: Int): Flow<Result<List<MonthlySummary>>> {
        return recordRepository.getMonthlySummaryList(year)
    }
}