package com.gowoon.network.service

import com.gowoon.network.dto.common.BaseDto
import com.gowoon.network.dto.request.RegisterUserRequest
import com.gowoon.network.dto.request.UpdateUserRequest
import com.gowoon.network.dto.response.NoticeStatusResponse
import com.gowoon.network.dto.response.RegisterUserResponse
import com.gowoon.network.dto.response.UpdateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @POST("user/register")
    suspend fun registerUser(
        @Body requestBody: RegisterUserRequest
    ): Response<BaseDto<RegisterUserResponse>>

    @PUT("user/update")
    suspend fun updateUser(
        @Body requestBody: UpdateUserRequest
    ): Response<BaseDto<UpdateUserResponse>>

    @GET("notice/status/{userKey}")
    suspend fun getNoticeStatus(
        @Path("userKey") userKey: String
    ): Response<NoticeStatusResponse>

    @PUT("notice/status/{userKey}")
    suspend fun updateNoticeStatus(
        @Path("userKey") userKey: String
    ): Response<Unit>
}