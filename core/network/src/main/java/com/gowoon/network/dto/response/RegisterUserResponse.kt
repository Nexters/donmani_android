package com.gowoon.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserResponse(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("userName")
    val userName: String
)
