package com.gowoon.ui.util

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.gowoon.ui.BuildConfig

@Composable
fun rememberAdView(context: Context, configuration: Configuration): AdView = remember {
    AdView(context).apply {
        adUnitId = BuildConfig.AD_UNIT_ID
        setAdSize(
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                context,
                configuration.screenWidthDp
            )
        )
        adListener =
            object : AdListener() {
                override fun onAdLoaded() {
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                }

                override fun onAdImpression() {
                }

                override fun onAdClicked() {
                }
            }
    }
}