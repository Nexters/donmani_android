package com.gowoon.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gowoon.home.HomeScreen
import com.gowoon.model.record.Record
import kotlinx.serialization.Serializable

@Serializable
data class HomeNavigationRoute(val addedRecord: String?)

fun NavController.navigateToHome(addedRecord: String? = null) {
    navigate(HomeNavigationRoute(addedRecord)) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(
    navigateToSetting: () -> Unit,
    navigateToRecord: (Boolean, Boolean) -> Unit,
    navigateToRecordList: (list: List<Record>, year: Int, month: Int) -> Unit,
    navigateToStarBottleList: () -> Unit
) {
    composable<HomeNavigationRoute> { backStackEntry ->
        val result = backStackEntry.toRoute<HomeNavigationRoute>().addedRecord
        HomeScreen(
            resultFromRecord = result,
            onClickSetting = navigateToSetting,
            onClickAdd = navigateToRecord,
            onClickBottle = navigateToRecordList,
            onClickGoToStarBottle = navigateToStarBottleList
        )
    }
}