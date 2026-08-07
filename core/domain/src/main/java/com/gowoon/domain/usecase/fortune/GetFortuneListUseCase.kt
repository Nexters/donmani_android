package com.gowoon.domain.usecase.fortune

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.FortuneRepository
import com.gowoon.model.fortune.Fortune
import javax.inject.Inject

class GetFortuneListUseCase @Inject constructor(
    private val fortuneRepository: FortuneRepository
) {
    suspend operator fun invoke(
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<Fortune>> = fortuneRepository.getFortuneList(startDate, endDate)
}
