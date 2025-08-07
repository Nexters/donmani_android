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
    navigateToDecoration: () -> Unit,
    navigateToWebView: (String) -> Unit,
    navigateToSystemSetting: () -> Unit
) {
    composable(route = settingNavigationRoute) {
        SettingScreen(
            onClickBack = onClickBack,
            onClickDecoration = navigateToDecoration,
            onClickNotice = { navigateToWebView(BuildConfig.NOTICE_URL) },
            onClickPrivatePrivacy = { navigateToWebView(BuildConfig.PRIVATE_PRIVACY_URL) },
            onClickFeedback = { navigateToWebView(BuildConfig.FEEDBACK_URL) },
            onClickPush = navigateToSystemSetting
        )
    }
}
