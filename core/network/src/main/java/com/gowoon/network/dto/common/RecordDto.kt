package com.gowoon.network.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordDto(
    @SerialName("date")
    val date: String,
    @SerialName("contents")
    val contents: List<ContentDto>?
)