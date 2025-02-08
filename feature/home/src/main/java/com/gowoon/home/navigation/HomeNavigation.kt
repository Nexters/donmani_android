package com.gowoon.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.home.HomeScreen

const val homeNavigationRoute = "home_route"

fun NavGraphBuilder.homeScreen() {
    composable(route = homeNavigationRoute) { HomeScreen() }
}