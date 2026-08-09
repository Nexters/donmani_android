package com.gowoon.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gowoon.designsystem.component.BaseRoundedButton
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.home.R
import com.gowoon.model.fortune.Fortune
import com.gowoon.ui.component.FortuneCard

@Composable
internal fun FortuneDialog(
    fortuneData: Fortune,
    showAdditionalInfo: Boolean,
    isTodayExpenseExist: Boolean?,
    showNotificationButton: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onNavigateToRecord: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        FortuneDialogContent(
            modifier = modifier,
            fortuneData = fortuneData,
            showAdditionalInfo = showAdditionalInfo,
            isTodayExpenseExist = isTodayExpenseExist,
            showNotificationButton = showNotificationButton,
            onDismissRequest = onDismissRequest,
            onClickNotificationSetting = onClickNotificationSetting,
            onNavigateToRecord = onNavigateToRecord
        )
    }
}

@Composable
private fun FortuneDialogContent(
    modifier: Modifier = Modifier,
    fortuneData: Fortune,
    showAdditionalInfo: Boolean,
    isTodayExpenseExist: Boolean?,
    showNotificationButton: Boolean,
    onDismissRequest: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onNavigateToRecord: () -> Unit
) {
    FortuneCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        fortune = fortuneData
    ) {
        FortuneButton(
            showAdditionalInfo = showAdditionalInfo,
            isTodayExpenseExist = isTodayExpenseExist,
            showNotificationButton = showNotificationButton,
            onDismissRequest = onDismissRequest,
            onClickNotificationSetting = onClickNotificationSetting,
            onNavigateToRecord = onNavigateToRecord
        )
    }
}

@Composable
private fun FortuneButton(
    showAdditionalInfo: Boolean,
    isTodayExpenseExist: Boolean?,
    showNotificationButton: Boolean,
    onDismissRequest: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onNavigateToRecord: () -> Unit
) {
    val showRecordButton = isTodayExpenseExist == false
    Column {
        BaseRoundedButton(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DonmaniTheme.colors.Gray99,
            contentColor = DonmaniTheme.colors.DeepBlue20,
            radius = 16.dp,
            verticalPadding = 16.dp,
            horizontalPadding = 16.dp,
            label = if (showRecordButton) stringResource(R.string.fortune_record_button)
                    else stringResource(R.string.fortune_button),
            onClick = if (showRecordButton) onNavigateToRecord else onDismissRequest
        )
        if (showNotificationButton) {
            BaseRoundedButton(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                backgroundColor = DonmaniTheme.colors.PurpleBlue95,
                contentColor = DonmaniTheme.colors.DeepBlue20,
                radius = 16.dp,
                verticalPadding = 16.dp,
                horizontalPadding = 16.dp,
                label = stringResource(R.string.fortune_notification_button),
                onClick = onClickNotificationSetting
            )
        }
        if (showAdditionalInfo) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                text = stringResource(R.string.fortune_additional_info),
                style = DonmaniTheme.typography.Body3,
                color = DonmaniTheme.colors.Gray40
            )
        }
    }
}
