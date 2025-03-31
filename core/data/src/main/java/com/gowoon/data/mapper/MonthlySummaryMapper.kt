package com.gowoon.data.mapper

import com.gowoon.model.record.MonthlySummary
import com.gowoon.network.dto.common.MonthlySummaryDto

fun MonthlySummaryDto.toModel(): MonthlySummary {
    return MonthlySummary(
        month = this.month,
        recordCount = this.recordCount,
        totalDaysInMonth = this.totalDaysInMonth
    )
}