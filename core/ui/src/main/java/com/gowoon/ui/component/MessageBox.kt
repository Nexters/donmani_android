package com.gowoon.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme

@Composable
fun MessageBox(modifier: Modifier = Modifier, message: String) {
    Row(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_default),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = message,
            color = DonmaniTheme.colors.PurpleBlue90,
            style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Preview
@Composable
private fun MessageBoxPreview(){
    MessageBox(message = "메시지입니다.")
}