package com.gowoon.domain.usecase.user

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Result<String?>> = userRepository.getUserNickname()
}