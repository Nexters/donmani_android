package com.gowoon.model.common

import com.gowoon.model.record.Record
import com.gowoon.model.reward.Gift

data class BBSState(
    val background: Gift? = null,
    val effect: Gift? = null,
    val decoration: Gift? = null,
    val case: Gift? = null,
//    val bgm: Gift? = null,
    val records: List<Record> = listOf()
)
