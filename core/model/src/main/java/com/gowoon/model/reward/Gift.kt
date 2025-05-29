package com.gowoon.model.reward

data class Gift(
    val category: GiftCategory,
    val name: String,
    val resourceUrl: String,
    val isNew: Boolean
)

enum class GiftCategory(val title: String) {
    BACKGROUND("배경"),
    EFFECT("효과"),
    DECORATION("장식"),
    BOTTLE("별통이"),
    SOUND("효과음")
}