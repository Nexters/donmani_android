package com.gowoon.statistics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.statistics.BuildConfig
import com.gowoon.statistics.StatisticsScreen
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsNavigation(
    val year: String,
    val month: Int
)

fun NavController.navigateToStatistics(year: String, month: Int) {
    navigate(route = StatisticsNavigation(year, month))
}

fun NavGraphBuilder.statisticsScreen(
    onClickBack: () -> Unit,
    navigateToWebView: (String) -> Unit
) {
    composable<StatisticsNavigation> {
        StatisticsScreen(
            onClickBack = onClickBack,
            onClickRequest = {
                navigateToWebView(BuildConfig.REQUEST_URL)
            }
        )
    }
}