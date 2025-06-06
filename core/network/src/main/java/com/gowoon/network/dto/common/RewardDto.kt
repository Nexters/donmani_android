package com.gowoon.network.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RewardDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("jsonUrl")
    val jsonUrl: String?,
    @SerialName("mp3Url")
    val mp3Url: String?,
    @SerialName("category")
    val category: String,
    @SerialName("owned")
    val owned: Boolean,
    @SerialName("newAcquiredFlag")
    val newAcquiredFlag: Boolean
)
