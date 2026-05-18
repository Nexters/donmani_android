package com.gowoon.donmani_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.common.util.NotificationConstants
import com.gowoon.common.util.NotificationPermissionUtil
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.donmani_android.navigation.DonmaniNavHost
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
        val notificationType = intent.extras?.getString(NotificationConstants.NOTIFICATION_TYPE_KEY)
        val isTodayExpenseExist = intent.extras?.getString(NotificationConstants.IS_TODAY_EXPENSE_EXIST_KEY)
        notificationType?.let {
            FirebaseAnalyticsUtil.sendEvent(
                trigger = FirebaseAnalyticsUtil.EventTrigger.OPEN,
                eventName = "notification_open",
                Pair("notificationType", it)
            )
        }
        setContent {
            DonmaniTheme {
                val navController = rememberNavController()
                val appState = rememberAppState(navController)
                GradientBackground {
                    DonmaniNavHost(
                        navController = navController,
                        isFromFcmAndType = notificationType,
                        isTodayExpenseExist = isTodayExpenseExist
                    )
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
}
