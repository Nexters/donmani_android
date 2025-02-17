package com.gowoon.donmani_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen
import com.gowoon.record.navigation.InputToMainArgumentKey
import com.gowoon.record.navigation.navigateToRecord
import com.gowoon.record.navigation.navigateToRecordInput
import com.gowoon.record.navigation.recordGraph

// TODO enter, exit transition
@Composable
fun DonmaniNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
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
            navController = navController,
            onClickBack = navController::popBackStack,
            navigateToRecordInput = navController::navigateToRecordInput,
            popBackStackWithArgument = { data ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    InputToMainArgumentKey,
                    data
                )
                navController.popBackStack()
            }
        )
    }
}