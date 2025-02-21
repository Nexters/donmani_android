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
internal fun NoConsumptionBottomSheet(
    onClick: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
){
    BottomSheet(
        title = stringResource(R.string.no_consumption_alert_title),
        buttonType = BottomSheetButtonType.Double(
            positiveTitle = stringResource(R.string.btn_no_consumption_positive),
            negativeTitle = stringResource(R.string.btn_no_consumption_negative)
        ),
        content = {
            Text(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                text = stringResource(R.string.no_consumption_alert_message),
                color = DonmaniTheme.colors.DeepBlue90,
                style = DonmaniTheme.typography.Body2
            )
        },
        onClick = onClick,
        onDismissRequest = onDismissRequest
    )
}