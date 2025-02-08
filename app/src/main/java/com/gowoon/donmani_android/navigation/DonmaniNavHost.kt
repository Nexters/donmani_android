package com.gowoon.donmani_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gowoon.home.navigation.homeNavigationRoute
import com.gowoon.home.navigation.homeScreen

@Composable
fun DonmaniNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = homeNavigationRoute,
        modifier = modifier
    ) {
        homeScreen()
    }
}