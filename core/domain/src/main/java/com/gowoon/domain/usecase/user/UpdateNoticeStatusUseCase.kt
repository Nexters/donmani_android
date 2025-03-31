package com.gowoon.domain.usecase.user

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.UserRepository
import javax.inject.Inject

class UpdateNoticeStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> = userRepository.updateNoticeStatus()
}