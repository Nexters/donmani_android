package com.gowoon.domain.usecase.record

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.model.common.BBSState
import com.gowoon.model.record.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecordListUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {
    suspend operator fun invoke(year: Int, month: Int): Flow<Result<BBSState>> {
        return recordRepository.getRecordList(year, month)
    }
}