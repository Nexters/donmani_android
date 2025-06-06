package com.gowoon.model.reward

data class Gift(
    val id: String,
    val category: GiftCategory,
    val name: String,
    val thumbnailImageUrl: String,
    val resourceUrl: String,
    val isNew: Boolean
)

enum class GiftCategory(val title: String) {
    BACKGROUND("배경"),
    EFFECT("효과"),
    DECORATION("장식"),
    CASE("별통이"),
    BGM("효과음")
}