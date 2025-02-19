package com.gowoon.donmani_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gowoon.common.di.FeatureJson
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen
import com.gowoon.record.navigation.InputToMainArgumentKey
import com.gowoon.record.navigation.navigateToRecord
import com.gowoon.record.navigation.navigateToRecordInput
import com.gowoon.record.navigation.recordGraph
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
            navigateToSetting = {},
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
    }
}