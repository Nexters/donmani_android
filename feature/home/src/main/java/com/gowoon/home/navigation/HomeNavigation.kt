package com.gowoon.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.home.HomeScreen

const val MainToHomeArgumentKey = "mainToHome"
const val homeNavigationRoute = "home_route"

fun NavGraphBuilder.homeScreen(
    navigateToSetting: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToRecord: (Boolean, Boolean) -> Unit,
    navigateToRecordList: () -> Unit
) {
    composable(route = homeNavigationRoute) { backStackEntry ->
        val result = backStackEntry.savedStateHandle.get<String>(MainToHomeArgumentKey)
        HomeScreen(
            resultFromRecord = result,
            onClickSetting = navigateToSetting,
            onClickAdd = navigateToRecord,
            onClickBottle = navigateToRecordList
        )
    }
}