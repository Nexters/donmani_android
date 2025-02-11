package com.gowoon.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.home.HomeScreen

const val homeNavigationRoute = "home_route"

fun NavGraphBuilder.homeScreen(
    onClickSetting: () -> Unit,
    onClickCalendar: () -> Unit,
    onClickAdd: (Boolean, Boolean) -> Unit
) {
    composable(route = homeNavigationRoute) { HomeScreen(onClickAdd = onClickAdd) }
}