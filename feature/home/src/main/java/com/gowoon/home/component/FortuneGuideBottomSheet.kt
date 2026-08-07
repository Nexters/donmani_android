package com.gowoon.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BaseRoundedButton
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.home.R

@Composable
internal fun FortuneGuideBottomSheet(
    showNotificationButton: Boolean,
    onDismissRequest: () -> Unit,
    onClickConfirm: () -> Unit,
    onClickNotificationSetting: () -> Unit
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        content = {
            FortuneGuideContent(
                showNotificationButton = showNotificationButton,
                onClickConfirm = onClickConfirm,
                onClickNotificationSetting = onClickNotificationSetting
            )
        },
        isSpaceBetweenBtn = false
    )
}

@Composable
private fun FortuneGuideContent(
    showNotificationButton: Boolean,
    onClickConfirm: () -> Unit,
    onClickNotificationSetting: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.fortune_guide_title),
            style = DonmaniTheme.typography.Heading1.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.fortune_guide_description),
            style = DonmaniTheme.typography.Body1,
            color = DonmaniTheme.colors.Gray95,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Image(
            modifier = Modifier.size(152.dp),
            painter = painterResource(com.gowoon.designsystem.R.drawable.fortune_tobby),
            contentDescription = null
        )
        Spacer(Modifier.height(24.dp))
        BaseRoundedButton(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DonmaniTheme.colors.Gray95,
            contentColor = DonmaniTheme.colors.DeepBlue20,
            radius = 16.dp,
            verticalPadding = 16.dp,
            horizontalPadding = 16.dp,
            label = stringResource(R.string.fortune_guide_confirm),
            onClick = onClickConfirm
        )
        if (showNotificationButton) {
            Spacer(Modifier.height(10.dp))
            BaseRoundedButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DonmaniTheme.colors.DeepBlue50,
                contentColor = DonmaniTheme.colors.Common0,
                radius = 16.dp,
                verticalPadding = 16.dp,
                horizontalPadding = 16.dp,
                label = stringResource(R.string.fortune_notification_button),
                onClick = onClickNotificationSetting
            )
        }
    }
}
