package com.gowoon.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.onboarding.OnBoardingScreen

const val OnBoardingNavigationRoute = "onboarding_route"

fun NavController.navigateToOnBoarding() {
    navigate(route = OnBoardingNavigationRoute)
}

fun NavGraphBuilder.onBoardingScreen(
    navigateToHome: () -> Unit,
    navigateToRecord: () -> Unit
) {
    composable(route = OnBoardingNavigationRoute) {
        OnBoardingScreen(
            navigateToHome = navigateToHome,
            navigateToRecord = navigateToRecord
        )
    }
}