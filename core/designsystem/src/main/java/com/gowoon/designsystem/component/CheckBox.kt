package com.gowoon.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable

@Composable
fun CheckBoxWithTitle(
    modifier: Modifier = Modifier,
    checked: Boolean,
    title: String,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp)
            .noRippleClickable { onClick(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                if (checked) R.drawable.check_on
                else R.drawable.check_off
            ),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = title,
            color = if (checked) DonmaniTheme.colors.Gray95 else DonmaniTheme.colors.DeepBlue80,
            style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold)
        )
    }

}