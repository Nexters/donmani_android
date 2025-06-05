package com.gowoon.motivation

import androidx.compose.runtime.Composable
import com.gowoon.designsystem.component.AppBar
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.GradientBackground

@Composable
internal fun DecorationScreen(
    onClickBack: () -> Unit
) {
    BBSScaffold(
        background = { GradientBackground() },
        topBar = { AppBar(onClickNavigation = onClickBack) }
    ) {
        
    }
}