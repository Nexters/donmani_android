package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackResponse(
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("name")
    val name: String,
    @SerialName("category")
    val category: String,
    @SerialName("flagType")
    val flagType: Boolean
)
