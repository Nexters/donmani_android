package com.gowoon.recordlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.component.Tooltip
import com.gowoon.designsystem.component.TooltipCaretAlignment
import com.gowoon.designsystem.component.TooltipDirection
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.designsystem.util.pxToDp
import com.gowoon.domain.util.toKorean
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.Record
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.ConsumptionCard
import com.gowoon.ui.component.NoConsumptionCard
import com.gowoon.ui.component.RecordCard

@Composable
internal fun RecordListScreen(
    viewModel: RecordListViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickAdd: () -> Unit,
    onClickSummary: (Int, Int) -> Unit,
    onClickActionButton: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var tooltipOffset by remember { mutableStateOf(Offset.Zero) }
    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }

    val showActionButton = true // TODO 전체 기록 없을 때로 조건 추가
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(
                    R.string.record_list_appbar_title,
                    state.year.toString().takeLast(2),
                    state.month
                ),
                onClickNavigation = onClickBack,
                actionButton = {
                    if (showActionButton) {
                        Icon(
                            modifier = Modifier
                                .onGloballyPositioned {
                                    tooltipOffset = it.boundsInRoot().bottomCenter
                                }
                                .noRippleClickable { onClickActionButton() },
                            imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.star_bottle_icon),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        if (state.records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                SummaryCardHeader(
                    month = state.month,
                    goodCount = 0,
                    badCount = 0
                )
                EmptyContent(
                    modifier = Modifier.align(Alignment.Center),
                    onClickAdd = onClickAdd
                )
            }
        } else {
            RecordListContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                records = state.records,
                month = state.month
            ) {
                onClickSummary(state.year, state.month)
            }
        }
    }
    if (state.showTooltip && showActionButton) {
        Tooltip(
            modifier = Modifier
                .onSizeChanged { tooltipSize = it }
                .offset(
                    x = (tooltipOffset.x - tooltipSize.width).pxToDp() + 12.dp + (14 / 2).dp, // distance to caret 12. caret width 14.
                    y = tooltipOffset.y.pxToDp()
                ),
            direction = TooltipDirection.BottomOf,
            caretAlignment = TooltipCaretAlignment.End,
            backgroundColor = DonmaniTheme.colors.DeepBlue80,
            contentColor = DonmaniTheme.colors.Common0,
            message = stringResource(R.string.action_btn_tooltip_message)
        ) { viewModel.setEvent(RecordListEvent.HideTooltip) }
    }
}

@Composable
private fun RecordListContent(
    modifier: Modifier = Modifier,
    records: List<Record>,
    month: Int,
    onClickHeader: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
    ) {
        item {
            SummaryCardHeader(
                month = month,
                goodCount = records.count {
                    (it is ConsumptionRecord) && it.goodRecord != null
                },
                badCount = records.count {
                    (it is ConsumptionRecord) && it.badRecord != null
                },
                onClick = onClickHeader
            )
        }
        itemsIndexed(records) { index, record ->
            Spacer(Modifier.height(if (index == 0) 40.dp else 60.dp))
            RecordListItem(record)
        }
    }
}

@Composable
private fun RecordListItem(record: Record) {
    Column {
        record.date?.let { date ->
            Text(
                text = "${date.month.value}월 ${date.dayOfMonth}일 ${date.dayOfWeek.toKorean()}",
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Body2
            )
        }
        Spacer(Modifier.height(16.dp))
        when (record) {
            is Record.NoConsumption -> {
                NoConsumptionCard()
            }

            is ConsumptionRecord -> {
                if (record.goodRecord != null && record.badRecord != null) {
                    RecordCard(
                        record = record,
                        showEdit = false
                    ) { }
                } else {
                    record.goodRecord?.let {
                        ConsumptionCard(consumption = it, showEdit = false) { }
                    }
                    record.badRecord?.let {
                        ConsumptionCard(consumption = it, showEdit = false) { }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier,
    onClickAdd: () -> Unit
) {
    Box(modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_record_title),
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(16.dp))
            RoundedButton(
                type = RoundedButtonRadius.Row,
                label = stringResource(R.string.btn_add_record),
                onClick = onClickAdd
            )
        }
    }
}

@Composable
private fun SummaryCardHeader(
    modifier: Modifier = Modifier,
    month: Int,
    goodCount: Int,
    badCount: Int,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = DonmaniTheme.colors.DeepBlue60, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .noRippleClickable { if (goodCount + badCount > 0) onClick?.invoke() },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.record_list_summary_header_title, month),
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                color = DonmaniTheme.colors.Gray99
            )
            onClick?.let {
                if (goodCount + badCount > 0) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.arrow_right),
                        tint = DonmaniTheme.colors.Gray99,
                        contentDescription = null
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    color = DonmaniTheme.colors.DeepBlue80,
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            if (goodCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            color = DonmaniTheme.colors.PurpleBlue70,
                            shape = RoundedCornerShape(
                                topStart = 6.dp,
                                bottomStart = 6.dp,
                                topEnd = if (badCount > 0) 0.dp else 6.dp,
                                bottomEnd = if (badCount > 0) 0.dp else 6.dp
                            )
                        )
                        .weight(goodCount.toFloat())
                )
            }
            if (badCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            color = DonmaniTheme.colors.PurpleBlue99,
                            shape = RoundedCornerShape(
                                topEnd = 6.dp,
                                bottomEnd = 6.dp,
                                topStart = if (goodCount > 0) 0.dp else 6.dp,
                                bottomStart = if (goodCount > 0) 0.dp else 6.dp,
                            )
                        )
                        .weight(badCount.toFloat())
                )
            }
        }
        Column {
            SummaryTypeItem(
                DonmaniTheme.colors.PurpleBlue70,
                ConsumptionType.GOOD.title,
                goodCount
            )
            Spacer(Modifier.height(4.dp))
            SummaryTypeItem(
                DonmaniTheme.colors.PurpleBlue99,
                ConsumptionType.BAD.title,
                badCount
            )
        }
    }
}

@Composable
private fun SummaryTypeItem(
    backgroundColor: Color,
    typeString: String,
    count: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(6.dp)
                .background(color = backgroundColor, shape = CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$typeString ${count}개",
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.Gray99
        )
    }
}

@Preview
@Composable
fun Preview() {
    SummaryCardHeader(month = 1, goodCount = 0, badCount = 0)
}