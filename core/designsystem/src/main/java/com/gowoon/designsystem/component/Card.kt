package com.gowoon.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Card(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(24.dp),
                color = backgroundColor
            )
            .padding(20.dp)
    ) {
        content()
    }
}

@Composable
fun Card(
    modifier: Modifier = Modifier,
    backgroundColor: Brush,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(24.dp),
                brush = backgroundColor
            )
            .padding(20.dp)
    ) {
        content()
    }
}