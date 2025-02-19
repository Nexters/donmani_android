package com.gowoon.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

// Density
@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }
@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }