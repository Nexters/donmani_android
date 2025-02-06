package com.gowoon.donmani_android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gowoon.home.navigation.homeNavigationRoute

@Composable
fun rememberAppState(navController: NavController = rememberNavController()): AppState =
    remember(navController) {
        AppState(navController)
    }

@Stable
class AppState(private val navController: NavController) {
    private val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val isHome: Boolean
        @Composable get() = currentDestination?.route == homeNavigationRoute
}