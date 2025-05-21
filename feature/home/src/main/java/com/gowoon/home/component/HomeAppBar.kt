package com.gowoon.home.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable

@Composable
internal fun HomeAppBar(
    onClickSetting: () -> Unit,
    onClickStore: () -> Unit,
) {
    AppBar(
        navigationIcon = ImageVector.vectorResource(R.drawable.setting),
        onClickNavigation = onClickSetting,
        actionButton = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { onClickStore() },
                imageVector = ImageVector.vectorResource(R.drawable.store),
                tint = DonmaniTheme.colors.Common0,
                contentDescription = null,
            )
        }
    )
}