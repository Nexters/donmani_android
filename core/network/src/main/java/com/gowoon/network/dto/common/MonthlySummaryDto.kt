package com.gowoon.network.dto.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MonthlySummaryDto(
    @SerialName("month")
    val month: Int,
    @SerialName("recordCount")
    val recordCount: Int,
    @SerialName("totalDaysInMonth")
    val totalDaysInMonth: Int
)

@Serializer(forClass = MonthlySummaryDto::class)
object MonthlySummarySerializer : KSerializer<List<MonthlySummaryDto>> {
    override val descriptor: SerialDescriptor =
        ListSerializer(MonthlySummaryDto.serializer()).descriptor

    override fun serialize(encoder: Encoder, value: List<MonthlySummaryDto>) {
        val jsonObject = JsonObject(value.associate {
            it.month.toString() to JsonObject(
                mapOf(
                    "recordCount" to JsonPrimitive(it.recordCount),
                    "totalDaysInMonth" to JsonPrimitive(it.totalDaysInMonth)
                )
            )
        })
        encoder.encodeSerializableValue(JsonObject.serializer(), jsonObject)
    }

    override fun deserialize(decoder: Decoder): List<MonthlySummaryDto> {
        val jsonObject = decoder.decodeSerializableValue(JsonObject.serializer())
        return jsonObject.map { (key, value) ->
            val recordCount = value.jsonObject["recordCount"]?.jsonPrimitive?.int ?: 0
            val totalDaysInMonth = value.jsonObject["totalDaysInMonth"]?.jsonPrimitive?.int ?: 0
            MonthlySummaryDto(
                month = key.toInt(),
                recordCount = recordCount,
                totalDaysInMonth = totalDaysInMonth
            )
        }
    }
}
