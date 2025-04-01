package com.gowoon.network.dto.response

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
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class CategoryStatisticsResponse(
    @SerialName("year")
    val year: Int,
    @SerialName("month")
    val month: Int,
    @Serializable(with = CategoryCountSerializer::class)
    @SerialName("categoryCounts")
    val categoryCounts: List<CategoryCount>
)

@Serializable
data class CategoryCount(
    @SerialName("category")
    val category: String,
    @SerialName("count")
    val count: Int
)

@Serializer(forClass = CategoryCount::class)
object CategoryCountSerializer : KSerializer<List<CategoryCount>> {
    override val descriptor: SerialDescriptor =
        ListSerializer(CategoryCount.serializer()).descriptor

    override fun serialize(encoder: Encoder, value: List<CategoryCount>) {
        val jsonObject = JsonObject(value.associate {
            "categoryCounts" to JsonObject(
                mapOf(
                    it.category to JsonPrimitive(it.count),
                )
            )
        })
        encoder.encodeSerializableValue(JsonObject.serializer(), jsonObject)
    }

    override fun deserialize(decoder: Decoder): List<CategoryCount> {
        val jsonObject = decoder.decodeSerializableValue(JsonObject.serializer())
        return jsonObject.map { (key, value) ->
            CategoryCount(
                category = key,
                count = value.jsonPrimitive.int
            )
        }
    }
}


