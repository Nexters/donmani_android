package com.gowoon.domain.usecase.user

import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.UserRepository
import com.gowoon.domain.util.NicknameUtil
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return when (val registerUserResult = userRepository.registerUser()) {
            is Result.Success -> {
                when (val registerNicknameResult =
                    userRepository.registerUserNickname(registerUserResult.data + NicknameUtil.DEFAULT_SUFFIX)) {
                    is Result.Success -> {
                        userRepository.setRegisterUserState(true)
                    }

                    is Result.Error -> registerNicknameResult
                }
            }

            is Result.Error -> registerUserResult
        }
    }
}