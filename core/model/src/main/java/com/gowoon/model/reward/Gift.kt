package com.gowoon.model.reward

data class Gift(
    val id: String,
    val category: GiftCategory,
    val name: String,
    val thumbnailImageUrl: String,
    val resourceUrl: String,
    val isNew: Boolean,
    val hidden: Boolean
)

enum class GiftCategory(val title: String) {
    BACKGROUND("배경"),
    EFFECT("효과"),
    DECORATION("장식"),
    CASE("별통이"),
    BGM("효과음")
}

enum class DecorationPosition {
    TOP_START, BOTTOM_END, ABOVE_BOTTLE
}

enum class DecorationAnimation {
    VERTICAL, HORIZONTAL, DIAGONAL, NONE
}

enum class BottleType {
    DEFAULT, CIRCLE, HEART
}

fun getDecorationPosition(decorationId: String): DecorationPosition {
    return when (decorationId) {
        "20" -> DecorationPosition.BOTTOM_END
        "23" -> DecorationPosition.ABOVE_BOTTLE
        else -> DecorationPosition.TOP_START
    }
}

fun getDecorationAnimation(decorationId: String): DecorationAnimation {
    return when (decorationId) {
        "19", "21" -> DecorationAnimation.VERTICAL
        "20" -> DecorationAnimation.HORIZONTAL
        "22" -> DecorationAnimation.DIAGONAL
        else -> DecorationAnimation.NONE
    }
}

fun getBottleType(bottleId: String): BottleType {
    return when (bottleId) {
        "24" -> BottleType.CIRCLE
        "25" -> BottleType.HEART
        else -> BottleType.DEFAULT
    }
}