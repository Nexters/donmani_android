package com.gowoon.motivation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.NegativeButton
import com.gowoon.designsystem.component.PositiveButton
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.getTitle
import com.gowoon.model.reward.Feedback
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import com.gowoon.motivation.component.FeedbackCard
import com.gowoon.motivation.component.FirstAccessBottomSheet
import com.gowoon.motivation.component.RewardBackground
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.component.MessageBox
import com.gowoon.ui.util.getNoConsumptionTitle
import kotlinx.coroutines.delay

@Composable
internal fun RewardScreen(
    viewModel: RewardViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickGoToRecord: (Boolean, Boolean) -> Unit,
    onClickReview: () -> Unit,
    onClickGoToDecoration: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val onClickNext = { viewModel.setEvent(RewardEvent.GoToNextStep) }
    BBSScaffold(
        background = {
            if (state.step is Step.Main || state.step is Step.Feedback) {
                RewardBackground()
            } else {
                GradientBackground()
            }
        },
        applyPadding = false,
        topBar = { AppBar(onClickNavigation = onClickBack, applyPadding = true) }
    ) {
        if (state.showFirstBottomSheet) {
            FirstAccessBottomSheet(
                onDismissRequest = { viewModel.setEvent(RewardEvent.HideFirstBottomSheet) },
                onClickGetFirstGift = {
                    if ((state.step as? Step.Main)?.state == MainState.AVAILABLE_GIFT) {
                        onClickNext()
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .padding(it)
                .padding(top = 16.dp)
        ) {
            AnimatedVisibility(
                visible = state.step is Step.Main,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(1000)
                )
            ) {
                (state.step as? Step.Main)?.let {
                    MainContent(
                        state = it.state,
                        dayStreakCount = state.dayStreakCount,
                        onClickGetGift = onClickNext,
                        onClickGoToRecord = {
                            onClickGoToRecord(state.hasTodayRecord, state.hasYesterdayRecord)
                        },
                        onClickGoToHome = onClickBack,
                        onClickReview = onClickReview
                    )
                }
            }
            AnimatedVisibility(
                visible = state.step is Step.Feedback,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(1000)
                )
            ) {
                (state.step as? Step.Feedback)?.let {
                    FeedbackContent(
                        feedback = it.feedback,
                        onClickNext = onClickNext
                    )
                }
            }
            AnimatedVisibility(
                visible = state.step is Step.GiftOpen,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(1000)
                )
            ) {
                (state.step as? Step.GiftOpen)?.let {
                    GiftOpenContent(
                        giftCount = it.giftCount,
                        onClickOpen = onClickNext
                    )
                }
            }
            AnimatedVisibility(
                visible = state.step is Step.GiftConfirm,
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(1000)
                )
            ) {
                (state.step as? Step.GiftConfirm)?.let {
                    GiftConfirmContent(
                        giftList = it.giftList,
                        onClickGoToDecoration = {
                            onClickGoToDecoration(it.giftList.last().category.name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    state: MainState,
    dayStreakCount: Int,
    onClickGetGift: () -> Unit,
    onClickGoToRecord: () -> Unit,
    onClickGoToHome: () -> Unit,
    onClickReview: () -> Unit
) {
    val (title, description) = when (state) {
        MainState.NO_RECORD -> {
            Pair(
                stringResource(R.string.reward_main_title_no_record),
                stringResource(R.string.reward_main_description_no_record)
            )
        }

        MainState.NO_AVAILABLE_GIFT -> {
            Pair(
                stringResource(R.string.reward_main_title_no_gift),
                stringResource(R.string.reward_main_description_default)
            )
        }

        MainState.AVAILABLE_GIFT -> {
            Pair(
                stringResource(R.string.reward_main_title_default, dayStreakCount),
                stringResource(R.string.reward_main_description_default)
            )
        }

        MainState.DONE -> {
            Pair(
                stringResource(R.string.reward_main_title_done),
                stringResource(R.string.reward_main_description_done)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
    ) {
        Text(
            text = title,
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Text(
            text = description,
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.DeepBlue90
        )
        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(
                if (state == MainState.DONE) {
                    com.gowoon.designsystem.R.drawable.reward_tobby_done
                } else {
                    com.gowoon.designsystem.R.drawable.reward_tobby_default
                }
            ),
            contentDescription = null
        )
        if (state == MainState.DONE) {
            PositiveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                label = stringResource(R.string.reward_main_go_to_review_button_title),
                onClick = onClickReview
            )
            Spacer(Modifier.height(10.dp))
            NegativeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = stringResource(R.string.reward_main_go_to_home_button_title),
                onClick = onClickGoToHome
            )
        } else {
            if (state == MainState.NO_AVAILABLE_GIFT) {
                MessageBox(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    message = stringResource(R.string.reward_main_no_gift_message)
                )
            }
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                type = RoundedButtonRadius.Row,
                label = if (state == MainState.NO_RECORD) {
                    stringResource(R.string.reward_main_go_to_record_button_title)
                } else {
                    stringResource(R.string.reward_main_get_gift_button_title)
                },
                enable = state != MainState.NO_AVAILABLE_GIFT,
                onClick = if (state == MainState.NO_RECORD) {
                    onClickGoToRecord
                } else {
                    onClickGetGift
                }
            )
        }
    }
}

@Composable
private fun FeedbackContent(
    modifier: Modifier = Modifier,
    feedback: Feedback,
    onClickNext: () -> Unit
) {
    val visibleStates = remember { List(3) { mutableStateOf(false) } }

    LaunchedEffect(Unit) {
        visibleStates.forEachIndexed { index, state ->
            delay(300L * index)
            state.value = true
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
    ) {
        AnimatedVisibility(
            visible = visibleStates[0].value,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Column {
                Text(
                    text = stringResource(R.string.reward_feedback_title_prefix, feedback.nickname),
                    style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
                    color = DonmaniTheme.colors.DeepBlue99
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(if (feedback.isToday) R.string.reward_feedback_today else R.string.reward_feedback_lately),
                        style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
                        color = DonmaniTheme.colors.DeepBlue99
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        Modifier
                            .background(
                                color = DonmaniTheme.colors.DeepBlue99.copy(0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = feedback.category?.getTitle()
                                ?: stringResource(getNoConsumptionTitle()),
                            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
                            color = DonmaniTheme.colors.DeepBlue99
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (feedback.category == null) stringResource(R.string.reward_feedback_title_suffix_for_no_consumption) else stringResource(
                            R.string.reward_feedback_title_suffix
                        ),
                        style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
                        color = DonmaniTheme.colors.DeepBlue99
                    )
                }
                Spacer(Modifier.height(70.dp))
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            visible = visibleStates[1].value,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Box(Modifier.fillMaxSize()) {
                FeedbackCard(modifier = Modifier.align(Alignment.TopCenter), feedback = feedback)
            }
        }
        AnimatedVisibility(
            visible = visibleStates[2].value,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                type = RoundedButtonRadius.Row,
                label = stringResource(R.string.reward_feedback_button_title),
                onClick = onClickNext
            )
        }
    }
}

@Composable
private fun GiftOpenContent(
    modifier: Modifier = Modifier,
    giftCount: Int,
    onClickOpen: () -> Unit
) {
    var exit by remember { mutableStateOf(false) }
    val giftAnimatedOffset by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val exitAnimatedOffset by animateFloatAsState(
        targetValue = if (!exit) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    LaunchedEffect(exitAnimatedOffset) {
        if (exitAnimatedOffset == 0f) onClickOpen()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
    ) {
        Text(
            text = stringResource(R.string.reward_open_title),
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99.copy(alpha = exitAnimatedOffset)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                modifier = Modifier
                    .offset(y = giftAnimatedOffset.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = exitAnimatedOffset
                        scaleY = exitAnimatedOffset
                    }
                    .alpha(exitAnimatedOffset),
                painter = painterResource(com.gowoon.designsystem.R.drawable.gift_box),
                contentDescription = null
            )

        }
        if (giftCount > 1) {
            GiftOpenBanner(
                modifier = Modifier.alpha(exitAnimatedOffset),
                giftCount = giftCount
            )
        }
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .alpha(exitAnimatedOffset),
            type = RoundedButtonRadius.Row,
            label = stringResource(R.string.reward_open_button_title),
            onClick = { exit = true }
        )
    }
}

@Composable
private fun GiftOpenBanner(
    modifier: Modifier = Modifier,
    giftCount: Int
) {
    Row(modifier = modifier.padding(16.dp)) {
        Icon(
            imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.notice),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(
                    R.string.reward_open_banner_title,
                    giftCount
                ),
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                color = DonmaniTheme.colors.Gray99
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.reward_open_banner_description),
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.Gray99
            )
        }
    }
}

@Composable
private fun GiftConfirmContent(
    modifier: Modifier = Modifier,
    giftList: List<Gift>,
    onClickGoToDecoration: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))
    val pagerState = rememberPagerState { giftList.size }
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState) {
                GiftItem(giftList[it])
            }
            if (giftList.size > 1) {
                Row(
                    Modifier
                        .wrapContentSize()
                        .padding(top = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val alpha = if (pagerState.currentPage == iteration) 1f else 0.1f
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(DonmaniTheme.colors.Common0.copy(alpha = alpha))
                                .size(6.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = DonmaniTheme.dimens.Margin20),
                type = RoundedButtonRadius.Row,
                label = stringResource(R.string.reward_open_confirm_button_title),
                onClick = onClickGoToDecoration
            )
        }
        LottieAnimation(composition)
    }
}

@Composable
private fun GiftItem(
    gift: Gift
) {
    val thumbnailModifier = Modifier
        .fillMaxSize()
        .then(
            when (gift.category) {
                GiftCategory.BACKGROUND -> Modifier
                else -> Modifier.padding(45.dp)
            }
        )
    Column(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = DonmaniTheme.dimens.Margin20),
            text = stringResource(R.string.reward_open_confirm_title, gift.name),
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99
        )
        Spacer(Modifier.height(160.dp))
        Box(
            Modifier
                .size(200.dp)
                .background(
                    color = DonmaniTheme.colors.DeepBlue70,
                    shape = RoundedCornerShape(60.dp)
                )
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                modifier = thumbnailModifier.align(Alignment.Center),
                model = gift.thumbnailImageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}