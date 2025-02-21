package com.gowoon.data.repository

import com.gowoon.datastore.UserDataSource
import com.gowoon.domain.common.Result
import com.gowoon.domain.repository.UserRepository
import com.gowoon.network.di.DeviceId
import com.gowoon.network.dto.request.RegisterUserRequest
import com.gowoon.network.dto.request.UpdateUserRequest
import com.gowoon.network.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @DeviceId private val deviceId: String,
    private val userDataSource: UserDataSource,
    private val userService: UserService
) : UserRepository {
    override suspend fun getRegisterUserState(): Result<Boolean> =
        try {
            Result.Success(userDataSource.getRegisterUserState().first())
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }

    override suspend fun setRegisterUserState(state: Boolean): Result<Unit> = try {
        Result.Success(userDataSource.setRegisterUserState(state))
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    // TODO response error handling 일괄 처리
    override suspend fun registerUser(): Result<String> = try {
        userService.registerUser(RegisterUserRequest(deviceId)).let { result ->
            if (result.isSuccessful) {
                result.body()?.let { body ->
                    Result.Success(body.userName)
                } ?: Result.Error(message = "empty body")
            } else {
                Result.Error(code = result.code(), message = result.message())
            }
        }
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    override suspend fun registerUserNickname(nickname: String): Result<Unit> = try {
        Result.Success(userDataSource.setUserNickname(nickname))
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }

    override suspend fun getUserNickname(): Flow<Result<String?>> = try {
        userDataSource.getUserNickname().map { Result.Success(it) }
    } catch (e: Exception) {
        flow { emit(Result.Error(message = e.message)) }
    }

    override suspend fun updateUserNickname(newNickname: String): Result<String> = try {
        userService.updateUser(
            UpdateUserRequest(
                userKey = deviceId,
                newUserName = newNickname
            )
        ).let { result ->
            if (result.isSuccessful) {
                result.body()?.let { body ->
                    Result.Success(body.updatedUserName)
                } ?: Result.Error(message = "body is null")
            } else {
                Result.Error(code = result.code(), message = result.message())
            }
        }
    } catch (e: Exception) {
        Result.Error(message = e.message)
    }
}