package com.gowoon.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.home.HomeScreen

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(){
    navigate(route = homeNavigationRoute)
}

fun NavGraphBuilder.homeScreen(){
    composable(route = homeNavigationRoute) { HomeScreen() }
}