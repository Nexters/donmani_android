package com.gowoon.record.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.record.RecordMainScreen
import kotlinx.serialization.Serializable

@Serializable
data class RecordNavigationRoute(val hasTodayRecord: Boolean, val hasYesterdayRecord: Boolean)

fun NavController.navigateToRecord(hasTodayRecord: Boolean, hasYesterdayRecord: Boolean) {
    navigate(route = RecordNavigationRoute(hasTodayRecord, hasYesterdayRecord))
}

fun NavGraphBuilder.recordScreen(
    onClickBack: () -> Unit
) {
    composable<RecordNavigationRoute> {
        RecordMainScreen(
            onClickBack = onClickBack
        )
    }
}