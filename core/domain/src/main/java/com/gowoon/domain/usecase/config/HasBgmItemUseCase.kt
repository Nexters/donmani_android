package com.gowoon.domain.usecase.config

import com.gowoon.domain.repository.RewardRepository
import javax.inject.Inject

class HasBgmItemUseCase @Inject constructor(
    private val rewardRepository: RewardRepository
) {
    suspend operator fun invoke() = rewardRepository.hasBgmItems()
}