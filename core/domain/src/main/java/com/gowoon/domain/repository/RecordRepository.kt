package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import com.gowoon.model.record.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun getRecordList(year: Int, month: Int): Flow<Result<List<Record?>>>
}