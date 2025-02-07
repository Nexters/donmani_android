package com.gowoon.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.AppBar
import com.gowoon.ui.component.CircleButtonSize
import com.gowoon.ui.component.PlusCircleButton
import com.gowoon.ui.noRippleClickable

@Composable
fun HomeScreen() {
    TransparentScaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = Color.Transparent,
        topBar = { HomeAppBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            HomeHeader()
            Spacer(Modifier.height(95.dp))
            HomeContent()
            HomeFooter(Modifier.weight(1f))
        }
    }
}

@Composable
internal fun HomeAppBar() {
    AppBar(
        navigationIcon = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.setting),
        onClickNavigation = {
            // TODO navigate to setting
        },
        actionButton = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable {
                        // TODO navigate to calendar
                    },
                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.calendar),
                tint = DonmaniTheme.colors.DeepBlue99,
                contentDescription = null,
            )
        }
    )
}

@Composable
internal fun HomeHeader(modifier: Modifier = Modifier) {
    // TODO nickname 받아서 타이틀 구성
    val nickname = "고운"
    Text(
        modifier = modifier,
        text = stringResource(R.string.home_title, nickname),
        style = DonmaniTheme.typography.Heading1.copy(fontWeight = FontWeight.Bold),
        color = DonmaniTheme.colors.Gray95
    )
}

@Composable
internal fun HomeContent(modifier: Modifier = Modifier) {
    // TODO 별사탕
    Box(
        modifier = modifier
            .width(300.dp)
            .height(400.dp)
            .background(Color.White)
    )
}

@Composable
internal fun HomeFooter(modifier: Modifier = Modifier) {
    // TODO 오늘, 어제 기록 여부에 따라 분기 처리 추가
    Box(modifier) {
        PlusCircleButton(
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
fun HomeScreenPreview() {
    HomeScreen()
}