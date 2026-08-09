package com.gowoon.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BaseRoundedButton
import com.gowoon.designsystem.theme.DonmaniTheme

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    title: String,
    retryLabel: String,
    onClickRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        BaseRoundedButton(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DonmaniTheme.colors.Gray95,
            contentColor = DonmaniTheme.colors.DeepBlue20,
            radius = 16.dp,
            verticalPadding = 16.dp,
            horizontalPadding = 16.dp,
            label = retryLabel,
            onClick = onClickRetry
        )
    }
}

@Preview
@Composable
private fun ErrorContentPreview() {
    ErrorContent(
        title = "정보를 불러오지 못했어요",
        retryLabel = "다시 시도하기",
        onClickRetry = {}
    )
}
