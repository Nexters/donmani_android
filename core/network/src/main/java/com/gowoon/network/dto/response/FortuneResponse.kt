package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FortuneResponse(
    @SerialName("targetDate")
    val targetDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("content")
    val content: String,
    @SerialName("item")
    val item: String
)
