package com.gowoon.model.record

import kotlinx.serialization.Serializable

@Serializable
data class Consumption(
    val type: ConsumptionType,
    val category: Category,
    val description: String = ""
)
