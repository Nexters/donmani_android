package com.gowoon.fortune

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FortuneScreen(
    viewModel: FortuneViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    navigateToRecord: (Boolean, Boolean, String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FortuneContent(
        state = state,
        onClickBack = onClickBack,
        onClickRetry = { viewModel.setEvent(FortuneEvent.Retry) },
        navigateToRecord = navigateToRecord
    )
}

@Composable
private fun FortuneContent(
    state: FortuneState,
    onClickBack: () -> Unit,
    onClickRetry: () -> Unit,
    navigateToRecord: (Boolean, Boolean, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF060C25), Color(0xFF09164B))
                )
            )
    ) {
        Column(Modifier.fillMaxSize()) {
            AppBar(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClickNavigation = onClickBack,
                title = stringResource(
                    R.string.fortune_history_title,
                    state.fortunes.lastOrNull()?.date?.monthValue ?: LocalDate.now().monthValue
                )
            )
            when {
                state.isLoading -> LoadingContent()
                state.errorMessage != null -> ErrorContent(onClickRetry)
                state.fortunes.isEmpty() -> EmptyContent()
                else -> FortunePager(
                    fortunes = state.fortunes,
                    hasTodayRecord = state.hasTodayRecord,
                    hasYesterdayRecord = state.hasYesterdayRecord,
                    navigateToRecord = navigateToRecord
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = DonmaniTheme.colors.DeepBlue99)
    }
}

@Composable
private fun EmptyContent() {
    MessageContent(
        title = stringResource(R.string.fortune_empty_title),
        description = stringResource(R.string.fortune_empty_description)
    )
}

@Composable
private fun ErrorContent(onClickRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.fortune_error_title),
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
            label = stringResource(R.string.fortune_retry),
            onClick = onClickRetry
        )
    }
}

@Composable
private fun MessageContent(title: String, description: String) {
    Column(
        modifier = Modifier
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
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.Gray95,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FortunePager(
    fortunes: List<Fortune>,
    hasTodayRecord: Boolean,
    hasYesterdayRecord: Boolean,
    navigateToRecord: (Boolean, Boolean, String) -> Unit
) {
    // 원형 스크롤: 가상의 넓은 페이지 범위를 두고 실제 인덱스로 모듈러 매핑한다.
    // 마지막에서 더 스와이프하면 처음으로, 처음에서 더 스와이프하면 마지막으로 이어진다.
    val isCircular = fortunes.size > 1
    val initialPage = circularPageOf(fortunes.lastIndex, fortunes.size)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { if (isCircular) CIRCULAR_PAGE_COUNT else fortunes.size }
    )
    val flippedDates = remember { mutableStateMapOf<LocalDate, Boolean>() }
    val scope = rememberCoroutineScope()
    // 진입 시 처음 보이는 카드를 한 번만 흔들어 뒤집을 수 있다는 걸 알린다.
    var flipHintPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(fortunes.size) {
        if (fortunes.isNotEmpty()) {
            pagerState.scrollToPage(circularPageOf(fortunes.lastIndex, fortunes.size))
        }
    }

    Column {
        FortuneWeekCalendar(
            fortunes = fortunes,
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
            contentPadding = PaddingValues(horizontal = 37.dp),
            pageSpacing = 20.dp
        ) { page ->
            val fortune = fortunes[page.mod(fortunes.size)]
            val isFlipped = flippedDates[fortune.date] == true
            val showRecordButton = shouldShowRecordButton(
                date = fortune.date,
                hasTodayRecord = hasTodayRecord,
                hasYesterdayRecord = hasYesterdayRecord
            )
            FortuneFlipCard(
                fortune = fortune,
                isFlipped = isFlipped,
                showRecordButton = showRecordButton,
                hasTodayRecord = hasTodayRecord,
                hasYesterdayRecord = hasYesterdayRecord,
                playFlipHint = !flipHintPlayed &&
                        !isFlipped &&
                        page == initialPage &&
                        page == pagerState.currentPage,
                onFlipHintPlayed = { flipHintPlayed = true },
                onClickCard = { flippedDates[fortune.date] = !isFlipped },
                navigateToRecord = navigateToRecord
            )
        }
    }
}

@Composable
private fun FortuneWeekCalendar(
    fortunes: List<Fortune>,
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
                    if (date == LocalDate.now()) {
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
                    Text(
                        text = if (date == LocalDate.now()) "오늘" else "",
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
    isFlipped: Boolean,
    showRecordButton: Boolean,
    hasTodayRecord: Boolean,
    hasYesterdayRecord: Boolean,
    playFlipHint: Boolean,
    onFlipHintPlayed: () -> Unit,
    onClickCard: () -> Unit,
    navigateToRecord: (Boolean, Boolean, String) -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (isFlipped) 180f else 0f)
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
        modifier = Modifier.width(300.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(313.dp)
                .graphicsLayer {
                    // 힌트는 실제 뒤집힘(rotation)에 더해지기만 하고 앞/뒤 판정에는 관여하지 않는다.
                    rotationY = rotation + hintRotation.value
                    cameraDistance = 12f * density.density
                }
                .noRippleClickable(onClickCard)
        ) {
            if (rotation <= 90f) {
                FortuneFront(fortune = fortune, onClickButton = onClickCard)
            } else {
                Box(Modifier.graphicsLayer { rotationY = 180f }) {
                    FortuneBack(fortune = fortune)
                }
            }
        }
        FortuneCardText(fortune)
        if (isFlipped && showRecordButton) {
            BaseRoundedButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = DonmaniTheme.colors.DeepBlue20,
                contentColor = DonmaniTheme.colors.Common0,
                radius = 16.dp,
                verticalPadding = 12.dp,
                horizontalPadding = 16.dp,
                label = if (fortune.date == LocalDate.now()) {
                    stringResource(R.string.fortune_today_record_button)
                } else {
                    stringResource(R.string.fortune_yesterday_record_button)
                },
                onClick = {
                    navigateToRecord(hasTodayRecord, hasYesterdayRecord, "fortune_list")
                }
            )
        }
    }
}

@Composable
private fun FortuneFront(fortune: Fortune, onClickButton: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(DonmaniTheme.colors.DeepBlue70)
    ) {
        if (fortune.imageUrl.isNullOrBlank()) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(180.dp),
                painter = painterResource(com.gowoon.designsystem.R.drawable.fortune_tobby),
                contentDescription = null
            )
        } else {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = fortune.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        BaseRoundedButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .fillMaxWidth(),
            backgroundColor = DonmaniTheme.colors.Gray95,
            contentColor = DonmaniTheme.colors.DeepBlue20,
            radius = 16.dp,
            verticalPadding = 12.dp,
            horizontalPadding = 16.dp,
            label = stringResource(R.string.fortune_front_button),
            onClick = onClickButton
        )
    }
}

@Composable
private fun FortuneBack(fortune: Fortune) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFD6DFFF))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(68.dp),
                painter = painterResource(com.gowoon.designsystem.R.drawable.fortune_tobby),
                contentDescription = null
            )
            Column {
                Text(
                    text = stringResource(R.string.fortune_back_title),
                    style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.Medium),
                    color = DonmaniTheme.colors.PurpleBlue70
                )
                Text(
                    text = stringResource(
                        R.string.fortune_back_date,
                        fortune.date.year % 100,
                        fortune.date.monthValue,
                        fortune.date.dayOfMonth
                    ),
                    style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold),
                    color = DonmaniTheme.colors.DeepBlue20
                )
            }
        }
        Text(
            text = fortune.content,
            style = DonmaniTheme.typography.Body1,
            color = DonmaniTheme.colors.DeepBlue20,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .background(DonmaniTheme.colors.PurpleBlue60, RoundedCornerShape(100.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.fortune_lucky_item, fortune.item),
                style = DonmaniTheme.typography.Body3.copy(fontWeight = FontWeight.Medium),
                color = DonmaniTheme.colors.Common0
            )
        }
    }
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
        Text(
            text = fortune.subtitle.ifBlank { fortune.content },
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.Common0,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private const val CIRCULAR_PAGE_COUNT = Int.MAX_VALUE

/** 화면이 자리를 잡은 뒤 흔들리도록 살짝 늦춘다. */
private const val FLIP_HINT_START_DELAY = 600L
private const val FLIP_HINT_STEP_DURATION = 180
/** 뒤집히는 축으로 좌우 한 번씩 흔들고 제자리로 돌아온다. */
private val FLIP_HINT_SWING = listOf(-16f, 12f, -6f, 0f)

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

private fun shouldShowRecordButton(
    date: LocalDate,
    hasTodayRecord: Boolean,
    hasYesterdayRecord: Boolean
): Boolean {
    val today = LocalDate.now()
    return when (date) {
        today -> !hasTodayRecord
        today.minusDays(1) -> !hasYesterdayRecord
        else -> false
    }
}
