package com.gowoon.fortune

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.BaseRoundedButton
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.fortune.Fortune
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.component.EmptyContent
import com.gowoon.ui.component.FortuneCard
import com.gowoon.ui.component.ErrorContent
import com.gowoon.ui.component.LoadingContent
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** 기록 화면에 어디서 들어왔는지 알려주는 값 */
private const val FORTUNE_LIST_REFERRER = "fortune_list"

private const val CIRCULAR_PAGE_COUNT = Int.MAX_VALUE

/** 카드가 앞뒤로 뒤집히는 데 걸리는 시간 */
private const val FLIP_DURATION = 400

/** 화면이 자리를 잡은 뒤 흔들리도록 살짝 늦춘다. */
private const val FLIP_HINT_START_DELAY = 600L
private const val FLIP_HINT_STEP_DURATION = 180

/** 뒤집히는 축으로 좌우 한 번씩 흔들고 제자리로 돌아온다. */
private val FLIP_HINT_SWING = listOf(-16f, 12f, -6f, 0f)

/** 기록하기 버튼이 뜨고 사라지는 데 걸리는 시간 */
private const val RECORD_BUTTON_FADE_DURATION = 400

@Composable
internal fun FortuneScreen(
    viewModel: FortuneViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    navigateToRecord: (Boolean, Boolean, String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BBSScaffold(
        background = { GradientBackground() },
        topBar = { AppBar(
            title = stringResource(R.string.fortune_history_title, state.displayMonth),
            applyPadding = true,
            onClickNavigation = onClickBack
        ) },
        applyPadding = false
    ) {
        FortuneContent(
            modifier = Modifier.padding(it),
            state = state,
            onClickRetry = { viewModel.setEvent(FortuneEvent.Retry) },
            onClickRecord = {
                navigateToRecord(
                    state.hasTodayRecord ?: true,
                    state.hasYesterdayRecord ?: true,
                    FORTUNE_LIST_REFERRER
                )
            }
        )
    }
}

@Composable
private fun FortuneContent(
    modifier: Modifier = Modifier,
    state: FortuneState,
    onClickRetry: () -> Unit,
    onClickRecord: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        when {
            state.isLoading -> LoadingContent()
            state.errorMessage != null -> ErrorContent(
                title = stringResource(R.string.fortune_error_title),
                retryLabel = stringResource(R.string.fortune_retry),
                onClickRetry = onClickRetry
            )

            state.fortunes.isEmpty() -> EmptyContent(
                title = stringResource(R.string.fortune_empty_title),
                description = stringResource(R.string.fortune_empty_description)
            )

            else -> FortunePager(
                fortunes = state.fortunes,
                today = state.today,
                showRecordButton = state::showRecordButton,
                onClickRecord = onClickRecord
            )
        }
    }
}

@Composable
private fun FortunePager(
    fortunes: List<Fortune>,
    today: LocalDate,
    showRecordButton: (LocalDate) -> Boolean,
    onClickRecord: () -> Unit
) {
    // 원형 스크롤: 가상의 넓은 페이지 범위를 두고 실제 인덱스로 모듈러 매핑한다.
    // 마지막에서 더 스와이프하면 처음으로, 처음에서 더 스와이프하면 마지막으로 이어진다.
    val isCircular = fortunes.size > 1
    val initialPage = circularPageOf(fortunes.lastIndex, fortunes.size)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { if (isCircular) CIRCULAR_PAGE_COUNT else fortunes.size }
    )
    val scope = rememberCoroutineScope()

    // flip
    val flippedDates = remember { mutableStateMapOf<LocalDate, Boolean>() }

    // flip hint
    var flipHintPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(fortunes.size) {
        if (fortunes.isNotEmpty()) {
            pagerState.scrollToPage(circularPageOf(fortunes.lastIndex, fortunes.size))
        }
    }

    Column {
        FortuneWeekCalendar(
            fortunes = fortunes,
            today = today,
            selectedDate = fortunes[pagerState.currentPage.mod(fortunes.size)].date,
            onClickDate = { date ->
                fortunes.indexOfFirst { it.date == date }.takeIf { it >= 0 }?.let { targetIndex ->
                    scope.launch {
                        pagerState.animateScrollToPage(
                            nearestCircularPage(
                                currentPage = pagerState.currentPage,
                                targetIndex = targetIndex,
                                size = fortunes.size
                            )
                        )
                    }
                }
            }
        )
        Spacer(Modifier.height(64.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 35.dp),
            pageSpacing = 20.dp,
            // 기본값(CenterVertically)이면 기록 버튼이 있는 페이지만 세로로 길어져
            // 그 카드가 버튼 높이의 절반만큼 위로 뜬다. 위를 기준으로 맞춰 카드 높이를 고정한다.
            verticalAlignment = Alignment.Top
        ) { page ->
            val fortune = fortunes[page.mod(fortunes.size)]
            val isFlipped = flippedDates[fortune.date] == true
            FortuneFlipCard(
                fortune = fortune,
                today = today,
                isFlipped = isFlipped,
                showRecordButton = showRecordButton(fortune.date),
                playFlipHint = !flipHintPlayed &&
                        !isFlipped &&
                        page == initialPage &&
                        page == pagerState.currentPage,
                onFlipHintPlayed = { flipHintPlayed = true },
                onClickCard = { flippedDates[fortune.date] = !isFlipped },
                onClickRecord = onClickRecord
            )
        }
    }
}

@Composable
private fun FortuneWeekCalendar(
    fortunes: List<Fortune>,
    today: LocalDate,
    selectedDate: LocalDate,
    onClickDate: (LocalDate) -> Unit
) {
    val days = fortunes.map { it.date }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { date ->
                Text(
                    modifier = Modifier.width(40.dp),
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                    style = DonmaniTheme.typography.Body3.copy(fontWeight = FontWeight.Bold),
                    color = DonmaniTheme.colors.DeepBlue90.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { date ->
                Column(
                    modifier = Modifier
                        .width(40.dp)
                        .noRippleClickable { onClickDate(date) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (date == today) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color(0xFF15BD66), CircleShape)
                        )
                    } else {
                        Spacer(Modifier.height(4.dp))
                    }
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = DonmaniTheme.typography.Body3.copy(fontWeight = FontWeight.Bold),
                        color = if (date == selectedDate) Color(0xFF15BD66) else DonmaniTheme.colors.Common0,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (date == today) "오늘" else "",
                        style = DonmaniTheme.typography.Body4.copy(fontWeight = FontWeight.Bold),
                        color = DonmaniTheme.colors.DeepBlue70,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun FortuneFlipCard(
    fortune: Fortune,
    today: LocalDate,
    isFlipped: Boolean,
    showRecordButton: Boolean,
    playFlipHint: Boolean,
    onFlipHintPlayed: () -> Unit,
    onClickCard: () -> Unit,
    onClickRecord: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = FLIP_DURATION, easing = FastOutSlowInEasing)
    )
    val hintRotation = remember { Animatable(0f) }
    val density = LocalDensity.current

    LaunchedEffect(playFlipHint) {
        if (!playFlipHint) {
            // 힌트 도중 카드를 뒤집거나 페이지를 넘기면 여기로 들어와 각도를 되돌린다.
            hintRotation.snapTo(0f)
            return@LaunchedEffect
        }
        delay(FLIP_HINT_START_DELAY)
        FLIP_HINT_SWING.forEach { target ->
            hintRotation.animateTo(
                targetValue = target,
                animationSpec = tween(FLIP_HINT_STEP_DURATION, easing = FastOutSlowInEasing)
            )
        }
        onFlipHintPlayed()
    }

    Column(
        // 폭은 pager 의 contentPadding 이 정한다. 여기서 고정하면 화면 폭에 따라 슬롯과 어긋난다.
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(313.dp)
                .graphicsLayer {
                    // 힌트는 실제 뒤집힘(rotation)에 더해지기만 하고 앞/뒤 판정에는 관여하지 않는다.
                    rotationY = rotation + hintRotation.value
                    cameraDistance = 12f * density.density
                }
                .noRippleClickable(onClickCard)
        ) {
            if (rotation <= 90f) {
                FortuneFront(fortune = fortune)
            } else {
                Box(Modifier.graphicsLayer { rotationY = 180f }) {
                    FortuneBack(fortune = fortune)
                }
            }
        }
        FortuneCardText(fortune)
        // 버튼은 뒷면에 딸린 요소라, 카드가 90도를 넘겨 뒷면이 드러나는 순간에 맞춰 나타난다.
        AnimatedVisibility(
            visible = showRecordButton && rotation > 90f,
            enter = fadeIn(tween(RECORD_BUTTON_FADE_DURATION)),
            exit = fadeOut(tween(RECORD_BUTTON_FADE_DURATION))
        ) {
            BaseRoundedButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DonmaniTheme.colors.DeepBlue20,
                contentColor = DonmaniTheme.colors.Common0,
                radius = 16.dp,
                verticalPadding = 12.dp,
                horizontalPadding = 16.dp,
                label = if (fortune.date == today) {
                    stringResource(R.string.fortune_today_record_button)
                } else {
                    stringResource(R.string.fortune_yesterday_record_button)
                },
                onClick = onClickRecord
            )
        }
    }
}

@Composable
private fun FortuneFront(fortune: Fortune) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(DonmaniTheme.colors.DeepBlue70)
    ) {
        fortune.imageUrl?.let {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun FortuneBack(fortune: Fortune) {
    FortuneCard(
        modifier = Modifier.fillMaxSize(),
        fortune = fortune,
        contentMaxLines = 5
    )
}

@Composable
private fun FortuneCardText(fortune: Fortune) {
    Column {
        Text(
            text = stringResource(
                R.string.fortune_date_label,
                fortune.date.monthValue,
                fortune.date.dayOfMonth
            ),
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.Common0
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = fortune.subtitle.ifBlank { fortune.content },
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.Common0,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** [targetIndex] 를 가리키면서 가상 범위 한가운데에 위치한 페이지를 구한다. */
private fun circularPageOf(targetIndex: Int, size: Int): Int {
    if (size <= 1) return targetIndex.coerceAtLeast(0)
    val middle = CIRCULAR_PAGE_COUNT / 2
    return middle - middle.mod(size) + targetIndex
}

/** [currentPage] 기준으로 [targetIndex] 에 가장 짧은 방향으로 닿는 가상 페이지를 구한다. */
private fun nearestCircularPage(currentPage: Int, targetIndex: Int, size: Int): Int {
    if (size <= 1) return targetIndex.coerceAtLeast(0)
    val forward = (targetIndex - currentPage).mod(size)
    val backward = forward - size
    return currentPage + if (forward <= -backward) forward else backward
}
