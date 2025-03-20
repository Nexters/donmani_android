package com.gowoon.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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

@Composable
fun CustomSnackBarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = { data ->
            CustomSnackBar(data)
        }
    )
}

@Composable
private fun CustomSnackBar(data: SnackbarData) {
    Row(
        modifier = Modifier
            .padding(bottom = 40.dp)
            .background(shape = RoundedCornerShape(32.dp), color = Color(0xff060B11).copy(0.9f))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.warning),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = data.visuals.message,
            color = Color.White,
            style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}
