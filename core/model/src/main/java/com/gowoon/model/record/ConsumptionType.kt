package com.gowoon.model.record

import kotlinx.serialization.Serializable

@Serializable
enum class ConsumptionType(val title: String) {
    GOOD("행복"), BAD("후회")
}