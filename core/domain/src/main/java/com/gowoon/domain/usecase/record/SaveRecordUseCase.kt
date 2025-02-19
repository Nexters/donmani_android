package com.gowoon.domain.usecase.record

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.model.record.Record
import javax.inject.Inject

class SaveRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(record: Record): Result<Unit> = recordRepository.saveRecord(record)
}