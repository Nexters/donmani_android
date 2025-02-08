package com.gowoon.donmani_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.donmani_android.navigation.DonmaniNavHost
import com.gowoon.ui.BGMode
import com.gowoon.ui.GradientBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonmaniTheme {
                val appState = rememberAppState()
                GradientBackground(if (appState.isHome) BGMode.MAIN else BGMode.DEFAULT) {
                    DonmaniNavHost()
                }
            }
        }
    }
}
