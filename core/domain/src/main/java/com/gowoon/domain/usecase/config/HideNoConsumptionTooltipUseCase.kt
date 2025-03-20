package com.gowoon.domain.usecase.config

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import javax.inject.Inject

class HideNoConsumptionTooltipUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        configRepository.setNoConsumptionTooltipState(false)
}