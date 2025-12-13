package com.gowoon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.ads.AdView
import com.gowoon.designsystem.theme.DonmaniTheme


@Composable
fun AdBanner(adView: AdView, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.wrapContentSize()) {
        Box(
            modifier = modifier
                .width(adView.adSize?.width?.dp ?: 0.dp)
                .height(adView.adSize?.height?.dp ?: 0.dp)
                .background(DonmaniTheme.colors.DeepBlue80)
        )
        AndroidView(modifier = modifier.wrapContentSize(), factory = { adView })
    }

    LifecycleResumeEffect(adView) {

        adView.resume()
        onPauseOrDispose { adView.pause() }
    }
}
