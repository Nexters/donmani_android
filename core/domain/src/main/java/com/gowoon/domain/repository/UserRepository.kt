package com.gowoon.domain.repository

import com.gowoon.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getRegisterUserState(): Result<Boolean>
    suspend fun setRegisterUserState(state: Boolean): Result<Unit>
    suspend fun registerUser(): Result<String>
    suspend fun registerUserNickname(nickname: String): Result<Unit>
    suspend fun getUserNickname(): Flow<Result<String?>>
}