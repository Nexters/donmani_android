package com.gowoon.network.service

import com.gowoon.network.dto.request.RegisterUserRequest
import com.gowoon.network.dto.response.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/users/register")
    suspend fun registerUser(
        @Body requestBody: RegisterUserRequest
    ): Response<RegisterUserResponse>
}