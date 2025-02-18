package com.gowoon.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("newUserName")
    val newUserName: String
)
