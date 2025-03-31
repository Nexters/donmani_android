package com.gowoon.starbottlelist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.model.record.Record
import com.gowoon.starbottlelist.StarBottleListScreen
import com.gowoon.starbottlelist.StarBottleScreen
import kotlinx.serialization.Serializable

const val StarBottleListNavigationRoute = "star_bottle_list_route"

@Serializable
data class StarBottleNavigationRoute(val month: Int, val state: String)

fun NavController.navigateToStarBottleList() {
    navigate(StarBottleListNavigationRoute)
}

fun NavController.navigateToStarBottle(month: Int, state: String) {
    navigate(StarBottleNavigationRoute(month, state))
}

fun NavGraphBuilder.starBottleListScreen(
    onClickBack: () -> Unit,
    navigateToStarBottle: (Int, String) -> Unit,
    navigateToRecordList: (list: List<Record>) -> Unit
) {
    composable(route = StarBottleListNavigationRoute) {
        StarBottleListScreen(
            onClickBack = onClickBack,
            onClickBottle = navigateToStarBottle
        )
    }

    composable<StarBottleNavigationRoute> {
        StarBottleScreen(
            onClickBack = onClickBack,
            onClickBottle = navigateToRecordList
        )
    }
}