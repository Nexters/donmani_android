package com.gowoon.motivation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.motivation.BuildConfig
import com.gowoon.motivation.DecorationScreen
import com.gowoon.motivation.RewardScreen

const val RewardNavigationRoute = "motivation_route"
const val DecorationNavigationRoute = "decoration_route"

fun NavController.navigateToReward() {
    navigate(route = RewardNavigationRoute)
}

fun NavController.navigateToDecoration() {
    navigate(route = DecorationNavigationRoute)
}

fun NavGraphBuilder.motivationScreen(
    onClickBack: () -> Unit,
    navigateToRecord: () -> Unit,
    navigateToWebView: (String) -> Unit,
    navigateToDecoration: () -> Unit
) {
    composable(route = RewardNavigationRoute) {
        RewardScreen(
            onClickBack = onClickBack,
            onClickGoToRecord = navigateToRecord,
            onClickReview = { navigateToWebView(BuildConfig.REVIEW_URL) },
            onClickGoToDecoration = navigateToDecoration
        )
    }
    composable(route = DecorationNavigationRoute) {
        DecorationScreen(
            onClickBack = onClickBack
        )
    }
}