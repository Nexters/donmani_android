package com.gowoon.network.service

import com.gowoon.network.dto.common.BaseDto
import com.gowoon.network.dto.common.NullableBaseDto
import com.gowoon.network.dto.request.ReadFortuneRequest
import com.gowoon.network.dto.response.FortuneResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FortuneService {
    @PUT("fortune/read")
    suspend fun putFortuneRead(
        @Body requestBody: ReadFortuneRequest
    ): Response<NullableBaseDto<Unit?>>

    @GET("fortune/{userKey}")
    suspend fun getFortune(
        @Path("userKey") userKey: String
    ): Response<BaseDto<FortuneResponse>>

    @GET("fortune/list/{userKey}")
    suspend fun getFortuneList(
        @Path("userKey") userKey: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<BaseDto<List<FortuneResponse>>>
}
