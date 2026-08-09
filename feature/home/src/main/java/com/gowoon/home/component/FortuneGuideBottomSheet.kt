package com.gowoon.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.NegativeButton
import com.gowoon.designsystem.component.PositiveButton
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.home.R

@Composable
internal fun FortuneGuideBottomSheet(
    showNotificationButton: Boolean,
    onDismissRequest: () -> Unit,
    onClickNotificationSetting: () -> Unit
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        canDismiss = false,
        isSpaceBetweenBtn = false,
        content = {
            FortuneGuideContent(
                showNotificationButton = showNotificationButton,
                onClickConfirm = onDismissRequest,
                onClickNotificationSetting = onClickNotificationSetting
            )
        }
    )
}

@Composable
private fun FortuneGuideContent(
    showNotificationButton: Boolean,
    onClickConfirm: () -> Unit,
    onClickNotificationSetting: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
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
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(com.gowoon.designsystem.R.drawable.fortune_guide),
            contentDescription = null
        )
        PositiveButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.fortune_guide_confirm),
            onClick = onClickConfirm
        )
        if (showNotificationButton) {
            Spacer(Modifier.height(10.dp))
            NegativeButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.fortune_notification_button),
                onClick = onClickNotificationSetting
            )
        }
    }
}