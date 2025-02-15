package com.gowoon.record.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.model.record.Category
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.ui.util.getDefaultResId
import com.gowoon.ui.util.getImageResId

@Composable
internal fun CategorySelectChip(
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
internal fun InputCategoryChip(
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
        imageVector = ImageVector.vectorResource(resId),
        tint = Color.Unspecified,
        contentDescription = null
    )
}

@Preview
@Composable
private fun CategoryChipPreview() {
    Column {
        InputCategoryChip(type = ConsumptionType.GOOD, category = GoodCategory.Energy)
        CategorySelectChip(category = GoodCategory.Energy, selected = true)
    }
}