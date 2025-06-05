package com.gowoon.data.mapper

import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import com.gowoon.network.dto.common.RewardDto

fun RewardDto.toModel(): Gift = Gift(
    category = GiftCategory.valueOf(this.category),
    name = this.name,
    thumbnailImageUrl = "", // TODO
    resourceUrl = when (GiftCategory.valueOf(category)) {
        GiftCategory.BACKGROUND -> this.imageUrl
        GiftCategory.EFFECT -> this.jsonUrl
        GiftCategory.DECORATION -> this.jsonUrl
        GiftCategory.BOTTLE -> this.imageUrl
        GiftCategory.SOUND -> this.mp3Url
    },
    isNew = this.newAcquiredFlag
)
