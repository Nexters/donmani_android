package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackSummaryResponse(
    @SerialName("isNotOpened")
    val isNotOpened: Boolean,
    @SerialName("isFirstOpen")
    val isFirstOpen: Boolean,
    @SerialName("totalCount")
    val totalCount: Int
)
