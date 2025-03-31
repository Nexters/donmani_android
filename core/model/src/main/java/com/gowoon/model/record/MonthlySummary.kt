package com.gowoon.model.record


data class MonthlySummary(
    val month: Int,
    val recordCount: Int,
    val totalDaysInMonth: Int
)
