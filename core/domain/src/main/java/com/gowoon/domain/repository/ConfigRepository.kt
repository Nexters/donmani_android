package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    suspend fun getNoConsumptionTooltipState(): Flow<Result<Boolean>>
    suspend fun setNoConsumptionTooltipState(state: Boolean): Result<Unit>
    suspend fun getBBSRuleSheetState(): Flow<Result<Boolean>>
    suspend fun setBBSRuleSheetState(state: Boolean): Result<Unit>
}