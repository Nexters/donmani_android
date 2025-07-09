package com.gowoon.network.dto.response

import com.gowoon.network.dto.common.RecordDto
import com.gowoon.network.dto.common.RewardDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseListResponse(
    @SerialName("userKey")
    val userKey: String,
    @SerialName("records")
    val records: List<RecordDto>?,
    @SerialName("saveItems")
    val saveItems: List<RewardDto>,
    @SerialName("hasNotOpenedRewards")
    val hasNotOpenedRewards: Boolean
)