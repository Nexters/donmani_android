package com.gowoon.domain.usecase.user

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.UserRepository
import javax.inject.Inject

class GetNoticeStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Boolean> = userRepository.getNoticeStatus()
}