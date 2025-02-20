package com.gowoon.donmani_android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.theme.pretendard_fontfamily
import kotlinx.coroutines.delay

// 현재는 splash에서 수행할 로직이 없어서 only 디자인용으로, 그냥 appmodule에 구현
@Composable
internal fun SplashScreen(
    navigateToHome: () -> Unit
) {
    LaunchedEffect(true) {
        delay(200)
        navigateToHome()
    }
    Box(Modifier
        .fillMaxSize()
        .padding(horizontal = DonmaniTheme.dimens.Margin20)) {
        TitleLogo(Modifier.padding(top = 120.dp))
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = ImageVector.vectorResource(R.drawable.splash_icon),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Composable
private fun TitleLogo(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append("나에게 의미있는\n소비를 발견하는\n")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("별별소")
            }
        },
        color = DonmaniTheme.colors.Common0,
        style = TextStyle(
            fontFamily = pretendard_fontfamily,
            fontSize = 32.sp
        )
    )
}