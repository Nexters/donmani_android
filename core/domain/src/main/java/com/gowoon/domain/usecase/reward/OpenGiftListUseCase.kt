package com.gowoon.domain.usecase.reward

import com.gowoon.domain.repository.RewardRepository
import javax.inject.Inject

class OpenGiftListUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend operator fun invoke() = rewardRepository.openGift()
}