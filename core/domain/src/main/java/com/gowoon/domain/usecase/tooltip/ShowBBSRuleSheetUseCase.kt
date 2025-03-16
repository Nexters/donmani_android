package com.gowoon.domain.usecase.tooltip

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowBBSRuleSheetUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> = configRepository.getBBSRuleSheetState()
}