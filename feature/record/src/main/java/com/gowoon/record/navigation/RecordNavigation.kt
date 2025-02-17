package com.gowoon.record.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.model.record.ConsumptionType
import com.gowoon.record.RecordInputScreen
import com.gowoon.record.RecordMainScreen
import kotlinx.serialization.Serializable

const val InputToMainArgumentKey = "inputToMain"

@Serializable
data class RecordNavigationRoute(val hasTodayRecord: Boolean, val hasYesterdayRecord: Boolean)

fun NavController.navigateToRecord(hasTodayRecord: Boolean, hasYesterdayRecord: Boolean) {
    navigate(route = RecordNavigationRoute(hasTodayRecord, hasYesterdayRecord))
}

@Serializable
data class RecordInputNavigationRoute(val type: ConsumptionType)

fun NavController.navigateToRecordInput(type: ConsumptionType) {
    navigate(route = RecordInputNavigationRoute(type))
}

fun NavGraphBuilder.recordGraph(
    navController: NavController,
    onClickBack: () -> Unit,
    navigateToRecordInput: (type: ConsumptionType) -> Unit,
    popBackStackWithArgument: (data: String) -> Unit
) {
    composable<RecordNavigationRoute> {
        RecordMainScreen(
            navController = navController,
            onClickBack = onClickBack,
            onClickAdd = navigateToRecordInput
        )
    }
    composable<RecordInputNavigationRoute> {
        RecordInputScreen(
            onClickBack = onClickBack,
            onClickDone = popBackStackWithArgument
        )
    }
}
