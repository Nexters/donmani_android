package com.gowoon.model.record

sealed interface Record
data object NoConsumption : Record
data class ConsumptionRecord(
    val goodRecord: Consumption = Consumption(type = ConsumptionType.GOOD),
    val badRecord: Consumption = Consumption(type = ConsumptionType.BAD),
) : Record