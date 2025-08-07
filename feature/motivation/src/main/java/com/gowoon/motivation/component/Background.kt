package com.gowoon.motivation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
internal fun RewardBackground(){
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(com.gowoon.designsystem.R.drawable.reward_bg),
        contentDescription = null
    )
}