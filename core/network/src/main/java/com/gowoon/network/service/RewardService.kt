package com.gowoon.network.service

import com.gowoon.network.dto.common.BaseDto
import com.gowoon.network.dto.common.EmptyBaseDto
import com.gowoon.network.dto.common.RewardDto
import com.gowoon.network.dto.request.ReadHiddenRequest
import com.gowoon.network.dto.request.UpdateRewardRequest
import com.gowoon.network.dto.response.FeedbackResponse
import com.gowoon.network.dto.response.FeedbackSummaryResponse
import com.gowoon.network.dto.response.InventoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface RewardService {
    @GET("feedback/{userKey}")
    suspend fun getFeedbackSummary(
        @Path("userKey") userKey: String
    ): Response<BaseDto<FeedbackSummaryResponse>>

    @GET("feedback/content/{userKey}")
    suspend fun getFeedback(
        @Path("userKey") userKey: String
    ): Response<BaseDto<FeedbackResponse>>

    @GET("reward/not-open/{userKey}")
    suspend fun getGiftCount(
        @Path("userKey") userKey: String
    ): Response<BaseDto<Int>>

    @PUT("reward/open/{userKey}")
    suspend fun openRewardList(
        @Path("userKey") userKey: String
    ): Response<BaseDto<List<RewardDto>>>

    @GET("reward/edit/{userKey}")
    suspend fun getInventoryList(
        @Path("userKey") userKey: String
    ): Response<BaseDto<InventoryResponse>>

    @PUT("reward")
    suspend fun updateReward(
        @Body requestBody: UpdateRewardRequest
    ): Response<EmptyBaseDto>

    @PUT("reward/hidden-read")
    suspend fun readHiddenItem(
        @Body requestBody: ReadHiddenRequest
    ): Response<EmptyBaseDto>
}