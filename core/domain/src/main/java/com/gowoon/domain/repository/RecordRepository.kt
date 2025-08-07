package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import com.gowoon.model.common.BBSState
import com.gowoon.model.record.Category
import com.gowoon.model.record.MonthlySummary
import com.gowoon.model.record.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun getRecordList(year: Int, month: Int): Flow<Result<BBSState>>
    suspend fun saveRecord(record: Record): Result<Unit>
    suspend fun getMonthlySummaryList(year: Int): Flow<Result<List<MonthlySummary>>>
    suspend fun getCategoryStatistics(
        year: Int,
        month: Int
    ): Flow<Result<Map<Category, Int>>>
}