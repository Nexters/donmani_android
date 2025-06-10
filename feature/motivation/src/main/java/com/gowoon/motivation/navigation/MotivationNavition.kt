package com.gowoon.motivation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.motivation.BuildConfig
import com.gowoon.motivation.DecorationScreen
import com.gowoon.motivation.RewardScreen
import kotlinx.serialization.Serializable

const val RewardNavigationRoute = "motivation_route"

@Serializable
data class DecorationNavigationRoute(
    val selected: String? = null
)

fun NavController.navigateToReward() {
    navigate(route = RewardNavigationRoute)
}

fun NavController.navigateToDecoration() {
    navigate(route = DecorationNavigationRoute())
}

fun NavController.navigateToDecorationAndPopUpTo(selected: String) {
    navigate(route = DecorationNavigationRoute(selected)) {
        currentBackStackEntry?.destination?.id?.let {
            popUpTo(it) { inclusive = true }
        }
    }
}

fun NavGraphBuilder.motivationScreen(
    onClickBack: () -> Unit,
    navigateToRecord: () -> Unit,
    navigateToWebView: (String) -> Unit,
    navigateToDecoration: (String) -> Unit,
    navigateToHome: (String) -> Unit
) {
    composable(route = RewardNavigationRoute) {
        RewardScreen(
            onClickBack = onClickBack,
            onClickGoToRecord = navigateToRecord,
            onClickReview = { navigateToWebView(BuildConfig.REVIEW_URL) },
            onClickGoToDecoration = navigateToDecoration
        )
    }
    composable<DecorationNavigationRoute>() {
        DecorationScreen(
            onClickBack = onClickBack,
            onClickSave = navigateToHome
        )
    }
}