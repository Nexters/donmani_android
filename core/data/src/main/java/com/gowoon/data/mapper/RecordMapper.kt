package com.gowoon.data.mapper

import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.model.record.Record
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.model.record.Record.NoConsumption
import com.gowoon.model.record.name
import com.gowoon.network.dto.common.ContentDto
import com.gowoon.network.dto.common.RecordDto
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

fun Record.toDto(): RecordDto? {
    val contents: List<ContentDto>? = when (this) {
        is NoConsumption -> null
        is ConsumptionRecord -> {
            mutableListOf<ContentDto>().apply {
                goodRecord?.let { good ->
                    this.add(
                        ContentDto(
                            flag = good.type.name,
                            category = good.category.name(good.type),
                            memo = good.description
                        )
                    )
                }
                badRecord?.let { bad ->
                    this.add(
                        ContentDto(
                            flag = bad.type.name,
                            category = bad.category.name(bad.type),
                            memo = bad.description
                        )
                    )
                }
            }
        }
    }
    return date?.let {
        RecordDto(
            date = it.toString(),
            contents = contents
        )
    }
}