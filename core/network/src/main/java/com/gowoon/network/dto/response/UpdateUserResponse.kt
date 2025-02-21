package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserResponse(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("updatedUserName")
    val updatedUserName: String
)
