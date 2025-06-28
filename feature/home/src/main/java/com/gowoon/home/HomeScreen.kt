package com.gowoon.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.common.di.FeatureJson
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.designsystem.component.CustomSnackBarHost
import com.gowoon.designsystem.component.HomeCircleButton
import com.gowoon.designsystem.component.SnackBarType
import com.gowoon.designsystem.component.Title
import com.gowoon.designsystem.component.Tooltip
import com.gowoon.designsystem.component.TooltipCaretAlignment
import com.gowoon.designsystem.component.TooltipDirection
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.pxToDp
import com.gowoon.home.component.HomeAppBar
import com.gowoon.home.component.StarBottleOpenBottomSheet
import com.gowoon.model.record.Record
import com.gowoon.model.reward.BottleType
import com.gowoon.model.reward.getBottleType
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.DecoratedBackground
import com.gowoon.ui.Decoration
import com.gowoon.ui.component.MessageBox
import com.gowoon.ui.component.StarBottle
import com.gowoon.ui.util.rememberHiltJson
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    @FeatureJson json: Json = rememberHiltJson(),
    changedState: String?,
    dataFromRecord: String?,
    onClickSetting: () -> Unit,
    onClickStore: (Boolean, Boolean) -> Unit,
    onClickAdd: (Boolean, Boolean, String) -> Unit,
    onClickBottle: (List<Record>, Int, Int) -> Unit,
    onClickGoToStarBottle: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var addTooltipOffset by remember { mutableStateOf(Offset.Zero) }
    var addTooltipSize by remember { mutableStateOf(IntSize.Zero) }

    var rewardTooltipOffset by remember { mutableStateOf(Offset.Zero) }
    var rewardTooltipSize by remember { mutableStateOf(0) }

    var decorationOffset by remember { mutableStateOf(Rect.Zero) }

//    val player by remember {
//        derivedStateOf {
//            if (state.bgmPlayOn) ExoPlayer.Builder(context).build() else null
//        }
//    }
//    var gravityDiff by remember { mutableStateOf(0f) }

    val referrer by viewModel.referrer.collectAsStateWithLifecycle()
    val isFromFcm by viewModel.isFromFcm.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEffect.collect {
            if (it is HomeEffect.ShowToast) {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    LaunchedEffect(referrer) {
        if (!referrer.first) {
            viewModel.sendViewMainGA4Event()
        }
    }
    LaunchedEffect(isFromFcm) {
        if (!isFromFcm.first && isFromFcm.second) {
            onClickAdd(state.hasToday, state.hasYesterday, "notification")
            viewModel.updateIsFromFcmState()
        }
    }

    LaunchedEffect(changedState) {
        changedState?.let {
            if (it != state.storedState) {
                viewModel.setEvent(
                    HomeEvent.UpdateDecorationState(
                        it,
                        context.getString(R.string.result_decoration_toast_message)
                    )
                )
            }
        }
    }

    LaunchedEffect(dataFromRecord) {
        Napier.d("gowoon result from record $dataFromRecord")
        var recordAdded = false
        var record: Record? = null
        dataFromRecord?.let {
            record = json.decodeFromString<Record>(it)
            if (record != state.newRecord) {
                recordAdded = true
                viewModel.setEvent(HomeEvent.UpdateRewardTooltipState(true))
            }
        }
        viewModel.setEvent(HomeEvent.OnAddRecord(record, recordAdded))
    }

//    LaunchedEffect(state.bbsState.bgm) {
//        state.bbsState.bgm?.resourceUrl?.let {
//            player?.setMediaItem(MediaItem.fromUri(it))
//            player?.prepare()
//            player?.repeatMode = Player.REPEAT_MODE_ONE
//        }
//    }
//
//    LaunchedEffect(gravityDiff) {
//        player?.volume = if (gravityDiff < 2f) {
//            0f
//        } else {
//            gravityDiff
//        }
//    }
//
//    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
//        player?.play()
//    }
//
//    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
//        player?.pause()
//    }

    BBSScaffold(
        background = {
            DecoratedBackground(
                background = state.bbsState.background?.resourceUrl ?: "",
                effect = state.bbsState.effect?.resourceUrl ?: ""
            )
        },
        topBar = {
            HomeAppBar(
                onClickSetting = {
                    onClickSetting()
                    FirebaseAnalyticsUtil.sendEvent(
                        trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                        eventName = "main_setting_button"
                    )
                },
                onClickStore = {
                    onClickStore(state.hasToday, state.hasYesterday)
                    viewModel.setEvent(HomeEvent.UpdateRewardTooltipState(false))
                }
            )
        }
    ) { padding ->
        if (state.showBottomSheet && state.bbsState.records.isEmpty()) {
            StarBottleOpenBottomSheet(
                onDismissRequest = { viewModel.setEvent(HomeEvent.HideBottomSheet) },
                onClickGoToStarBottle = onClickGoToStarBottle
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 24.dp)
                .onGloballyPositioned { rewardTooltipOffset = it.boundsInRoot().topRight },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Title(text = state.nickname)
            HomeContent(
                bottleType = getBottleType(state.bbsState.case?.id ?: ""),
                records = state.bbsState.records,
                newRecord = state.newRecord,
                recordAdded = state.recordAdded,
                onChangePosition = {
                    decorationOffset = it
                },
//                onChangeDiff = { gravityDiff = it },
                onClickBottle = {
                    onClickBottle(state.bbsState.records, state.year, state.month)
                    FirebaseAnalyticsUtil.sendEvent(
                        trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                        eventName = "main_record_archive_button"
                    )
                },
            )
            HomeFooter(
                hasToday = state.hasToday,
                hasYesterday = state.hasYesterday,
                changedCircleButtonPosition = { addTooltipOffset = it },
                onClickAdd = {
                    onClickAdd(state.hasToday, state.hasYesterday, "main")
                    FirebaseAnalyticsUtil.sendEvent(
                        trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                        eventName = "main_record_button"
                    )
                }
            )
        }
    }

    if (state.hasToday && !state.hasYesterday && state.showTooltip) {
        Tooltip(
            modifier = Modifier
                .padding(
                    top = addTooltipOffset.y.pxToDp(),
                    start = addTooltipOffset.x.pxToDp()
                )
                .onSizeChanged { addTooltipSize = it }
                .offset(
                    x = -(addTooltipSize / 2).width.pxToDp(),
                    y = (-40).dp // calculated with button size and tooltip size
                ),
            direction = TooltipDirection.TopOf,
            caretAlignment = TooltipCaretAlignment.Center,
            message = stringResource(R.string.tooltip_message_for_yesterday)
        ) {
            viewModel.setEvent(HomeEvent.HideTooltip)
        }
    }

    if (state.showRewardTooltip) {
        Tooltip(
            modifier = Modifier
                .offset(
                    y = rewardTooltipOffset.y.pxToDp() - (16 + 24).dp, // appbar size, action button size, padding
                    x = rewardTooltipOffset.x.pxToDp() - rewardTooltipSize.pxToDp() + 19.dp - 12.dp // screen size, margin size, button size, toolbar size
                )
                .onSizeChanged { rewardTooltipSize = it.width },
            direction = TooltipDirection.BottomOf,
            caretAlignment = TooltipCaretAlignment.End,
            showCloseButton = false,
            backgroundColor = DonmaniTheme.colors.DeepBlue70,
            message = stringResource(R.string.tooltip_message_for_reward)
        ) { }
    }
    Decoration(
        targetRect = decorationOffset,
        decoration = state.bbsState.decoration,
        bottleType = getBottleType(state.bbsState.case?.id ?: "")
    )
    Box(Modifier.fillMaxSize()) {
        CustomSnackBarHost(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp),
            snackBarType = SnackBarType.Confirm,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    bottleType: BottleType,
    records: List<Record>,
    newRecord: Record?,
    recordAdded: Boolean,
    onChangePosition: (Rect) -> Unit,
//    onChangeDiff: (Float) -> Unit,
    onClickBottle: () -> Unit
) {
    var isMoved by remember { mutableStateOf(false) }
    LaunchedEffect(recordAdded) {
        if (recordAdded) {
            delay(3000)
            isMoved = true
        }
    }
    val offsetY by animateFloatAsState(
        targetValue = if (isMoved) 0f else -70f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    StarBottle(
        modifier = modifier
            .graphicsLayer(translationY = if (recordAdded) offsetY else 0f)
            .onGloballyPositioned { onChangePosition(it.boundsInRoot()) },
        bottleType = bottleType,
        records = records,
        newRecord = newRecord,
        recordAdded = recordAdded,
//        onChangeDiff = onChangeDiff,
        onClickBottle = onClickBottle
    )
}

@Composable
private fun HomeFooter(
    modifier: Modifier = Modifier,
    hasToday: Boolean,
    hasYesterday: Boolean,
    changedCircleButtonPosition: (Offset) -> Unit,
    onClickAdd: () -> Unit
) {
    Box(modifier) {
        if (hasToday && hasYesterday) {
            Image(
                painter = painterResource(com.gowoon.designsystem.R.drawable.message_box_bg),
                contentDescription = null
            )
            MessageBox(
                modifier = Modifier.align(Alignment.Center),
                message = stringResource(R.string.footer_message_when_has_today_and_yesterday),
                textColor = DonmaniTheme.colors.Common0
            )
        } else {
            HomeCircleButton(
                modifier = Modifier
                    .align(Alignment.Center)
                    .onGloballyPositioned {
                        changedCircleButtonPosition(it.boundsInRoot().topCenter)
                    },
                backgroundColor = DonmaniTheme.colors.PurpleBlue70,
                contentColor = DonmaniTheme.colors.PurpleBlue99
            ) { onClickAdd() }
        }
    }
}