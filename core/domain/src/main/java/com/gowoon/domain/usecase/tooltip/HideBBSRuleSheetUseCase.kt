package com.gowoon.domain.usecase.tooltip

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import javax.inject.Inject

class HideBBSRuleSheetUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke(): Result<Unit> = configRepository.setBBSRuleSheetState(false)
}