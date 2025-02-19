package com.gowoon.data.mapper

import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.model.record.Record.NoConsumption
import com.gowoon.model.record.Record
import com.gowoon.network.dto.response.RecordDto
import java.time.LocalDate

fun RecordDto.toModel(): Record? {
    return if (contents == null) {
        NoConsumption(consumptionDate = LocalDate.parse(date))
    } else {
        contents?.let { content ->
            val goodConsumption = content.find { it.flag == ConsumptionType.GOOD.name }?.let {
                Consumption(
                    type = ConsumptionType.valueOf(it.flag),
                    category = GoodCategory.valueOf(it.category),
                    description = it.memo
                )
            }
            val badConsumption = content.find { it.flag == ConsumptionType.BAD.name }?.let {
                Consumption(
                    type = ConsumptionType.valueOf(it.flag),
                    category = BadCategory.valueOf(it.category),
                    description = it.memo
                )
            }
            ConsumptionRecord(
                consumptionDate = LocalDate.parse(date),
                goodRecord = goodConsumption,
                badRecord = badConsumption
            )
        }
    }
}