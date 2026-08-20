package com.gowoon.fortune.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.fortune.FortuneScreen
import kotlinx.serialization.Serializable

@Serializable
data object FortuneNavigationRoute

fun NavController.navigateToFortune() {
    navigate(FortuneNavigationRoute)
}

fun NavGraphBuilder.fortuneScreen(
    onClickBack: () -> Unit,
    navigateToRecord: (Boolean, Boolean, String) -> Unit
) {
    composable<FortuneNavigationRoute> {
        FortuneScreen(
            onClickBack = onClickBack,
            navigateToRecord = navigateToRecord
        )
    }
}
