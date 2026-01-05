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
data class StarBottleNavigationRoute(val year: Int, val month: Int, val state: String)

fun NavController.navigateToStarBottleList() {
    navigate(StarBottleListNavigationRoute)
}

fun NavController.navigateToStarBottle(year: Int, month: Int, state: String) {
    navigate(StarBottleNavigationRoute(year, month, state))
}

fun NavGraphBuilder.starBottleListScreen(
    onClickBack: () -> Unit,
    navigateToStarBottle: (Int, Int, String) -> Unit,
    navigateToRecordList: (list: List<Record>, Int, Int) -> Unit
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