package com.gowoon.domain.usecase.record

import com.gowoon.domain.repository.RecordRepository
import javax.inject.Inject

class GetCategoryStatisticsUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(year: Int, month: Int) =
        recordRepository.getCategoryStatistics(year, month)
}