package com.gowoon.donmani_android

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@HiltAndroidApp
class DonmaniApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {
            // TODO status handling
        }
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }
}