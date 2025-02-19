package com.gowoon.network.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDto(
    @SerialName("flag")
    val flag: String,
    @SerialName("category")
    val category: String,
    @SerialName("memo")
    val memo: String
)

