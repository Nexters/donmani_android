package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseListResponse(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("records")
    val records: List<RecordDto>
)

@Serializable
data class RecordDto(
    @SerialName("date")
    val date: String,
    @SerialName("contents")
    val contents: List<ContentDto>?
)

@Serializable
data class ContentDto(
    @SerialName("flag")
    val flag: String,
    @SerialName("category")
    val category: String,
    @SerialName("memo")
    val memo: String
)
