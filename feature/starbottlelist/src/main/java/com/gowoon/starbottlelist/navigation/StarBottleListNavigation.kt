package com.gowoon.starbottlelist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.starbottlelist.StarBottleListScreen

const val StarBottleListNavigationRoute = "star_bottle_list_route"

fun NavController.navigateToStarBottleList() {
    navigate(StarBottleListNavigationRoute)
}

fun NavGraphBuilder.starBottleListScreen(
    onClickBack: () -> Unit
) {
    composable(route = StarBottleListNavigationRoute) {
        StarBottleListScreen(onClickBack = onClickBack)
    }
}