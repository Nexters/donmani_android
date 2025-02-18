package com.gowoon.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.home.HomeScreen

const val homeNavigationRoute = "home_route"

fun NavGraphBuilder.homeScreen(
    navigateToSetting: () -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToRecord: (Boolean, Boolean) -> Unit
) {
    composable(route = homeNavigationRoute) { HomeScreen(onClickAdd = navigateToRecord) }
}