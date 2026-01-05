package com.gowoon.starbottlelist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.starbottlelist.R

private const val WHEEL_HEIGHT = 120
private const val VISIBLE_ITEM_COUNT = 3

@Composable
internal fun YearPickerBottomSheet(
    items: List<String>,
    initialItem: String = items.last(),
    onItemSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(initialItem) }
    BottomSheet(
        buttonType = BottomSheetButtonType.Single(title = stringResource(R.string.star_bottle_list_year_select_btn_title)),
        content = {
            YearPickerWheel(
                items = items,
                initialItem = initialItem
            ) {
                selectedItem = it
            }
        },
        onClick = { _ ->
            onItemSelected(selectedItem)
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun YearPickerWheel(
    modifier: Modifier = Modifier,
    visibleItemCount: Int = VISIBLE_ITEM_COUNT,
    items: List<String>,
    initialItem: String = items.last(),
    onItemChanged: (String) -> Unit
) {
    val initialIndex = items.indexOf(element = initialItem).coerceAtLeast(0)
    val pagerState = rememberPagerState(initialPage = initialIndex) { items.size }

    LaunchedEffect(pagerState.currentPage) {
        onItemChanged(items[pagerState.currentPage])
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(WHEEL_HEIGHT.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    color = DonmaniTheme.colors.DeepBlue70,
                    shape = RoundedCornerShape(16.dp)
                )
                .align(Alignment.Center)
        )
        VerticalPager(
            modifier = Modifier.fillMaxSize(),
            pageSize = PageSize.Fixed((WHEEL_HEIGHT / visibleItemCount).dp),
            contentPadding = PaddingValues(vertical = (WHEEL_HEIGHT / visibleItemCount).dp),
            pageSpacing = 5.dp,
            state = pagerState,
            beyondViewportPageCount = 1
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = items[it],
                    color = DonmaniTheme.colors.Gray99.copy(alpha = if (pagerState.currentPage == it) 1f else 0.3f),
                    style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}