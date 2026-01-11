package com.gowoon.onboarding.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.onboarding.R

@Composable
internal fun SkipButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        modifier = modifier.noRippleClickable { onClick() },
        text = stringResource(R.string.onboarding_skip_btn_title),
        style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold),
        color = DonmaniTheme.colors.DeepBlue80
    )
}