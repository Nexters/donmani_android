package com.gowoon.statistics.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PercentageIndicator(
    modifier: Modifier = Modifier,
    trackColor: Color,
    targetColor: Color,
    progress: Float
) {
    Box(
        modifier = modifier
            .background(
                color = trackColor,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress)
                .background(
                    color = targetColor,
                    shape = RoundedCornerShape(
                        topStart = 6.dp,
                        bottomStart = 6.dp,
                        topEnd = if (progress == 1f) 6.dp else 0.dp,
                        bottomEnd = if (progress == 1f) 6.dp else 0.dp,
                    )
                )
        )
    }
}

