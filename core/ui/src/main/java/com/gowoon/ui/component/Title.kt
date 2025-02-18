package com.gowoon.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.gowoon.designsystem.theme.DonmaniTheme

// subtitle, icon on 모드 사용되는 곳 없어서 우선 고려하지 않고 만들어둔 component
@Composable
fun Title(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = DonmaniTheme.typography.Heading1.copy(fontWeight = FontWeight.Bold),
        color = DonmaniTheme.colors.Gray95
    )
}