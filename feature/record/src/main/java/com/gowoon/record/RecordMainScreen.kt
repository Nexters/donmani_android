package com.gowoon.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gowoon.common.di.FeatureJson
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.CheckBoxWithTitle
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.component.Title
import com.gowoon.designsystem.component.Tooltip
import com.gowoon.designsystem.component.TooltipCaretAlignment
import com.gowoon.designsystem.component.TooltipDirection
import com.gowoon.domain.util.format
import com.gowoon.model.common.EntryDay
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.Record
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.model.record.Record.NoConsumption
import com.gowoon.record.component.ExitWarningBottomSheet
import com.gowoon.record.component.NoConsumptionBottomSheet
import com.gowoon.record.component.TodayYesterdayToggle
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.BBSRuleBottomSheet
import com.gowoon.ui.component.ConsumptionCard
import com.gowoon.ui.component.EmptyCard
import com.gowoon.ui.component.MessageBox
import com.gowoon.ui.component.NoConsumptionCard
import com.gowoon.ui.component.RecordCard
import com.gowoon.ui.util.rememberHiltJson
import kotlinx.serialization.json.Json

@Composable
internal fun RecordMainScreen(
    viewModel: RecordMainViewModel = hiltViewModel(),
    @FeatureJson json: Json = rememberHiltJson(),
    navController: NavController,
    resultFromInput: String? = null,
    navigateToHome: () -> Unit,
    onClickAdd: (ConsumptionType) -> Unit,
    onClickEdit: (Consumption) -> Unit,
    onSave: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val finishToRecord by remember {
        derivedStateOf {
            state.records[state.selectedDay.name]?.let { record: Record ->
                when (record) {
                    is NoConsumption -> true
                    is ConsumptionRecord -> {
                        record.goodRecord != null && record.badRecord != null
                    }
                }
            } ?: false
        }
    }

    val onClickBackEvent = {
        if (viewModel.startToRecord()) {
            viewModel.setEvent(RecordMainEvent.ShowBottomSheet(RecordMainDialogType.EXIT_WARNING))
        } else {
            navigateToHome()
        }
    }

    LaunchedEffect(resultFromInput) {
        resultFromInput?.let {
            viewModel.setEvent(
                RecordMainEvent.OnChangedConsumption(
                    json.decodeFromString<Consumption>(
                        it
                    )
                )
            )
        }
    }

    BackHandler {
        onClickBackEvent()
    }

    if (state.showConfirm) {
        state.records[state.selectedDay.name]?.let { record ->
            RecordConfirmScreen(
                modifier = Modifier.zIndex(1f), record = record
            ) {
                if (it) {
                    viewModel.setEvent(RecordMainEvent.OnSaveRecord(record) { succeed ->
                        if (succeed) {
                            onSave(json.encodeToString(record))
                        }
                    })
                } else {
                    viewModel.setEvent(RecordMainEvent.ShowConfirm(false))
                }
            }
        }
    } else {
        TransparentScaffold(topBar = {
            AppBar(
                onClickNavigation = onClickBackEvent,
                actionButton = {
                    TodayYesterdayToggle(options = EntryDay.entries.filter {
                        state.records.containsKey(
                            it.name
                        )
                    }.ifEmpty { EntryDay.entries }, selectedState = state.selectedDay
                    ) { selected -> viewModel.setEvent(RecordMainEvent.OnClickDayToggle(selected)) }
                })
        }) { padding ->
            val scrollState = rememberScrollState()

            if (state.showRuleBottomSheet) {
                BBSRuleBottomSheet { viewModel.setEvent(RecordMainEvent.HideBBSRuleSheet) }
            }
            state.showBottomSheet?.let {
                when (it) {
                    RecordMainDialogType.NO_CONSUMPTION -> {
                        NoConsumptionBottomSheet(onClick = { isPositive ->
                            if (isPositive) {
                                viewModel.setEvent(
                                    RecordMainEvent.OnClickNoConsumptionCheckBox(true)
                                )
                            }
                        }) {
                            viewModel.setEvent(RecordMainEvent.ShowBottomSheet(null))
                        }
                    }

                    RecordMainDialogType.EXIT_WARNING -> {
                        ExitWarningBottomSheet(onClick = { isPositive ->
                            if (isPositive) navigateToHome()
                        }) {
                            viewModel.setEvent(RecordMainEvent.ShowBottomSheet(null))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))
                    Title(
                        text = stringResource(
                            R.string.record_main_title, if (viewModel.isNoRecordForBothDays()) {
                                stringResource(R.string.record_main_title_prefix_day)
                            } else {
                                state.selectedDay.title
                            }
                        )
                    )
                    Spacer(Modifier.height(60.dp))
                    state.records[state.selectedDay.name]?.let {
                        RecordMainContent(
                            modifier = Modifier.padding(bottom = 140.dp),
                            record = it,
                            finishToRecord = finishToRecord,
                            showTooltip = state.showTooltip,
                            onClickCheckBox = { checked ->
                                if (checked) {
                                    viewModel.setEvent(
                                        RecordMainEvent.ShowBottomSheet(
                                            RecordMainDialogType.NO_CONSUMPTION
                                        )
                                    )
                                } else {
                                    viewModel.setEvent(
                                        RecordMainEvent.OnClickNoConsumptionCheckBox(false)
                                    )
                                }
                            },
                            onClickEmptyBox = onClickAdd,
                            onClickTooltip = {
                                viewModel.setEvent(RecordMainEvent.OnClickNoConsumptionTooltip)
                            },
                            onClickEdit = onClickEdit
                        )
                    }
                }
                RecordMainFooter(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f),
                    enable = finishToRecord,
                    remainTime = if (state.selectedDay == EntryDay.Yesterday) {
                        state.remainTime?.let {
                            it.format() + stringResource(R.string.record_bottom_count_down_suffix)
                        }
                    } else {
                        null
                    }) {
                    viewModel.setEvent(RecordMainEvent.ShowConfirm(true))
                }
            }
        }
    }
}

@Composable
private fun RecordMainContent(
    modifier: Modifier = Modifier,
    record: Record,
    finishToRecord: Boolean,
    showTooltip: Boolean,
    onClickCheckBox: (Boolean) -> Unit,
    onClickEmptyBox: (ConsumptionType) -> Unit,
    onClickTooltip: () -> Unit,
    onClickEdit: (Consumption) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (record) {
            is NoConsumption -> {
                NoConsumptionCard()
            }

            is ConsumptionRecord -> {
                if (finishToRecord) {
                    RecordCard(
                        record = record, onClickEdit = onClickEdit
                    )
                } else {
                    if (record.goodRecord == null) {
                        EmptyCard(type = ConsumptionType.GOOD) { onClickEmptyBox(ConsumptionType.GOOD) }
                    } else {
                        record.goodRecord?.let {
                            ConsumptionCard(
                                consumption = it, onClickEdit = onClickEdit
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    if (record.badRecord == null) {
                        EmptyCard(type = ConsumptionType.BAD) { onClickEmptyBox(ConsumptionType.BAD) }
                    } else {
                        record.badRecord?.let {
                            ConsumptionCard(
                                consumption = it, onClickEdit = onClickEdit
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        CheckBoxWithTitle(
            title = stringResource(R.string.no_consumption_message),
            checked = record is NoConsumption,
            onClick = onClickCheckBox
        )
        if (showTooltip) {
            Tooltip(
                modifier = Modifier.offset(y = (-8).dp),
                direction = TooltipDirection.BottomOf,
                caretAlignment = TooltipCaretAlignment.Start,
                message = stringResource(R.string.no_consumption_tooltip_message),
                onClick = onClickTooltip
            )
        }
    }
}

@Composable
private fun RecordMainFooter(
    modifier: Modifier = Modifier, enable: Boolean, remainTime: String?, onClick: () -> Unit
) {
    val gradientBgColor = Color(0xFF091647)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = arrayListOf(
                        gradientBgColor.copy(0f), gradientBgColor
                    )
                )
            )
            .padding(top = 80.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageBox(message = remainTime ?: stringResource(R.string.record_bottom_message_default))
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            type = RoundedButtonRadius.Row,
            label = stringResource(R.string.btn_record_save),
            enable = enable,
            onClick = onClick
        )
    }
}