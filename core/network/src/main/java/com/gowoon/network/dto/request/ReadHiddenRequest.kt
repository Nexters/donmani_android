package com.gowoon.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadHiddenRequest(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("year")
    val year: Int,
    @SerialName("month")
    val month: Int
)
