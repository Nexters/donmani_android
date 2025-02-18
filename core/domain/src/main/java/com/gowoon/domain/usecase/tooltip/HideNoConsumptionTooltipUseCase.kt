package com.gowoon.domain.usecase.tooltip

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.TooltipRepository
import javax.inject.Inject

class HideNoConsumptionTooltipUseCase @Inject constructor(
    private val tooltipRepository: TooltipRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        tooltipRepository.setNoConsumptionTooltipState(false)
}