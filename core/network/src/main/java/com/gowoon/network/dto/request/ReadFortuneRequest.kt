package com.gowoon.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadFortuneRequest(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("readSource")
    val readSource: String
)
