package com.gowoon.record.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.record.RecordInputScreen
import com.gowoon.record.RecordMainScreen
import kotlinx.serialization.Serializable

const val InputToMainArgumentKey = "inputToMain"
const val MainToHomeArgumentKey = "mainToHome"

@Serializable
data class RecordNavigationRoute(val hasTodayRecord: Boolean, val hasYesterdayRecord: Boolean)

fun NavController.navigateToRecord(
    hasTodayRecord: Boolean,
    hasYesterdayRecord: Boolean
) {
    navigate(route = RecordNavigationRoute(hasTodayRecord, hasYesterdayRecord))
}

fun NavController.navigateToRecordAndPopUpTo() {
    navigate(route = RecordNavigationRoute(false, false)) {
        currentBackStackEntry?.destination?.id?.let {
            popUpTo(it) { inclusive = true }
        }
    }
}

@Serializable
data class RecordInputNavigationRoute(
    val type: ConsumptionType? = null,
    val consumption: String? = null
)

fun NavController.navigateToRecordInput(
    type: ConsumptionType? = null,
    consumption: String? = null
) {
    navigate(route = RecordInputNavigationRoute(type, consumption))
}

fun NavGraphBuilder.recordGraph(
    onClickBack: () -> Unit,
    navigateToRecordInput: (ConsumptionType) -> Unit,
    navigateToRecordInputWithData: (Consumption) -> Unit,
    popBackStackWithArgument: (key: String, data: String) -> Unit,
) {
    composable<RecordNavigationRoute> { backStackEntry ->
        val result = backStackEntry.savedStateHandle.get<String>(InputToMainArgumentKey)
        RecordMainScreen(
            resultFromInput = result,
            onClickBack = onClickBack,
            onClickAdd = navigateToRecordInput,
            onClickEdit = navigateToRecordInputWithData,
            onSave = popBackStackWithArgument
        )
    }
    composable<RecordInputNavigationRoute> {
        RecordInputScreen(
            onClickBack = onClickBack,
            onClickDone = popBackStackWithArgument
        )
    }
}
