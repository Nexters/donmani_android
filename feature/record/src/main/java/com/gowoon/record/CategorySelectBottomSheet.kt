package com.gowoon.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Category
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.model.record.getTitle
import com.gowoon.ui.component.CategorySelectChip

@Composable
internal fun CategorySelectBottomSheet(
    type: ConsumptionType,
    selected: Category? = null,
    onChangedValue: (Category) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(selected) }
    BottomSheet(
        title = stringResource(R.string.category_select_title, type.title),
        buttonType = BottomSheetButtonType.Single(
            title = stringResource(R.string.btn_category_select_done),
            enable = selectedCategory != null
        ),
        content = {
            CategoryGrid(type = type, selected = selectedCategory) {
                selectedCategory = it
            }
        },
        onClick = { _ ->
            selectedCategory?.let {
                onChangedValue(it)
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun CategoryGrid(
    modifier: Modifier = Modifier,
    type: ConsumptionType,
    selected: Category?,
    onClickItem: (Category) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val items = when (type) {
            ConsumptionType.GOOD -> GoodCategory.entries
            ConsumptionType.BAD -> BadCategory.entries
        }
        items(items) {
            CategoryGridItem(
                type = type,
                category = it,
                selected = selected,
                onClickItem = onClickItem
            )
        }
    }
}

@Composable
private fun CategoryGridItem(
    type: ConsumptionType,
    category: Category,
    selected: Category?,
    onClickItem: (Category) -> Unit
) {
    Column(
        modifier = Modifier.noRippleClickable { onClickItem(category) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CategorySelectChip(
            modifier = Modifier.alpha(if (selected == null || category == selected) 1f else 0.4f),
            category = category,
            selected = category == selected
        )
        Text(
            text = category.getTitle(type),
            color = if (category == selected) DonmaniTheme.colors.DeepBlue99 else DonmaniTheme.colors.DeepBlue90,
            style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}