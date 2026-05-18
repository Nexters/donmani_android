package com.gowoon.model.fortune

import java.time.LocalDate

data class Fortune(
    val date: LocalDate,
    val content: String,
    val item: String
)
