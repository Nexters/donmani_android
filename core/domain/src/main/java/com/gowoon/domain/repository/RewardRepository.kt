package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import com.gowoon.model.reward.Feedback
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    suspend fun getShowRewardReceivedTooltip(): Flow<Result<Boolean>>
    suspend fun setShowRewardReceivedTooltip(state: Boolean): Result<Unit>
    suspend fun getFeedbackSummary(): Flow<Result<Pair<Boolean, Int>>> // isNotOpened, totalCount
    suspend fun getShowFirstAccessRewardBottomSheet(): Flow<Result<Boolean>>
    suspend fun setShowFirstAccessRewardBottomSheet(state: Boolean): Result<Unit>
    suspend fun getFeedback(): Flow<Result<Feedback>>
    suspend fun getGiftCount(): Flow<Result<Int>>
    suspend fun openGift(): Flow<Result<List<Gift>>>
    suspend fun getInventoryList(): Flow<Result<Map<GiftCategory, List<Gift>>>>
    suspend fun updateReward(
        year: Int,
        month: Int,
        backgroundId: Int,
        effectId: Int,
        decorationId: Int,
        caseId: Int,
        bgmId: Int
    ): Result<Unit>

    suspend fun getShowFirstAccessDecorationBottomSheet(): Flow<Result<Boolean>>
    suspend fun setShowFirstAccessDecorationBottomSheet(state: Boolean): Result<Unit>
    suspend fun hasBgmItems(): Result<Boolean>
    suspend fun setBgmItemsOwned(): Result<Unit>
}