package com.gowoon.data.repository

import com.gowoon.datastore.TooltipDataSource
import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.TooltipRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TooltipRepositoryImpl @Inject constructor(
    private val tooltipDataSource: TooltipDataSource
) : TooltipRepository {
    override suspend fun getNoConsumptionTooltipState(): Flow<Result<Boolean>> =
        try {
            tooltipDataSource.getNoConsumptionTooltipState().map {
                Result.Success(it)
            }
        } catch (e: Exception) {
            flow { Result.Error(message = e.message) }
        }

    override suspend fun setNoConsumptionTooltipState(state: Boolean): Result<Unit> =
        try {
            tooltipDataSource.setNoConsumptionTooltipState(state).let { Result.Success(Unit) }
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
}