package com.gowoon.recordlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gowoon.designsystem.component.AppBar
import com.gowoon.ui.TransparentScaffold

@Composable
internal fun RecordListScreen(
    onClickBack: () -> Unit
) {
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.appbar_title),
                onClickNavigation = onClickBack
            )
        }
    ) {

    }
}