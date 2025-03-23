package com.gowoon.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.theme.pretendard_fontfamily
import kotlinx.coroutines.delay

@Composable
internal fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToOnBoarding: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var navigate by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        navigate = true
    }
    LaunchedEffect(navigate, state.nextRoute) {
        if (navigate) {
            state.nextRoute?.let {
                when (it) {
                    Route.Home -> navigateToHome()
                    Route.OnBoarding -> navigateToOnBoarding()
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
    ) {
        TitleLogo(Modifier.padding(top = 120.dp))
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.splash_icon),
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