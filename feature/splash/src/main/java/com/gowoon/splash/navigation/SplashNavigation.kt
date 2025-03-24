package com.gowoon.splash.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.splash.SplashScreen

const val SplashNavigationRoute = "splash_route"

fun NavController.navigateToSplash() {
    navigate(SplashNavigationRoute)
}

fun NavGraphBuilder.splashScreen(
    navigateToHome: () -> Unit,
    navigateToOnBoarding: () -> Unit
) {
    composable(
        route = SplashNavigationRoute,
        enterTransition = {
            expandVertically(
                expandFrom = Alignment.CenterVertically,
                animationSpec = tween(1000)
            ) + fadeIn(animationSpec = tween(1000))
        }
    ) {
        SplashScreen(
            navigateToHome = navigateToHome,
            navigateToOnBoarding = navigateToOnBoarding
        )
    }
}