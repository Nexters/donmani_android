package com.gowoon.network.service

import com.gowoon.network.dto.common.BaseDto
import com.gowoon.network.dto.common.EmptyBaseDto
import com.gowoon.network.dto.request.PostRecordRequest
import com.gowoon.network.dto.response.ExpenseListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ExpenseService {
    @GET("expenses/list/{userKey}")
    suspend fun getExpenseList(
        @Path("userKey") userKey: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): Response<BaseDto<ExpenseListResponse>>

    @POST("expenses")
    suspend fun postExpense(
        @Body requestBody: PostRecordRequest
    ): Response<EmptyBaseDto>
}