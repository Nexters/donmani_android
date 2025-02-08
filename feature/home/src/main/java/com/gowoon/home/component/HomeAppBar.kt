package com.gowoon.home.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.ui.component.AppBar
import com.gowoon.ui.noRippleClickable

@Composable
internal fun HomeAppBar() {
    AppBar(
        navigationIcon = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.setting),
        onClickNavigation = {
            // TODO navigate to setting
        },
        actionButton = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable {
                        // TODO navigate to calendar
                    },
                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.calendar),
                tint = DonmaniTheme.colors.DeepBlue99,
                contentDescription = null,
            )
        }
    )
}