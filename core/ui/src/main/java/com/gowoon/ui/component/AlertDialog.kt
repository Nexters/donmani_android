package com.gowoon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gowoon.designsystem.component.NegativeButton
import com.gowoon.designsystem.component.PositiveButton
import com.gowoon.designsystem.theme.DonmaniTheme

@Composable
fun AlertDialog(
    title: String,
    description: String,
    positiveTitle: String,
    negativeTitle: String,
    onClickPositive: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    shape = RoundedCornerShape(32.dp),
                    color = DonmaniTheme.colors.DeepBlue60
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
                color = DonmaniTheme.colors.DeepBlue99
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.DeepBlue90
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NegativeButton(
                    modifier = Modifier.weight(1f),
                    label = negativeTitle,
                    onClick = onDismissRequest
                )
                PositiveButton(
                    modifier = Modifier.weight(1f),
                    label = positiveTitle
                ) {
                    onDismissRequest()
                    onClickPositive()
                }
            }
        }
    }
}
