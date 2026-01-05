package com.gowoon.starbottlelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.designsystem.component.CustomSnackBarHost
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.theme.pretendard_fontfamily
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.BottleState
import com.gowoon.starbottlelist.component.StarBottleListAppBar
import com.gowoon.starbottlelist.component.YearPickerBottomSheet
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.component.NoticeBanner
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
internal fun StarBottleListScreen(
    viewModel: StarBottleListViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickBottle: (Int, Int, String) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

//    val adView = rememberAdView(context, configuration)

    val pagerState =
        rememberPagerState(initialPage = state.yearList.indexOf(state.selectedYear)) { state.yearList.size }


    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest {
            if (it is StarBottleListEffect.ShowToast) {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    LaunchedEffect(state.selectedYear) {
        pagerState.scrollToPage(state.yearList.indexOf(state.selectedYear))
    }

//    LaunchedEffect(Unit) {
//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)
//    }

    if (state.showYearPickerBottomSheet) {
        YearPickerBottomSheet(
            items = state.yearList.map { it.toString() },
            initialItem = state.selectedYear.toString(),
            onItemSelected = {
                it.toIntOrNull()?.let {
                    viewModel.setEvent(StarBottleListEvent.ChangeCurrentYear(it))
                    viewModel.setEvent(StarBottleListEvent.FetchMonthlySummaryData(it))
                }
            }
        ) {
            viewModel.setEvent(StarBottleListEvent.ShowYearPicker(false))
        }
    }

    BBSScaffold(
        background = { GradientBackground() },
        topBar = {
            StarBottleListAppBar(
                year = state.selectedYear.toString(),
                onClickArrow = {
                    viewModel.setEvent(StarBottleListEvent.ShowYearPicker(true))
                },
                onClickBack = onClickBack
            )
        },
        snackbarHost = { CustomSnackBarHost(snackbarHostState) }
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1
        ) { page ->
            LaunchedEffect(page) {
                viewModel.setEvent(StarBottleListEvent.FetchMonthlySummaryData(state.yearList[page]))
            }
            LaunchedEffect(pagerState.currentPage) {
                viewModel.setEvent(StarBottleListEvent.ChangeCurrentYear(state.yearList[pagerState.currentPage]))
            }

            LazyVerticalGrid(
                modifier = Modifier.padding(it),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(top = 8.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
//                item(span = { GridItemSpan(3) }) {
//                    AdBanner(adView, Modifier.clip(shape = RoundedCornerShape(16.dp)))
//                }
                if (state.showBanner) {
                    item(span = { GridItemSpan(3) }) {
                        StarBottleListHeader { viewModel.setEvent(StarBottleListEvent.HideBanner) }
                    }
                }
                state.monthlySummaryList[state.yearList[page]]?.let {
                    items(it) {
                        StarBottleListItem(it.first, it.second) {
                            FirebaseAnalyticsUtil.sendEvent(
                                trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                                eventName = "list_별통이_button",
                                params = mutableListOf(
                                    Pair(
                                        "별통이_id",
                                        LocalDate.now().year.toString().takeLast(2) + String.format(
                                            "%02d",
                                            it.first
                                        )
                                    )
                                )
                            )
                            when (val bottleState = it.second) {
                                is BottleState.OPENED -> {
                                    if (bottleState.count == 0) {
                                        viewModel.showToast(context.getString(R.string.star_bottle_list_no_record_toast_message))
                                    } else {
                                        onClickBottle(
                                            state.yearList[page],
                                            it.first,
                                            BottleState.OPENED::class.simpleName ?: "OPENED"
                                        )
                                    }
                                }

                                is BottleState.LOCKED -> {
                                    onClickBottle(
                                        state.yearList[page],
                                        it.first,
                                        BottleState.LOCKED::class.simpleName ?: "LOCKED"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

//    DisposableEffect(Unit) {
//        onDispose { adView.destroy() }
//    }
}

@Composable
private fun StarBottleListHeader(modifier: Modifier = Modifier, onClick: () -> Unit) {
    NoticeBanner(modifier = modifier) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.star_bottle_list_header_notice_message),
            style = DonmaniTheme.typography.Body1,
            color = DonmaniTheme.colors.Gray95
        )
        Icon(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable { onClick() },
            imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.close),
            tint = DonmaniTheme.colors.DeepBlue99,
            contentDescription = null
        )
    }
}

@Composable
private fun StarBottleListItem(
    month: Int,
    state: BottleState,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .width(100.dp)
                .height(116.dp)
                .noRippleClickable { onClick() }
        ) {
            when (state) {
                is BottleState.LOCKED -> {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(com.gowoon.designsystem.R.drawable.star_bottle_thumbnail_locked),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }

                is BottleState.OPENED -> {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(com.gowoon.designsystem.R.drawable.star_bottle_thumbnail),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(com.gowoon.designsystem.R.drawable.star_thumbnail),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontFamily = pretendard_fontfamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DonmaniTheme.colors.Gray80
                                    )
                                ) {
                                    append(state.count.toString())
                                }
                                append("/${state.total}")
                            },
                            style = DonmaniTheme.typography.Body3.copy(fontWeight = FontWeight.SemiBold),
                            color = DonmaniTheme.colors.DeepBlue80
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "${month}월",
            style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold),
            color = if (state is BottleState.LOCKED) DonmaniTheme.colors.DeepBlue80 else DonmaniTheme.colors.Gray99,
            textAlign = TextAlign.Center,
        )
    }
}