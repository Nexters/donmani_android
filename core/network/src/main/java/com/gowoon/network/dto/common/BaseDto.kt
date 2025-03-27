package com.gowoon.network.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseDto<T>(
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("responseMessage")
    val responseMessage: String,
    @SerialName("responseData")
    val responseData: T
)

@Serializable
data class EmptyBaseDto(
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("responseMessage")
    val responseMessage: String
)
