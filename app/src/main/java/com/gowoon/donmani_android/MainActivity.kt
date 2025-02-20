package com.gowoon.donmani_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
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
        setContent {
            DonmaniTheme {
                val navController = rememberNavController()
                val appState = rememberAppState(navController)
                GradientBackground(if (appState.isBeforeHome) BGMode.MAIN else BGMode.DEFAULT) {
                    DonmaniNavHost(navController = navController)
                }
            }
        }
    }
}
