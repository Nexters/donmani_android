package com.gowoon.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme

@Composable
internal fun Reddot(modifier: Modifier = Modifier){
    Box(modifier.size(6.dp).background(color = DonmaniTheme.colors.Red40, shape = CircleShape))
}