package com.gowoon.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.common.di.FeatureJson
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.designsystem.component.HomeCircleButton
import com.gowoon.designsystem.component.Title
import com.gowoon.designsystem.component.Tooltip
import com.gowoon.designsystem.component.TooltipCaretAlignment
import com.gowoon.designsystem.component.TooltipDirection
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.pxToDp
import com.gowoon.home.component.HomeAppBar
import com.gowoon.home.component.StarBottleOpenBottomSheet
import com.gowoon.model.record.Record
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.MessageBox
import com.gowoon.ui.component.StarBottle
import com.gowoon.ui.util.rememberHiltJson
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    @FeatureJson json: Json = rememberHiltJson(),
    resultFromRecord: String?,
    onClickSetting: () -> Unit,
    onClickAdd: (Boolean, Boolean, String) -> Unit,
    onClickBottle: (List<Record>, Int, Int) -> Unit,
    onClickGoToStarBottle: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var tooltipOffset by remember { mutableStateOf(Offset.Zero) }
    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }

    val referrer by viewModel.referrer.collectAsStateWithLifecycle()
    val isFromFcm by viewModel.isFromFcm.collectAsStateWithLifecycle()
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
    LaunchedEffect(resultFromRecord) {
        var recordAdded = false
        var record: Record? = null
        resultFromRecord?.let {
            record = json.decodeFromString<Record>(it)
            if (record != state.newRecord) {
                recordAdded = true
            }
        }
        viewModel.setEvent(HomeEvent.OnAddRecord(record, recordAdded))
    }
    TransparentScaffold(
        topBar = {
            HomeAppBar(
                onClickSetting = {
                    onClickSetting()
                    FirebaseAnalyticsUtil.sendEvent(
                        trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                        eventName = "main_setting_button"
                    )
                }
            )
        }
    ) { padding ->
        if (state.showBottomSheet && state.records.isEmpty()) {
            StarBottleOpenBottomSheet(
                onDismissRequest = { viewModel.setEvent(HomeEvent.HideBottomSheet) },
                onClickGoToStarBottle = onClickGoToStarBottle
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Title(text = state.nickname)
            HomeContent(
                records = state.records,
                newRecord = state.newRecord,
                recordAdded = state.recordAdded,
                onClickBottle = {
                    onClickBottle(state.records, state.year, state.month)
                    FirebaseAnalyticsUtil.sendEvent(
                        trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                        eventName = "main_record_archive_button"
                    )
                },
            )
            HomeFooter(
                hasToday = state.hasToday,
                hasYesterday = state.hasYesterday,
                changedCircleButtonPosition = { tooltipOffset = it },
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
                    top = tooltipOffset.y.pxToDp(),
                    start = tooltipOffset.x.pxToDp()
                )
                .onSizeChanged { tooltipSize = it }
                .offset(
                    x = -(tooltipSize / 2).width.pxToDp(),
                    y = (-40).dp // calculated with button size and tooltip size
                ),
            direction = TooltipDirection.TopOf,
            caretAlignment = TooltipCaretAlignment.Center,
            message = stringResource(R.string.tooltip_message_for_yesterday)
        ) {
            viewModel.setEvent(HomeEvent.HideTooltip)
        }
    }

}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    records: List<Record>,
    newRecord: Record?,
    recordAdded: Boolean,
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
        modifier = modifier.graphicsLayer(translationY = if (recordAdded) offsetY else 0f),
        records = records,
        newRecord = newRecord,
        recordAdded = recordAdded,
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
            MessageBox(
                modifier = Modifier.align(Alignment.Center),
                message = stringResource(R.string.footer_message_when_has_today_and_yesterday)
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