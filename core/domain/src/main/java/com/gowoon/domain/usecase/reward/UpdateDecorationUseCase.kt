package com.gowoon.domain.usecase.reward

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.RewardRepository
import java.time.LocalDate
import javax.inject.Inject

class UpdateDecorationUseCase @Inject constructor(
    private val rewardRepository: RewardRepository,
) {
    suspend operator fun invoke(
        backgroundId: String,
        effectId: String,
        decorationId: String,
        caseId: String,
//        bgmId: String
    ): Result<Unit> {
        return rewardRepository.updateReward(
            year = LocalDate.now().year,
            month = LocalDate.now().monthValue,
            backgroundId = backgroundId.toInt(),
            effectId = effectId.toInt(),
            decorationId = decorationId.toInt(),
            caseId = caseId.toInt(),
//            bgmId = bgmId.toInt()
        )
    }
}