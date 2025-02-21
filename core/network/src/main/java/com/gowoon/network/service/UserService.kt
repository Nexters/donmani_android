package com.gowoon.network.service

import com.gowoon.network.dto.request.RegisterUserRequest
import com.gowoon.network.dto.request.UpdateUserRequest
import com.gowoon.network.dto.response.RegisterUserResponse
import com.gowoon.network.dto.response.UpdateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @POST("/users/register")
    suspend fun registerUser(
        @Body requestBody: RegisterUserRequest
    ): Response<RegisterUserResponse>

    @PUT("users/update")
    suspend fun updateUser(
        @Body requestBody: UpdateUserRequest
    ): Response<UpdateUserResponse>

}