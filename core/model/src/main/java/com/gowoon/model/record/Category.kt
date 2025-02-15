package com.gowoon.model.record

sealed interface Category
enum class GoodCategory(val title: String) : Category {
    Energy("활력"),
    Growth("성장"),
    Healing("힐링"),
    SBDH("소확행"),
    Flex("플렉스"),
    Maintenance("품위유지"),
    Heart("마음전달"),
    Healty("건강"),
    None("없음")
}

enum class BadCategory(val title: String) : Category {
    Greed("욕심"),
    Addiction("중독"),
    Laziness("게으름"),
    Impulse("충동"),
    Meaningless("무의미"),
    Ostentation("과시"),
    Habitual("습관반복"),
    Stingy("과한절약"),
    None("없음")
}