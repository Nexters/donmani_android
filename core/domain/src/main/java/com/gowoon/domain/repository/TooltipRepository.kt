package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface TooltipRepository {
    suspend fun getNoConsumptionTooltipState(): Flow<Result<Boolean>>
    suspend fun setNoConsumptionTooltipState(state: Boolean): Result<Unit>
}