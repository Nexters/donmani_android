package com.gowoon.domain.usecase.reward

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RewardRepository
import javax.inject.Inject

class HideRewardReceivedTooltipUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        rewardRepository.setShowRewardReceivedTooltip(false)
}