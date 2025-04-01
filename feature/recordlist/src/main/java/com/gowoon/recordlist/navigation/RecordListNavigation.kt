package com.gowoon.recordlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.recordlist.RecordListScreen
import kotlinx.serialization.Serializable

@Serializable
data class RecordListNavigationRoute(
    val records: String,
    val year: Int,
    val month: Int
)

fun NavController.navigateToRecordList(records: String, year: Int, month: Int) {
    navigate(route = RecordListNavigationRoute(records, year, month))
}

fun NavGraphBuilder.recordListScreen(
    onClickBack: () -> Unit,
    navigateToRecord: () -> Unit,
    navigateToStatistics: (Int, Int) -> Unit,
    navigateToStarBottleList: () -> Unit
) {
    composable<RecordListNavigationRoute> {
        RecordListScreen(
            onClickBack = onClickBack,
            onClickAdd = navigateToRecord,
            onClickSummary = navigateToStatistics,
            onClickActionButton = navigateToStarBottleList
        )
    }
}