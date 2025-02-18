package com.gowoon.record

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
import com.gowoon.common.di.FeatureJson
import com.gowoon.model.common.EntryDay
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionRecord
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.NoConsumption
import com.gowoon.model.record.Record
import com.gowoon.record.component.ConsumptionCard
import com.gowoon.record.component.EmptyCard
import com.gowoon.record.component.MessageBox
import com.gowoon.record.component.NoConsumptionCard
import com.gowoon.record.component.RecordCard
import com.gowoon.record.component.TodayYesterdayToggle
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.AppBar
import com.gowoon.ui.component.CheckBoxWithTitle
import com.gowoon.ui.component.RoundedButton
import com.gowoon.ui.component.RoundedButtonRadius
import com.gowoon.ui.component.Title
import com.gowoon.ui.component.Tooltip
import com.gowoon.ui.component.TooltipCaretAlignment
import com.gowoon.ui.component.TooltipDirection
import com.gowoon.ui.util.rememberHiltJson
import kotlinx.serialization.json.Json

@Composable
internal fun RecordMainScreen(
    viewModel: RecordMainViewModel = hiltViewModel(),
    @FeatureJson json: Json = rememberHiltJson(),
    resultFromInput: String? = null,
    onClickBack: () -> Unit,
    onClickAdd: (ConsumptionType) -> Unit,
    onClickEdit: (Consumption) -> Unit
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

    if (state.showConfirm) {
        state.records[state.selectedDay.name]?.let { record ->
            RecordConfirmScreen(
                modifier = Modifier.zIndex(1f),
                record = record
            ) {
                if (it) {
                    // TODO api call
                } else {
                    viewModel.setEvent(RecordMainEvent.ShowConfirm(false))
                }
            }
        }
    } else {
        TransparentScaffold(
            topBar = {
                AppBar(
                    onClickNavigation = onClickBack,
                    actionButton = {
                        TodayYesterdayToggle(
                            options = EntryDay.entries.filter { state.records.containsKey(it.name) }
                                .ifEmpty { EntryDay.entries },
                            selectedState = state.selectedDay
                        ) { selected -> viewModel.setEvent(RecordMainEvent.OnClickDayToggle(selected)) }
                    }
                )
            }
        ) { padding ->
            val scrollState = rememberScrollState()
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
                            R.string.record_main_title,
                            state.selectedDay.title
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
                                viewModel.setEvent(
                                    RecordMainEvent.OnClickNoConsumptionCheckBox(
                                        checked
                                    )
                                )
                            },
                            onClickEmptyBox = onClickAdd,
                            onClickTooltip = {
                                viewModel.setEvent(RecordMainEvent.OnClickNoConsumptionTooltip)
                            },
                            onClickEdit = onClickEdit
                        )
                    }
                }
                RecordMainFooter(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f),
                    enable = finishToRecord
                ) {
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
                        record = record,
                        onClickEdit = onClickEdit
                    )
                } else {
                    if (record.goodRecord == null) {
                        EmptyCard(type = ConsumptionType.GOOD) { onClickEmptyBox(ConsumptionType.GOOD) }
                    } else {
                        record.goodRecord?.let {
                            ConsumptionCard(
                                consumption = it,
                                onClickEdit = onClickEdit
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    if (record.badRecord == null) {
                        EmptyCard(type = ConsumptionType.BAD) { onClickEmptyBox(ConsumptionType.BAD) }
                    } else {
                        record.badRecord?.let {
                            ConsumptionCard(
                                consumption = it,
                                onClickEdit = onClickEdit
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
    modifier: Modifier = Modifier,
    enable: Boolean,
    onClick: () -> Unit
) {
    // TODO message 문구 로직 > timer
    val gradientBgColor = Color(0xFF091647)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = arrayListOf(
                        gradientBgColor.copy(0f),
                        gradientBgColor
                    )
                )
            )
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageBox(message = stringResource(R.string.record_bottom_message_default))
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