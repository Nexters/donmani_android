package com.gowoon.data.mapper

import com.gowoon.model.record.getCategory
import com.gowoon.model.reward.Feedback
import com.gowoon.network.dto.response.FeedbackResponse

fun FeedbackResponse.toModel(): Feedback = Feedback(
    category = if (this.category == "NONE") null else this.category.getCategory(),
    title = this.title,
    description = this.content,
    nickname = this.name,
    isToday = this.flagType
)