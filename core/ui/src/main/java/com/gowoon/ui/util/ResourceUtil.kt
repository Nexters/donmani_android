package com.gowoon.ui.util

import androidx.compose.ui.graphics.Color
import com.gowoon.designsystem.R
import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Category
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory

fun ConsumptionType.getDefaultResId(): Int = when(this){
    ConsumptionType.GOOD -> R.drawable.happy_default
    ConsumptionType.BAD -> R.drawable.regret_default
}

fun Category.getColor(): Color = when (val category = this) {
    is GoodCategory -> {
        when (category) {
            GoodCategory.ENERGY -> Color(0xffFFC2AA)
            GoodCategory.GROWTH -> Color(0xffBDEC98)
            GoodCategory.HEALING -> Color(0xffB1CFEC)
            GoodCategory.HAPPINESS -> Color(0xffFFBED5)
            GoodCategory.FLEX -> Color(0xffFBDA92)
            GoodCategory.DIGNITY -> Color(0xffC4C4FF)
            GoodCategory.AFFECTION -> Color(0xffFFB8B9)
            GoodCategory.HEALTH -> Color(0xffAAE1C8)
            GoodCategory.NONE -> Color(0xff8E92A0)
        }
    }

    is BadCategory -> {
        when (category) {
            BadCategory.GREED -> Color(0xffFFC2AA)
            BadCategory.ADDICTION -> Color(0xffC4C4FF)
            BadCategory.LAZINESS -> Color(0xffB8C9E0)
            BadCategory.IMPULSE -> Color(0xffFFB8B9)
            BadCategory.MEANINGLESSNESS -> Color(0xffB1CFEC)
            BadCategory.BOASTFULNESS -> Color(0xffFBDA92)
            BadCategory.HABIT -> Color(0xffD9CDBD)
            BadCategory.OVERFRUGALITY -> Color(0xffAAE1C8)
            BadCategory.NONE -> Color(0xff8E92A0)
        }
    }
}

fun Category.getImageResId(): Int = when (val category = this) {
    is GoodCategory -> {
        when (category) {
            GoodCategory.ENERGY -> R.drawable.happy_energy
            GoodCategory.GROWTH -> R.drawable.happy_growth
            GoodCategory.HEALING -> R.drawable.happy_healing
            GoodCategory.HAPPINESS -> R.drawable.happy_happiness
            GoodCategory.FLEX -> R.drawable.happy_flex
            GoodCategory.DIGNITY -> R.drawable.happy_dignity
            GoodCategory.AFFECTION -> R.drawable.happy_affection
            GoodCategory.HEALTH -> R.drawable.happy_health
            GoodCategory.NONE -> R.drawable.happy_none
        }
    }

    is BadCategory -> {
        when (category) {
            BadCategory.GREED -> R.drawable.regret_greed
            BadCategory.ADDICTION -> R.drawable.regret_addiction
            BadCategory.LAZINESS -> R.drawable.regret_laziness
            BadCategory.IMPULSE -> R.drawable.regret_impulse
            BadCategory.MEANINGLESSNESS -> R.drawable.regret_meaninglessness
            BadCategory.BOASTFULNESS -> R.drawable.regret_boastfulness
            BadCategory.HABIT -> R.drawable.regret_habit
            BadCategory.OVERFRUGALITY -> R.drawable.regret_overfrugality
            BadCategory.NONE -> R.drawable.regret_none
        }
    }
}

fun getNoConsumptionColor() = Color(0xff83D8DB)
fun getNoConsumptionResId() = R.drawable.icon_no_consumption