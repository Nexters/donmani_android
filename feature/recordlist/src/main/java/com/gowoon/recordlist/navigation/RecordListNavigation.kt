package com.gowoon.recordlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.recordlist.RecordListScreen
import kotlinx.serialization.Serializable

@Serializable
data class RecordListNavigationRoute(val records: String)

fun NavController.navigateToRecordList(records: String) {
    navigate(route = RecordListNavigationRoute(records))
}

fun NavGraphBuilder.recordListScreen(
    onClickBack: () -> Unit,
    navigateToRecord: () -> Unit
) {
    composable<RecordListNavigationRoute> {
        RecordListScreen(
            onClickBack = onClickBack,
            onClickAdd = navigateToRecord
        )
    }
}