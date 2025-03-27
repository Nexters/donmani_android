package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    suspend fun getNoConsumptionTooltipState(): Flow<Result<Boolean>>
    suspend fun setNoConsumptionTooltipState(state: Boolean): Result<Unit>
    suspend fun getOnBoardingState(): Result<Boolean>
    suspend fun setOnBoardingState(state: Boolean): Result<Unit>
    suspend fun getYesterdayTooltipLastCheckedDay(): Flow<Result<String>>
    suspend fun setYesterdayTooltipLastCheckedDay(date: String): Result<Unit>
}