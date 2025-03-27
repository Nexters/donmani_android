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

    override suspend fun getOnBoardingState(): Result<Boolean> = try {
        Result.Success(configDataSource.getOnBoardingState())
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    override suspend fun setOnBoardingState(state: Boolean): Result<Unit> = try {
        Result.Success(configDataSource.setOnBoardingState(state))
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    override suspend fun getYesterdayTooltipLastCheckedDay(): Flow<Result<String>> =
        try {
            configDataSource.getYesterdayTooltipLastCheckedDay().map { Result.Success(it) }
        } catch (e: Exception) {
            flow { emit(Result.Error(message = e.message)) }
        }

    override suspend fun setYesterdayTooltipLastCheckedDay(date: String): Result<Unit> =
        try {
            Result.Success(configDataSource.setYesterdayTooltipLastCheckedDay(date))
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }

    override suspend fun getStarBottleListTooltipState(): Flow<Result<Boolean>> =
        try {
            configDataSource.getStarBottleListTooltipState().map { Result.Success(it) }
        } catch (e: Exception) {
            flow { emit(Result.Error(message = e.message)) }
        }

    override suspend fun setStarBottleListTooltipState(state: Boolean): Result<Unit> =
        try {
            Result.Success(configDataSource.setStarBottleListTooltipState(state))
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
}