package com.gowoon.model.record

sealed interface Category
enum class GoodCategory : Category {
    Type1, Type2, Type3, Type4, Type5, Type6, Type7, None, ETC
}

enum class BadCategory : Category {
    Type1, Type2, Type3, Type4, Type5, Type6, Type7, None, ETC
}