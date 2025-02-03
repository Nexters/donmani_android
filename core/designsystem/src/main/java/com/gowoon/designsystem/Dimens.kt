package com.gowoon.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimens(
    val Margin20: Dp = 20.dp
)

val LocalDimens = staticCompositionLocalOf { Dimens() }