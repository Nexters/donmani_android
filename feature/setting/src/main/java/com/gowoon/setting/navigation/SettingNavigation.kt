package com.gowoon.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.setting.BuildConfig
import com.gowoon.setting.SettingScreen

const val settingNavigationRoute = "setting_route"

fun NavController.navigateToSetting() {
    navigate(route = settingNavigationRoute)
}

fun NavGraphBuilder.settingScreen(
    onClickBack: () -> Unit,
    navigateToWebView: (String) -> Unit
) {
    composable(route = settingNavigationRoute) {
        SettingScreen(
            onClickBack = onClickBack,
            onClickPrivatePrivacy = { navigateToWebView(BuildConfig.PRIVATE_PRIVACY_URL) },
            onClickFeedback = { navigateToWebView(BuildConfig.FEEDBACK_URL) }
        )
    }
}
