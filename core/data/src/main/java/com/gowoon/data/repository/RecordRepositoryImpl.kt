package com.gowoon.data.repository

import com.gowoon.data.mapper.toModel
import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RecordRepository
import com.gowoon.model.record.Record
import com.gowoon.network.di.DeviceId
import com.gowoon.network.service.ExpenseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    @DeviceId private val deviceId: String,
    private val recordService: ExpenseService
) : RecordRepository {
    override suspend fun getRecordList(
        year: Int,
        month: Int
    ): Flow<Result<List<Record?>>> = flow {
        try {
            emit(
                recordService.getExpenseList(
                    userKey = deviceId,
                    year = year,
                    month = month
                ).let { result ->
                    if (result.isSuccessful) {
                        result.body()?.let { body ->
                            Result.Success(
                                body.records.map { record ->
                                    record.toModel()
                                }
                            )
                        } ?: Result.Error(message = "empty body")
                    } else {
                        Result.Error(code = result.code(), message = result.message())
                    }
                }
            )
        } catch (e: Exception) {
            emit(Result.Error(message = e.message))
        }
    }
}