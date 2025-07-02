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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.designsystem.component.NegativeButton
import com.gowoon.designsystem.component.PositiveButton
import com.gowoon.designsystem.component.Title
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.Record
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.model.record.Record.NoConsumption
import com.gowoon.model.record.name
import com.gowoon.ui.component.ConsumptionCard
import com.gowoon.ui.component.MessageBox
import com.gowoon.ui.component.NoConsumptionCard
import com.gowoon.ui.component.RecordCard

@Composable
internal fun RecordConfirmScreen(
    modifier: Modifier = Modifier,
    record: Record,
    onClickEdit: (Consumption, String) -> Unit,
    screenType: String,
    onClick: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        FirebaseAnalyticsUtil.sendEvent(
            trigger = FirebaseAnalyticsUtil.EventTrigger.VIEW,
            eventName = "confirm",
            Pair("referrer", "true")
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = DonmaniTheme.dimens.Margin20)
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Title(text = stringResource(R.string.record_confirm_title))
        Spacer(Modifier.height(60.dp))
        RecordConfirmContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            record = record,
            onClickEdit = onClickEdit,
            screenType = screenType
        )
        RecordConfirmFooter(
            modifier = Modifier.fillMaxWidth(),
            incompleteType = (record as? ConsumptionRecord)?.let {
                if (it.goodRecord == null) {
                    ConsumptionType.GOOD
                } else if (it.badRecord == null) {
                    ConsumptionType.BAD
                } else {
                    null
                }
            },
            onClick = { isPositive ->
                val midfix = if (isPositive) "submit" else "back"
                FirebaseAnalyticsUtil.sendEvent(
                    trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                    eventName = "confirm_${midfix}_button",
                    params = mutableListOf(
                        Pair("screentype", screenType)
                    ).apply {
                        when (record) {
                            is NoConsumption -> {
                                add(Pair("empty", "null"))
                            }

                            is ConsumptionRecord -> {
                                record.goodRecord?.let {
                                    add(
                                        Pair(
                                            "good",
                                            "category: ${it.category.name(it.type)}, record: ${it.description}"
                                        )
                                    )
                                }
                                record.badRecord?.let {
                                    add(
                                        Pair(
                                            "bad",
                                            "category: ${it.category.name(it.type)}, record: ${it.description}"
                                        )
                                    )
                                }
                            }
                        }
                    }
                )
                onClick(isPositive)
            }
        )
    }
}

@Composable
private fun RecordConfirmContent(
    modifier: Modifier = Modifier,
    record: Record,
    onClickEdit: (Consumption, String) -> Unit,
    screenType: String
) {
    Box(modifier = modifier) {
        when (record) {
            is NoConsumption -> {
                NoConsumptionCard()
            }

            is ConsumptionRecord -> {
                if (record.goodRecord != null && record.badRecord != null) {
                    RecordCard(
                        record = record,
                        showEdit = true,
                        onClickEdit = onClickEdit,
                        screenType = screenType
                    )
                } else {
                    record.goodRecord?.let {
                        ConsumptionCard(
                            consumption = it,
                            showEdit = true,
                            onClickEdit = onClickEdit,
                            screenType = screenType
                        )
                    }
                    record.badRecord?.let {
                        ConsumptionCard(
                            consumption = it,
                            showEdit = true,
                            onClickEdit = onClickEdit,
                            screenType = screenType
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordConfirmFooter(
    modifier: Modifier = Modifier,
    incompleteType: ConsumptionType?,
    onClick: (Boolean) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        incompleteType?.let {
            IncompleteRecordBanner(
                modifier = Modifier.fillMaxWidth(),
                incompleteType = it
            )
        } ?: run {
            MessageBox(message = stringResource(R.string.record_bottom_message_default))
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

@Composable
private fun IncompleteRecordBanner(
    modifier: Modifier = Modifier,
    incompleteType: ConsumptionType
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
                    R.string.incomplete_record_banner_title,
                    incompleteType.title
                ),
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                color = DonmaniTheme.colors.Gray99
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.incomplete_record_banner_description),
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.Gray99
            )
        }
    }
}