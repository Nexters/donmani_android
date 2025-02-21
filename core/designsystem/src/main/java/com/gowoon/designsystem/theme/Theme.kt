package com.gowoon.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun DonmaniTheme(
    content: @Composable () -> Unit
) {
    val colors = Colors()
    val typography = Typography()
    val dimens = Dimens()

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalDimens provides dimens,
        content = content
    )
}

object DonmaniTheme {
    val colors: Colors
        @Composable
        get() = LocalColors.current
    val typography: Typography
        @Composable
        get() = LocalTypography.current
    val dimens: Dimens
        @Composable
        get() = LocalDimens.current
}