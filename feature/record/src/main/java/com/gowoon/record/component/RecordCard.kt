package com.gowoon.record.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.Category
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionRecord
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.getTitle
import com.gowoon.record.R
import com.gowoon.ui.component.Card
import com.gowoon.ui.component.CircleButton
import com.gowoon.ui.component.CircleButtonSize
import com.gowoon.ui.component.RecordStar
import com.gowoon.ui.noRippleClickable
import com.gowoon.ui.util.getColor
import com.gowoon.ui.util.getNoConsumptionColor
import com.gowoon.ui.util.getNoConsumptionResId

@Composable
private fun ConsumptionContent(
    modifier: Modifier = Modifier,
    title: String,
    category: Category,
    memo: String,
    showEdit: Boolean = true,
    onClickEdit: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold)
            )
            if (showEdit) {
                Icon(
                    modifier = Modifier.noRippleClickable { onClickEdit() },
                    imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.edit),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row {
            Box(
                modifier = Modifier
                    .width(78.dp)
                    .height(86.dp)
            ) {
                CardCategoryChip(category = category)
                RecordStar(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    category = category
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = memo,
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Body1
            )
        }
    }
}

@Composable
internal fun EmptyCard(
    modifier: Modifier = Modifier,
    type: ConsumptionType,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(118.dp)
            .noRippleClickable { onClick() },
        backgroundColor = Color.White.copy(0.1f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = type.title,
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold)
            )
            CircleButton(
                buttonSize = CircleButtonSize.Small,
                backgroundColor = DonmaniTheme.colors.DeepBlue70,
                contentColor = DonmaniTheme.colors.DeepBlue99,
                onClick = onClick
            )
        }
    }
}

@Composable
internal fun ConsumptionCard(
    modifier: Modifier = Modifier,
    consumption: Consumption,
    onClickEdit: (Consumption) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = consumption.category.getColor().copy(alpha = 0.5f)
    ) {
        ConsumptionContent(
            title = consumption.category.getTitle(consumption.type),
            category = consumption.category,
            memo = consumption.description,
            onClickEdit = { onClickEdit(consumption) }
        )
    }

}

@Composable
internal fun RecordCard(
    modifier: Modifier = Modifier,
    record: ConsumptionRecord,
    showEdit: Boolean = true,
    onClickEdit: (Consumption) -> Unit
) {
    record.goodRecord?.let { good ->
        record.badRecord?.let { bad ->
            Card(
                modifier = modifier.fillMaxWidth(),
                backgroundColor = Brush.linearGradient(
                    listOf(
                        good.category.getColor(),
                        bad.category.getColor()
                    )
                )
            ) {
                Column {
                    ConsumptionContent(
                        title = good.category.getTitle(ConsumptionType.GOOD),
                        category = good.category,
                        memo = good.description,
                        showEdit = showEdit,
                        onClickEdit = { onClickEdit(good) }
                    )
                    Spacer(Modifier.height(32.dp))
                    ConsumptionContent(
                        title = bad.category.getTitle(ConsumptionType.BAD),
                        category = bad.category,
                        memo = bad.description,
                        showEdit = showEdit,
                        onClickEdit = { onClickEdit(bad) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun NoConsumptionCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = getNoConsumptionColor()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_consumption_card_title),
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(20.dp))
            Icon(
                imageVector = ImageVector.vectorResource(getNoConsumptionResId()),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }
}