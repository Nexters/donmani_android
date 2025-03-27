package com.gowoon.domain.usecase.config

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ShowYesterdayTooltipUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> =
        configRepository.getYesterdayTooltipLastCheckedDay().map {
            when (it) {
                is Result.Success -> {
                    val today = LocalDate.now().toString()
                    Result.Success(it.data != today)
                }

                is Result.Error -> it
            }
        }
}