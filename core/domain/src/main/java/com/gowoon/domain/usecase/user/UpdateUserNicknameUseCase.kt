package com.gowoon.domain.usecase.user

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickname: String): Result<Unit> {
        return when (val updateUserResult = userRepository.updateUserNickname(nickname)) {
            is Result.Success -> {
                userRepository.registerUserNickname(updateUserResult.data)
            }

            is Result.Error -> updateUserResult
        }
    }
}