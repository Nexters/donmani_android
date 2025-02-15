package com.gowoon.record.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.ConsumptionType
import com.gowoon.record.R
import com.gowoon.ui.component.Card
import com.gowoon.ui.component.CircleButton
import com.gowoon.ui.component.CircleButtonSize
import com.gowoon.ui.noRippleClickable
import com.gowoon.ui.util.getNoConsumptionColor

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
                contentColor = DonmaniTheme.colors.DeepBlue99
            ) { }
        }
    }
}

@Composable
internal fun ConsumptionCard() {

}

@Composable
internal fun RecordCard() {

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
                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.icon_no_consumption),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun CardPreview() {
    NoConsumptionCard()
}
