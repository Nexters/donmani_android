package com.gowoon.domain.usecase.record

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.model.record.Record
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetRecordListUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Record?>>> {
        val today = LocalDate.now()
        return recordRepository.getRecordList(today.year, today.monthValue)
    }
}