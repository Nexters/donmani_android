package com.gowoon.data.repository

import com.gowoon.data.mapper.toModel
import com.gowoon.datastore.ConfigDataSource
import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.FortuneRepository
import com.gowoon.model.fortune.Fortune
import com.gowoon.model.fortune.FortuneReadType
import com.gowoon.network.di.DeviceId
import com.gowoon.network.dto.request.ReadFortuneRequest
import com.gowoon.network.service.FortuneService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class FortuneRepositoryImpl @Inject constructor(
    @DeviceId private val deviceId: String,
    private val configDataSource: ConfigDataSource,
    private val fortuneService: FortuneService
) : FortuneRepository {
    override suspend fun getFortuneDialogLastReadDate(): Flow<Result<String>> =
        try {
            configDataSource.getFortuneLastReadDate().map { Result.Success(it) }
        } catch (e: Exception) {
            flow { emit(Result.Error(message = e.message)) }
        }

    override suspend fun updateFortuneDialogLastReadDate(fortuneReadType: FortuneReadType): Result<Unit> =
        try {
            fortuneService.putFortuneRead(
                requestBody = ReadFortuneRequest(
                    userKey = deviceId,
                    readSource = fortuneReadType.name
                )
            ).let { result ->
                if (result.isSuccessful) {
                    Result.Success(
                        configDataSource.setFortuneLastReadDate(
                            LocalDate.now().toString()
                        )
                    )
                } else {
                    Result.Error(code = result.code(), message = result.message())
                }
            }
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }

    override suspend fun getFortune(): Flow<Result<Fortune>> = flow {
        try {
            emit(
                fortuneService.getFortune(userKey = deviceId).let { result ->
                    if (result.isSuccessful) {
                        result.body()?.let { body ->
                            Result.Success(body.responseData.toModel())
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