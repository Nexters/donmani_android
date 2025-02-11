package com.gowoon.model.record

data class Consumption(
    val type: ConsumptionType,
    val category: Category? = null,
    val description: String = ""
)
