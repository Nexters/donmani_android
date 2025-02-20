package com.gowoon.recordlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.AppBar

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