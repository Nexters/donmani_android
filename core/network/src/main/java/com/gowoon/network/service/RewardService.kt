package com.gowoon.network.service

import com.gowoon.network.dto.common.BaseDto
import com.gowoon.network.dto.response.FeedbackSummaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RewardService {
    @GET("feedback/{userKey}")
    suspend fun getFeedbackSummary(
        @Path("userKey") userKey: String
    ): Response<BaseDto<FeedbackSummaryResponse>>
}