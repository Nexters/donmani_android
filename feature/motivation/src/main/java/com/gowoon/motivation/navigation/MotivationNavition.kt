package com.gowoon.motivation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.motivation.RewardScreen

const val RewardNavigationRoute = "motivation_route"

fun NavController.navigateToReward() {
    navigate(route = RewardNavigationRoute)
}

fun NavGraphBuilder.motivationScreen() {
    composable(route = RewardNavigationRoute) {
        RewardScreen()
    }
}