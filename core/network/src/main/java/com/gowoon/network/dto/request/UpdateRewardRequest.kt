package com.gowoon.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRewardRequest(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("year")
    val year: Int,
    @SerialName("month")
    val month: Int,
    @SerialName("backgroundId")
    val backgroundId: Int,
    @SerialName("effectId")
    val effectId: Int,
    @SerialName("decorationId")
    val decorationId: Int,
    @SerialName("byeoltongCaseId")
    val byeoltongCaseId: Int,
    @SerialName("bgmId")
    val bgmId: Int
)
