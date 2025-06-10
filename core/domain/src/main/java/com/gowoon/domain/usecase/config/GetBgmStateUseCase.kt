package com.gowoon.domain.usecase.config

import com.gowoon.domain.repository.ConfigRepository
import javax.inject.Inject

class GetBgmStateUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke() = configRepository.getBgmPlayState()
}