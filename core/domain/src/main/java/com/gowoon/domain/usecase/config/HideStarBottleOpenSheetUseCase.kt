package com.gowoon.domain.usecase.config

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import java.time.LocalDate
import javax.inject.Inject

class HideStarBottleOpenSheetUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        configRepository.setStarBottleOpenShownMonth(LocalDate.now().monthValue)
}