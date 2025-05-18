package com.gowoon.donmani_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.common.util.NotificationPermissionUtil
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.donmani_android.navigation.DonmaniNavHost
import com.gowoon.ui.BGMode
import com.gowoon.ui.GradientBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkNotificationPermission()
        FirebaseAnalyticsUtil.initialize()
        val isFromFcm = checkFromNotification(intent)
        setContent {
            DonmaniTheme {
                val navController = rememberNavController()
                val appState = rememberAppState(navController)
                GradientBackground(if (appState.isBeforeHome) BGMode.SPECIAL else BGMode.DEFAULT) {
                    DonmaniNavHost(navController = navController, isFromFcm = isFromFcm)
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (!NotificationPermissionUtil.isNotificationPermissionGranted(this)) {
            NotificationPermissionUtil.requestNotificationPermission(this) {
                if (it) {
                    // TODO granted
                }
            }
        }
    }

    private fun checkFromNotification(intent: Intent): Boolean {
        // 지금은 진입 루트가 런처랑 노티뿐이라 임시로 extra 데이터 있는지만 확인하지만, 서버랑 협의해서 fcm noti 구분할 수 있는 scheme, key 등등 논의 필요
        return intent.extras != null
    }
}
