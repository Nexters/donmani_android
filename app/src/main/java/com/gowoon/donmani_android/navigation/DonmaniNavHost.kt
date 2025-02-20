package com.gowoon.donmani_android.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gowoon.common.di.FeatureJson
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen
import com.gowoon.record.navigation.navigateToRecord
import com.gowoon.record.navigation.navigateToRecordInput
import com.gowoon.record.navigation.recordGraph
import com.gowoon.recordlist.navigation.recordListScreen
import com.gowoon.setting.navigation.navigateToSetting
import com.gowoon.setting.navigation.settingScreen
import com.gowoon.ui.util.rememberHiltJson

// TODO enter, exit transition
@Composable
fun DonmaniNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    @FeatureJson
    val json = rememberHiltJson()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = homeNavigationRoute,
        modifier = modifier
    ) {
        homeScreen(
            navigateToSetting = navController::navigateToSetting,
            navigateToCalendar = {},
            navigateToRecord = navController::navigateToRecord
        )
        recordGraph(
            onClickBack = navController::popBackStack,
            navigateToRecordInput = {
                navController.navigateToRecordInput(type = it)
            },
            navigateToRecordInputWithData = {
                navController.navigateToRecordInput(consumption = json.encodeToString(it))
            },
            popBackStackWithArgument = { key, data ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    key,
                    data
                )
                navController.popBackStack()
            }
        )
        recordListScreen(
            onClickBack = navController::popBackStack
        )
        settingScreen(
            onClickBack = navController::popBackStack,
            navigateToWebView = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            }
        )
    }
}