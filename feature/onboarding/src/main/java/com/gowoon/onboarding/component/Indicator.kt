package com.gowoon.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun Indicator(pageCount: Int, selectedIndex: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(pageCount) { index ->
            val alpha = if (index == selectedIndex) 1f else 0.1f
            Box(
                Modifier
                    .size(6.dp)
                    .background(Color.White.copy(alpha = alpha), shape = CircleShape)
            )
        }
    }
}