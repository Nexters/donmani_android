package com.gowoon.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.gowoon.designsystem.R
import com.gowoon.designsystem.component.AppBar

@Composable
internal fun HomeAppBar(
    onClickSetting: () -> Unit
) {
    AppBar(
        navigationIcon = ImageVector.vectorResource(R.drawable.setting),
        onClickNavigation = onClickSetting,
        actionButton = {
            // TODO next update
//            Icon(
//                modifier = Modifier
//                    .size(24.dp)
//                    .noRippleClickable {
//                        // TODO navigate to calendar
//                    },
//                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.calendar),
//                tint = DonmaniTheme.colors.DeepBlue99,
//                contentDescription = null,
//            )
        }
    )
}