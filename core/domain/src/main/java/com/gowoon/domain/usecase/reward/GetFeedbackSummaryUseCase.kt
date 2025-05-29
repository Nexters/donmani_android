package com.gowoon.domain.usecase.reward

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RewardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeedbackSummaryUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend operator fun invoke(): Flow<Result<Triple<Boolean, Boolean, Int>>> =
        rewardRepository.getFeedbackSummary()
}