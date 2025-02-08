package com.gowoon.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.ui.noRippleClickable


@Composable
internal fun TodayYesterdayToggle(
    modifier: Modifier = Modifier,
    options: Array<String>,
    onClick: (String) -> Unit
) {
    var selected by remember { mutableStateOf(options[options.lastIndex]) }
    LaunchedEffect(selected) {
        onClick(selected)
    }
    Row(
        modifier = modifier
            .wrapContentWidth()
            .height(24.dp)
            .padding(horizontal = 8.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            TitleButton(
                title = option,
                isSelected = selected == option
            ) { selected = option }
            if (index < options.lastIndex) {
                Divider()
            }
        }
    }
}

@Composable
private fun TitleButton(
    title: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Text(
        modifier = Modifier.noRippleClickable { onClick(title) },
        text = title,
        style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold),
        color = if (isSelected) DonmaniTheme.colors.Gray95 else DonmaniTheme.colors.DeepBlue70
    )
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .padding(vertical = 5.dp)
            .background(DonmaniTheme.colors.DeepBlue70)
    )
}

@Preview
@Composable
private fun TogglePreview() {
    TodayYesterdayToggle(options = arrayOf("어제", "오늘")) { }
}