package com.gowoon.network.dto.request

import com.gowoon.network.dto.common.RecordDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostRecordRequest(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("records")
    val records: List<RecordDto>
)
