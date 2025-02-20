package com.gowoon.recordlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.recordlist.RecordListScreen

const val recordListNavigationRoute = "record_list_route"

fun NavController.navigateToRecordList() {
    navigate(route = recordListNavigationRoute)
}

fun NavGraphBuilder.recordListScreen(
    onClickBack: () -> Unit
){
    composable(route = recordListNavigationRoute) {
        RecordListScreen(
            onClickBack = onClickBack
        )
    }
}