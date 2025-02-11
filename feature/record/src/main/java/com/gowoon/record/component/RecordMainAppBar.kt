package com.gowoon.record.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.gowoon.designsystem.R
import com.gowoon.model.common.EntryDay
import com.gowoon.ui.component.AppBar

@Composable
internal fun RecordMainAppBar(
    onClickBack: () -> Unit,
    onClickToggle: (EntryDay) -> Unit
) {
    AppBar(
        navigationIcon = ImageVector.vectorResource(R.drawable.arrow_left),
        onClickNavigation = onClickBack,
        actionButton = {
            TodayYesterdayToggle(options = EntryDay.entries) { selected ->
                onClickToggle(selected)
            }
        }
    )
}