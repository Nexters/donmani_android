package com.gowoon.recordlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.domain.util.toKorean
import com.gowoon.model.record.Record
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.NoConsumptionCard
import com.gowoon.ui.component.RecordCard
import com.gowoon.ui.component.Star

@Composable
internal fun RecordListScreen(
    viewModel: RecordListViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickAdd: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.record_list_appbar_title),
                onClickNavigation = onClickBack
            )
        }
    ) {
        if (state.records.isEmpty()) {
            EmptyContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                onClickAdd = onClickAdd
            )
        } else {
            RecordListContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                records = state.records
            )
        }
    }
}

@Composable
private fun RecordListContent(
    modifier: Modifier = Modifier,
    records: List<Record>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(60.dp)
    ) {
        items(records) {
            RecordListItem(it)
        }
    }
}

@Composable
private fun RecordListItem(record: Record) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Star(size = 32.dp, record = record)
            record.date?.let { date ->
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${date.month.value}월 ${date.dayOfMonth}일 ${date.dayOfWeek.toKorean()}",
                    color = DonmaniTheme.colors.Gray95,
                    style = DonmaniTheme.typography.Body2
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        when (record) {
            is Record.NoConsumption -> {
                NoConsumptionCard()
            }

            is ConsumptionRecord -> {
                RecordCard(
                    record = record,
                    showEdit = false
                ) { }
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
