package com.gowoon.network.dto.response

import com.gowoon.network.dto.common.RewardDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InventoryResponse(
    @SerialName("DECORATION")
    val decoration: List<RewardDto>,
    @SerialName("EFFECT")
    val effect: List<RewardDto>,
    @SerialName("CASE")
    val case: List<RewardDto>,
    @SerialName("BGM")
    val bgm: List<RewardDto>,
    @SerialName("BACKGROUND")
    val background: List<RewardDto>
)
