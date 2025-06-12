package com.gowoon.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.model.record.Category
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.ui.util.getDefaultResId
import com.gowoon.ui.util.getImageResId
import com.gowoon.ui.util.getNoConsumptionResId

@Composable
fun CardCategoryChip(
    modifier: Modifier = Modifier,
    category: Category?,
) {
    CategoryChip(
        modifier = modifier,
        resId = category?.getImageResId() ?: getNoConsumptionResId(), // TODO 배경 있는 애로 받아서 바꾸기
        size = 78.dp,
        radius = 20.dp,
        border = 3.dp
    )
}

@Composable
fun CategorySelectChip(
    modifier: Modifier = Modifier,
    category: Category,
    selected: Boolean = false
) {
    CategoryChip(
        modifier = modifier,
        resId = category.getImageResId(),
        size = 62.dp,
        radius = 16.dp,
        border = if (selected) 2.dp else null
    )
}

@Composable
fun InputCategoryChip(
    modifier: Modifier = Modifier,
    type: ConsumptionType,
    category: Category?
) {
    CategoryChip(
        modifier = modifier,
        resId = category?.getImageResId() ?: type.getDefaultResId(),
        size = 160.dp,
        radius = 44.dp,
        border = if (category == null) null else 6.dp
    )
}

@Composable
fun StatisticsCategoryChip(
    modifier: Modifier = Modifier,
    category: Category
) {
    CategoryChip(
        modifier = modifier,
        resId = category.getImageResId(),
        size = 32.dp,
        radius = 8.dp
    )
}

@Composable
private fun CategoryChip(
    modifier: Modifier = Modifier,
    resId: Int,
    size: Dp,
    radius: Dp,
    border: Dp? = null
) {
    val baseModifier = modifier
        .size(size)
        .clip(shape = RoundedCornerShape(radius))
    val finalModifier = border?.let {
        baseModifier.then(
            Modifier.border(
                width = border,
                color = Color.White,
                shape = RoundedCornerShape(radius)
            )
        )
    } ?: baseModifier
    Icon(
        modifier = finalModifier,
        painter = painterResource(resId),
        tint = Color.Unspecified,
        contentDescription = null
    )
}

@Preview
@Composable
private fun CategoryChipPreview() {
    Column {
        InputCategoryChip(type = ConsumptionType.GOOD, category = GoodCategory.ENERGY)
        CategorySelectChip(category = GoodCategory.ENERGY, selected = true)
    }
}