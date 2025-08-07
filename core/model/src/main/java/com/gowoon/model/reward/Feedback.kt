package com.gowoon.model.reward

import com.gowoon.model.record.Category

data class Feedback(
    val category: Category?,
    val title: String,
    val description: String,
    val nickname: String,
    val isToday: Boolean
)
