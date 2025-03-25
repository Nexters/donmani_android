package com.gowoon.donmani_android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gowoon.home.navigation.HomeNavigationRoute
import com.gowoon.splash.navigation.SplashNavigationRoute

@Composable
fun rememberAppState(navController: NavController): AppState =
    remember(navController) {
        AppState(navController)
    }

@Stable
class AppState(private val navController: NavController) {
    private val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val isBeforeHome: Boolean
        @Composable get() = (currentDestination?.route?.contains(HomeNavigationRoute::class.simpleName.toString()) == true || currentDestination?.route == SplashNavigationRoute)
}