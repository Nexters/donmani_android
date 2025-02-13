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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.common.EntryDay
import com.gowoon.model.record.ConsumptionRecord
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.NoConsumption
import com.gowoon.model.record.Record
import com.gowoon.record.component.EmptyCard
import com.gowoon.record.component.MessageBox
import com.gowoon.record.component.NoConsumptionCard
import com.gowoon.record.component.RecordMainAppBar
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.CheckBoxWithTitle
import com.gowoon.ui.component.RoundedButton
import com.gowoon.ui.component.RoundedButtonRadius
import com.gowoon.ui.component.Title
import com.gowoon.ui.component.Tooltip
import com.gowoon.ui.component.TooltipCaretAlignment
import com.gowoon.ui.component.TooltipDirection

@Composable
internal fun RecordMainScreen(
    viewModel: RecordMainViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransparentScaffold(
        topBar = {
            RecordMainAppBar(onClickBack = onClickBack) { selected ->
                viewModel.setEvent(RecordMainEvent.OnClickDayToggle(selected))
            }
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
                Title(text = stringResource(R.string.record_main_title, state.selectedDay.title))
                Spacer(Modifier.height(60.dp))
                when (state.selectedDay) {
                    EntryDay.Today -> state.todayRecord
                    EntryDay.Yesterday -> state.yesterdayRecord
                }?.let {
                    RecordMainContent(
                        modifier = Modifier.weight(1f),
                        record = it,
                        showTooltip = state.showTooltip,
                        onClickCheckBox = { checked ->
                            viewModel.setEvent(RecordMainEvent.OnClickNoConsumptionCheckBox(checked))
                        },
                        onClickEmptyBox = {
                            // TODO navigate to record input
                        },
                        onClickTooltip = {
                            viewModel.setEvent(RecordMainEvent.OnClickNoConsumptionTooltip)
                        }
                    )
                }
            }
            RecordMainFooter(
                Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f)
            )
        }
    }
}

@Composable
private fun RecordMainContent(
    modifier: Modifier = Modifier,
    record: Record,
    showTooltip: Boolean,
    onClickCheckBox: (Boolean) -> Unit,
    onClickEmptyBox: (ConsumptionType) -> Unit,
    onClickTooltip: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (record) {
            is NoConsumption -> {
                NoConsumptionCard()
            }

            is ConsumptionRecord -> {
                // TODO content 유무에 따라 분기
                record.goodRecord.apply {
                    EmptyCard(type = type) { onClickEmptyBox(ConsumptionType.GOOD) }
                }
                Spacer(Modifier.height(20.dp))
                record.badRecord.apply {
                    EmptyCard(type = type) { onClickEmptyBox(ConsumptionType.BAD) }
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
private fun RecordMainFooter(modifier: Modifier = Modifier) {
    // TODO message 문구 로직 > timer
    // TODO button enable 로직
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
            enable = true
        ) {
            // TOOD onClick
        }
    }
}

@Preview
@Composable
private fun RecordMainPreview() {
    DonmaniTheme {
        GradientBackground {
            RecordMainScreen {}
        }
    }
}