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
            GoodCategory.Energy -> Color(0xffFFC2AA)
            GoodCategory.Growth -> Color(0xffBDEC98)
            GoodCategory.Healing -> Color(0xffB1CFEC)
            GoodCategory.SBDH -> Color(0xffFFBED5)
            GoodCategory.Flex -> Color(0xffFBDA92)
            GoodCategory.Maintenance -> Color(0xffC4C4FF)
            GoodCategory.Heart -> Color(0xffFFB8B9)
            GoodCategory.Healty -> Color(0xffAAE1C8)
            GoodCategory.None -> Color(0xff8E92A0)
        }
    }

    is BadCategory -> {
        when (category) {
            BadCategory.Greed -> Color(0xffFFC2AA)
            BadCategory.Addiction -> Color(0xffC4C4FF)
            BadCategory.Laziness -> Color(0xffB8C9E0)
            BadCategory.Impulse -> Color(0xffFFB8B9)
            BadCategory.Meaningless -> Color(0xffC5CDD8)
            BadCategory.Ostentation -> Color(0xffFBDA92)
            BadCategory.Habitual -> Color(0xffD9CDBD)
            BadCategory.Stingy -> Color(0xffAAE1C8)
            BadCategory.None -> Color(0xff8E92A0)
        }
    }
}

fun Category.getImageResId(): Int = when (val category = this) {
    is GoodCategory -> {
        when (category) {
            GoodCategory.Energy -> R.drawable.happy_energy
            GoodCategory.Growth -> R.drawable.happy_growth
            GoodCategory.Healing -> R.drawable.happy_healing
            GoodCategory.SBDH -> R.drawable.happy_sbdh
            GoodCategory.Flex -> R.drawable.happy_flex
            GoodCategory.Maintenance -> R.drawable.happy_maintenance
            GoodCategory.Heart -> R.drawable.happy_heart
            GoodCategory.Healty -> R.drawable.happy_healthy
            GoodCategory.None -> R.drawable.happy_none
        }
    }

    is BadCategory -> {
        when (category) {
            BadCategory.Greed -> R.drawable.regret_greed
            BadCategory.Addiction -> R.drawable.regret_addiction
            BadCategory.Laziness -> R.drawable.regret_laziness
            BadCategory.Impulse -> R.drawable.regret_impulse
            BadCategory.Meaningless -> R.drawable.regret_meaningless
            BadCategory.Ostentation -> R.drawable.regret_ostentation
            BadCategory.Habitual -> R.drawable.regret_habitual
            BadCategory.Stingy -> R.drawable.regret_stingy
            BadCategory.None -> R.drawable.regret_none
        }
    }
}

fun getNoConsumptionColor() = Color(0xff83D8DB)
fun getNoConsumptionResId() = R.drawable.icon_no_consumption