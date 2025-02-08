package com.gowoon.record.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.gowoon.designsystem.R
import com.gowoon.ui.component.AppBar

@Composable
internal fun RecordMainAppBar() {
    // TODO 어제, 오늘 기록 가능한 것 어떻게 관리할 지 고민 ( domain, business 개념 )
    val options = arrayOf("어제", "오늘")
    AppBar(
        navigationIcon = ImageVector.vectorResource(R.drawable.arrow_left),
        onClickNavigation = {
            // TODO navigation pop back stack
        },
        actionButton = {
            TodayYesterdayToggle(options = options) { selected ->
                // TODO selected 처리
            }
        }
    )
}

@Preview
@Composable
private fun RecordMainAppBarPreview() {
    RecordMainAppBar()
}