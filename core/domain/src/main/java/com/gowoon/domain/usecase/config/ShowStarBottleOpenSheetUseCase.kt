package com.gowoon.domain.usecase.config

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ShowStarBottleOpenSheetUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    suspend operator fun invoke(): Flow<Result<Boolean>> {
        return configRepository.getStarBottleOpenShownMonth().map {
            val currentMonth = LocalDate.now().monthValue
            when (it) {
                is Result.Success -> {
                    Result.Success(it.data != currentMonth)
                }

                is Result.Error -> it
            }
        }
    }
}