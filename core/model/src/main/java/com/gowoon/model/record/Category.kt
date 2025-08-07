package com.gowoon.model.record

import kotlinx.serialization.Serializable

@Serializable
sealed interface Category

@Serializable
enum class GoodCategory(val title: String, val deleted: Boolean = false) : Category {
    ENERGY("활력"),
    GROWTH("성장"),
    HEALING("힐링"),
    HAPPINESS("소확행"),
    FLEX("플렉스"),
    DIGNITY("품위유지"),
    AFFECTION("마음전달"),
    HEALTH("건강"),
    SAVING("절약"),
    NONE("없음", true)
}

@Serializable
enum class BadCategory(val title: String, val deleted: Boolean = false) : Category {
    GREED("욕심"),
    ADDICTION("중독"),
    LAZINESS("게으름"),
    IMPULSE("충동"),
    MEANINGLESSNESS("무의미"),
    BOASTFULNESS("과시"),
    HABIT("습관반복"),
    OVERFRUGALITY("과한절약"),
    MISS("선택미스"),
    NONE("없음", true)
}

fun Category.getTitle(type: ConsumptionType): String = when (type) {
    ConsumptionType.GOOD -> (this as GoodCategory).title
    ConsumptionType.BAD -> (this as BadCategory).title
}

fun Category.name(type: ConsumptionType): String = when (type) {
    ConsumptionType.GOOD -> (this as GoodCategory).name
    ConsumptionType.BAD -> (this as BadCategory).name
}

fun Category.isDeleted(): Boolean = kotlin.runCatching {
    (this as GoodCategory).deleted
}.getOrElse {
    (this as BadCategory).deleted
}

fun Category.getTitle(): String = kotlin.runCatching {
    (this as GoodCategory).title
}.getOrElse {
    (this as BadCategory).title
}

fun String.getCategory(): Category = kotlin.runCatching {
    GoodCategory.valueOf(this)
}.getOrElse {
    BadCategory.valueOf(this)
}