package com.gowoon.domain.usecase.fortune

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.FortuneRepository
import com.gowoon.model.fortune.Fortune
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

class ShowFortuneUseCase @Inject constructor(
    private val fortuneRepository: FortuneRepository
) {
    suspend operator fun invoke(): Flow<Result<Fortune?>> =
        fortuneRepository.getFortuneDialogLastReadDate().flatMapLatest {
            when (it) {
                is Result.Success -> {
                    if (it.data == LocalDate.now().toString()) {
                        flowOf(Result.Success(null))
                    } else {
                        fortuneRepository.getFortune()
                    }
                }

                is Result.Error -> flowOf(it)
            }
        }
}