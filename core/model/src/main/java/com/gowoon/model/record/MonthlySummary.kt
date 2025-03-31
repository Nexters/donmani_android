package com.gowoon.model.record

data class MonthlySummary(
    val month: Int,
    val recordCount: Int,
    val totalDaysInMonth: Int
)

sealed class BottleState {
    data class OPENED(val count: Int, val total: Int) : BottleState()
    data object LOCKED : BottleState()
}