package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeStatusResponse(
    @SerialName("read")
    val read: Boolean
)
