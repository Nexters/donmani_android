package com.gowoon.model.record

import java.time.LocalDate

sealed class Record(val date: LocalDate?) {
    data class NoConsumption(val consumptionDate: LocalDate? = null) : Record(consumptionDate)
    data class ConsumptionRecord(
        val consumptionDate: LocalDate? = null,
        val goodRecord: Consumption? = null,
        val badRecord: Consumption? = null,
    ) : Record(consumptionDate)
}