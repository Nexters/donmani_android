package com.gowoon.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.home.component.HomeAppBar
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.CircleButton
import com.gowoon.ui.component.CircleButtonSize
import com.gowoon.ui.component.Title

@Composable
internal fun HomeScreen() {
    // TODO nickname 받아서 타이틀 구성
    val nickname = "고운"
    TransparentScaffold(
        topBar = { HomeAppBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Title(text = stringResource(R.string.home_title, nickname))
            Spacer(Modifier.height(95.dp))
            HomeContent()
            HomeFooter(Modifier.weight(1f))
        }
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    // TODO 별사탕
    Box(
        modifier = modifier
            .width(300.dp)
            .height(400.dp)
            .background(Color.White)
    )
}

@Composable
private fun HomeFooter(modifier: Modifier = Modifier) {
    // TODO 오늘, 어제 기록 여부에 따라 분기 처리 추가
    Box(modifier) {
        CircleButton(
            modifier = Modifier.align(Alignment.Center),
            buttonSize = CircleButtonSize.Big,
            backgroundColor = DonmaniTheme.colors.PurpleBlue70,
            contentColor = DonmaniTheme.colors.PurpleBlue99
        ) {
            // TODO navigte to record
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}