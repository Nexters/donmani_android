package com.gowoon.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.model.record.Record.NoConsumption
import com.gowoon.model.record.Record
import com.gowoon.record.component.NoConsumptionCard
import com.gowoon.record.component.RecordCard
import com.gowoon.ui.component.NegativeButton
import com.gowoon.ui.component.PositiveButton
import com.gowoon.ui.component.Title

@Composable
internal fun RecordConfirmScreen(
    modifier: Modifier = Modifier,
    record: Record,
    onClick: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
            .padding(top = 120.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Title(text = stringResource(R.string.record_confirm_title))
        Spacer(Modifier.height(60.dp))
        Box(Modifier.fillMaxWidth().weight(1f)){
            when(record){
                is NoConsumption -> {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NegativeButton(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.btn_record_confirm_back)
            ) {
                onClick(false)
            }
            PositiveButton(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.btn_record_confirm)
            ) {
                onClick(true)
            }
        }
    }
}

@Preview
@Composable
private fun RecordConfirmPreview() {
//    val record: Record = NoConsumption
    val record: Record = ConsumptionRecord(
        goodRecord = Consumption(ConsumptionType.GOOD, GoodCategory.Flex, "아아아아아아"),
        badRecord = Consumption(ConsumptionType.BAD, BadCategory.Greed, "우우우우우우우"),
    )
    RecordConfirmScreen(record = record){}
}