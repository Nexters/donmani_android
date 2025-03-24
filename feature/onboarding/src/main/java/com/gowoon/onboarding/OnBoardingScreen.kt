package com.gowoon.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.NegativeButton
import com.gowoon.designsystem.component.PositiveButton
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.component.Title
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.onboarding.component.OnBoardingConfirmBottomSheet
import com.gowoon.onboarding.component.OnBoardingEvent
import com.gowoon.onboarding.component.OnBoardingViewModel
import com.gowoon.onboarding.component.Route
import com.gowoon.onboarding.component.Step
import com.gowoon.onboarding.component.SubTitle
import kotlinx.coroutines.launch

@Composable
internal fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToRecord: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.showBottomSheet) {
        OnBoardingConfirmBottomSheet {
            viewModel.setEvent(OnBoardingEvent.ShowBottomSheet(false))
            state.route?.let {
                when (it) {
                    Route.HOME -> navigateToHome()
                    Route.RECORD -> navigateToRecord()
                }
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(DonmaniTheme.colors.DeepBlue20)
            .safeDrawingPadding()
    ) {
        when (state.step) {
            Step.INTRO -> {
                GuideIntro { viewModel.setEvent(OnBoardingEvent.GoToGuide) }
            }

            Step.GUIDE -> {
                GuideScreen { route ->
                    viewModel.setEvent(OnBoardingEvent.ShowBottomSheet(true))
                    viewModel.setEvent(OnBoardingEvent.UpdateNextRoute(route))
                }
            }
        }
    }
}

@Composable
private fun GuideIntro(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
            .padding(top = 76.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(text = stringResource(R.string.onboarding_intro_title))
        Icon(
            modifier = Modifier.padding(vertical = 10.dp),
            painter = painterResource(com.gowoon.designsystem.R.drawable.star),
            tint = Color.Unspecified,
            contentDescription = null
        )
        SubTitle(text = stringResource(R.string.onboarding_intro_subtitle))
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(com.gowoon.designsystem.R.drawable.onboarding_intro_img),
            tint = Color.Unspecified,
            contentDescription = null
        )
        RoundedButton(
            modifier = Modifier.fillMaxWidth(),
            type = RoundedButtonRadius.Row,
            label = stringResource(R.string.onboarding_intro_btn),
            onClick = onClick
        )
    }
}

@Composable
private fun GuideScreen(onClick: (Route) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 5 }
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Indicator(pageCount = pagerState.pageCount, selectedIndex = pagerState.currentPage)
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .weight(1f)
                .padding(top = 20.dp)
        ) { page ->
            val titleId = context.resources.getIdentifier(
                "onboarding${page + 1}_title",
                "string",
                context.packageName
            )
            val subtitleId = context.resources.getIdentifier(
                "onboarding${page + 1}_subtitle",
                "string",
                context.packageName
            )
            val imgId = context.resources.getIdentifier(
                "onboarding_img${page + 1}",
                "drawable",
                context.packageName
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(text = stringResource(titleId))
                Spacer(Modifier.height(26.dp))
                SubTitle(text = stringResource(subtitleId))
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(imgId),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        }
        if (pagerState.currentPage == pagerState.pageCount - 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DonmaniTheme.dimens.Margin20),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NegativeButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.onboarding_btn_go_home)
                ) { onClick(Route.HOME) }
                PositiveButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.onboarding_btn_go_record)
                ) { onClick(Route.RECORD) }
            }
        } else {
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DonmaniTheme.dimens.Margin20),
                type = RoundedButtonRadius.Row,
                label = stringResource(R.string.onboarding_btn_next)
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }
    }
}

@Composable
private fun Indicator(pageCount: Int, selectedIndex: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(pageCount) { index ->
            val alpha = if (index == selectedIndex) 1f else 0.1f
            Box(
                Modifier
                    .size(6.dp)
                    .background(Color.White.copy(alpha = alpha), shape = CircleShape)
            )
        }
    }
}