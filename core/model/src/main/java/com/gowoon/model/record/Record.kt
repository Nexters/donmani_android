package com.gowoon.model.record

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
sealed class Record(@Serializable(with = LocalDateSerializer::class) val date: LocalDate?) {
    @Serializable
    data class NoConsumption(@Serializable(with = LocalDateSerializer::class) val consumptionDate: LocalDate? = null) :
        Record(consumptionDate)

    @Serializable
    data class ConsumptionRecord(
        @Serializable(with = LocalDateSerializer::class)
        val consumptionDate: LocalDate? = null,
        val goodRecord: Consumption? = null,
        val badRecord: Consumption? = null,
    ) : Record(consumptionDate)
}

object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"

    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}