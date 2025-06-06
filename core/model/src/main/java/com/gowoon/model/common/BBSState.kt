package com.gowoon.model.common

import com.gowoon.model.record.Record
import com.gowoon.model.reward.Gift

data class BBSState(
    val background: Gift,
    val effect: Gift,
    val decoration: Gift,
    val case: Gift,
    val bgm: Gift,
    val records: List<Record>
)
