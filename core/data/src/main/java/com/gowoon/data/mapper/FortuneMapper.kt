package com.gowoon.data.mapper

import com.gowoon.model.fortune.Fortune
import com.gowoon.network.dto.response.FortuneResponse
import java.time.LocalDate

fun FortuneResponse.toModel(): Fortune = Fortune(
    date = LocalDate.parse(this.targetDate),
    content = this.content,
    item = this.item
)