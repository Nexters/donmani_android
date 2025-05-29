package com.gowoon.motivation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.motivation.BuildConfig
import com.gowoon.motivation.RewardScreen

const val RewardNavigationRoute = "motivation_route"

fun NavController.navigateToReward() {
    navigate(route = RewardNavigationRoute)
}

fun NavGraphBuilder.motivationScreen(
    onClickBack: () -> Unit,
    navigateToRecord: () -> Unit,
    navigateToWebView: (String) -> Unit
) {
    composable(route = RewardNavigationRoute) {
        RewardScreen(
            onClickBack = onClickBack,
            onClickGoToRecord = navigateToRecord,
            onClickReview = { navigateToWebView(BuildConfig.REVIEW_URL) }
        )
    }
}