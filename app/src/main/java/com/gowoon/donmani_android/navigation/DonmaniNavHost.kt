package com.gowoon.donmani_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gowoon.common.di.FeatureJson
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen
import com.gowoon.record.navigation.InputToMainArgumentKey
import com.gowoon.record.navigation.navigateToRecord
import com.gowoon.record.navigation.navigateToRecordInput
import com.gowoon.record.navigation.recordGraph
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
            popBackStackWithArgument = { data ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    InputToMainArgumentKey,
                    data
                )
                navController.popBackStack()
            }
        )
        settingScreen(onClickBack = navController::popBackStack)
    }
}