package com.gowoon.domain.usecase.fortune

import com.gowoon.domain.repository.FortuneRepository
import com.gowoon.model.fortune.FortuneReadType
import javax.inject.Inject

class ReadFortuneUseCase @Inject constructor(
    private val fortuneRepository: FortuneRepository
) {
    suspend operator fun invoke(isFromNotification: Boolean) {
        fortuneRepository.updateFortuneDialogLastReadDate(if (isFromNotification) FortuneReadType.NOTIFICATION else FortuneReadType.APP_DIRECT)
    }
}