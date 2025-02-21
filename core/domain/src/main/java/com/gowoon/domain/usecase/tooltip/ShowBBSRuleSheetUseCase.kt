package com.gowoon.domain.usecase.tooltip

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.TooltipRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowBBSRuleSheetUseCase @Inject constructor(
    private val tooltipRepository: TooltipRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> = tooltipRepository.getBBSRuleSheetState()
}