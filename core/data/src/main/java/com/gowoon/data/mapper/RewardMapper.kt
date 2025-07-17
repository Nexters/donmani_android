package com.gowoon.data.mapper

import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import com.gowoon.network.dto.common.RewardDto

fun RewardDto.toModel(): Gift = Gift(
    id = this.id,
    category = GiftCategory.valueOf(this.category),
    name = this.name,
    thumbnailImageUrl = this.thumbnailUrl ?: "",
    resourceUrl = when (GiftCategory.valueOf(category)) {
        GiftCategory.BACKGROUND -> this.imageUrl
        GiftCategory.EFFECT -> this.jsonUrl
        GiftCategory.DECORATION -> this.imageUrl
        GiftCategory.CASE -> this.imageUrl
//        GiftCategory.BGM -> this.mp3Url
    } ?: "",
    isNew = this.newAcquiredFlag,
    hidden = this.hidden,
    hiddenRead = this.hiddenRead
)
