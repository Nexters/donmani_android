package com.gowoon.record.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.RecordType
import com.gowoon.ui.component.Card
import com.gowoon.ui.component.CircleButton
import com.gowoon.ui.component.CircleButtonSize
import com.gowoon.ui.noRippleClickable

@Composable
internal fun EmptyCard(
    modifier: Modifier = Modifier,
    type: RecordType,
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
                text = type.description,
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
internal fun RecordCard() {

}

@Preview
@Composable
private fun CardPreview() {
    EmptyCard(type = RecordType.GOOD){}
}
