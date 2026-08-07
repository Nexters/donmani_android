package com.gowoon.model.fortune

import java.time.LocalDate

data class Fortune(
    val date: LocalDate,
    val subtitle: String = "",
    val content: String,
    val item: String,
    val imageUrl: String? = null
)
