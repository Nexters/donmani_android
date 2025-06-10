package com.gowoon.domain.usecase.user

import com.gowoon.domain.repository.UserRepository
import javax.inject.Inject

class GetRewardStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getRewardStatus()
}