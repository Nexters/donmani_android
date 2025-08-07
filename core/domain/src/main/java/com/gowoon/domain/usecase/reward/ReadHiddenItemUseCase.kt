package com.gowoon.domain.usecase.reward

import com.gowoon.domain.repository.RewardRepository
import java.time.LocalDate
import javax.inject.Inject

class ReadHiddenItemUseCase @Inject constructor(
    val rewardRepository: RewardRepository
) {
    suspend operator fun invoke() = rewardRepository.readHiddenItem(
        year = LocalDate.now().year,
        month = LocalDate.now().monthValue
    )
}