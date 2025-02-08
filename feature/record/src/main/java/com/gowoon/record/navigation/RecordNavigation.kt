package com.gowoon.record.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.record.RecordMainScreen

const val recordNavigationRoute = "record_route"

fun NavController.navigateToHome(){
    navigate(route = recordNavigationRoute)
}

fun NavGraphBuilder.homeScreen(){
    composable(route = recordNavigationRoute) { RecordMainScreen() }
}