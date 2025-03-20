package com.gowoon.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.home.HomeScreen
import com.gowoon.model.record.Record

const val MainToHomeArgumentKey = "mainToHome"
const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(from: String? = null) {
    navigate(route = homeNavigationRoute) {
        from?.let {
            popUpTo(it) { inclusive = true }
        }
    }
}

fun NavGraphBuilder.homeScreen(
    navigateToSetting: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToRecord: (Boolean, Boolean) -> Unit,
    navigateToRecordList: (list: List<Record>) -> Unit
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