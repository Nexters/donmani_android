package com.gowoon.data.repository

import com.gowoon.datastore.ConfigDataSource
import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val configDataSource: ConfigDataSource
) : ConfigRepository {
    override suspend fun getNoConsumptionTooltipState(): Flow<Result<Boolean>> =
        try {
            configDataSource.getNoConsumptionTooltipState().map {
                Result.Success(it)
            }
        } catch (e: Exception) {
            flow { emit(Result.Error(message = e.message)) }
        }

    override suspend fun setNoConsumptionTooltipState(state: Boolean): Result<Unit> =
        try {
            configDataSource.setNoConsumptionTooltipState(state).let { Result.Success(Unit) }
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }

    override suspend fun getBBSRuleSheetState(): Flow<Result<Boolean>> =
        try {
            configDataSource.getBBSRuleSheetState().map {
                Result.Success(it)
            }
        } catch (e: Exception) {
            flow { emit(Result.Error(message = e.message)) }
        }


    override suspend fun setBBSRuleSheetState(state: Boolean): Result<Unit> =
        try {
            configDataSource.setBBSRuleSheetState(state).let { Result.Success(Unit) }
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
}