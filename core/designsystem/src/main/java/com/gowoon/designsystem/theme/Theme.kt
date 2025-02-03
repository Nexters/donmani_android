package com.gowoon.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun DonmaniTheme(
    content: @Composable () -> Unit
){
    val colors = Colors()
    val typography = Typography()
    val dimens = Dimens()

    CompositionLocalProvider(
        LocalColors provides  colors,
        LocalTypography provides typography,
        LocalDimens provides dimens,
        content = content
    )
}

object DonmaniTheme{
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