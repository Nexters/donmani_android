package com.gowoon.onboarding.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.gowoon.designsystem.theme.DonmaniTheme

@Composable
internal fun SubTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = DonmaniTheme.typography.Heading3,
        color = DonmaniTheme.colors.Gray60,
        textAlign = TextAlign.Center
    )

}