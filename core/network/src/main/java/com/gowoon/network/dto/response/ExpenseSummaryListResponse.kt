package com.gowoon.network.dto.response

import com.gowoon.network.dto.common.MonthlySummaryDto
import com.gowoon.network.dto.common.MonthlySummarySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseSummaryListResponse(
    @SerialName("year")
    val year: Int,
    @Serializable(with = MonthlySummarySerializer::class)
    @SerialName("monthlyRecords")
    val monthlyRecords: List<MonthlySummaryDto>
)