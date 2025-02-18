package com.gowoon.model.record

sealed interface Record
data object NoConsumption : Record
data class ConsumptionRecord(
    val goodRecord: Consumption? = null,
    val badRecord: Consumption? = null,
) : Record