package com.gowoon.record.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.record.R

@Composable
internal fun ExitWarningBottomSheet(
    onExpanded: () -> Unit,
    onClick: (Boolean) -> Unit,
    onDismissRequest: (Boolean) -> Unit
) {
    BottomSheet(
        title = stringResource(R.string.exit_warning_title),
        buttonType = BottomSheetButtonType.Double(
            positiveTitle = stringResource(R.string.btn_exit_warning_positive),
            negativeTitle = stringResource(R.string.btn_exit_warning_negative)
        ),
        content = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.exit_warning_message),
                color = DonmaniTheme.colors.DeepBlue90,
                style = DonmaniTheme.typography.Body2
            )
        },
        onClick = onClick,
        onDismissRequestWithAction = onDismissRequest,
        onExpanded = onExpanded
    )
}