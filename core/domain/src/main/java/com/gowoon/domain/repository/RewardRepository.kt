package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    suspend fun getShowRewardReceivedTooltip(): Flow<Result<Boolean>>
    suspend fun setShowRewardReceivedTooltip(state: Boolean): Result<Unit>
    suspend fun getFeedbackSummary(): Flow<Result<Triple<Boolean, Boolean, Int>>> // isOpened, isFirstOpen, totalCount
}