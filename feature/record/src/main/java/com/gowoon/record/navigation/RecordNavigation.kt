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

@Serializable
data class RecordNavigationRoute(
    val hasTodayRecord: Boolean,
    val hasYesterdayRecord: Boolean,
    val referrer: String
)

fun NavController.navigateToRecord(
    referrer: String,
    hasTodayRecord: Boolean,
    hasYesterdayRecord: Boolean,
    fromStart: Boolean = false
) {
    navigate(route = RecordNavigationRoute(hasTodayRecord, hasYesterdayRecord, referrer)) {
        if (fromStart) {
            popUpTo(graph.startDestinationId) { inclusive = true }
        }
    }
}

fun NavController.navigateToRecordAndPopUpTo(referrer: String) {
    navigate(route = RecordNavigationRoute(false, false, referrer)) {
        currentBackStackEntry?.destination?.id?.let {
            popUpTo(it) { inclusive = true }
        }
    }
}

@Serializable
data class RecordInputNavigationRoute(
    val type: ConsumptionType? = null,
    val consumption: String? = null,
    val screenType: String
)

fun NavController.navigateToRecordInput(
    type: ConsumptionType? = null,
    consumption: String? = null,
    screenType: String
) {
    navigate(route = RecordInputNavigationRoute(type, consumption, screenType))
}

fun NavGraphBuilder.recordGraph(
    onClickBack: () -> Unit,
    navigateToHome: (data: String?) -> Unit,
    navigateToRecordInput: (ConsumptionType, String) -> Unit,
    navigateToRecordInputWithData: (Consumption, String) -> Unit,
    popBackStackWithArgument: (key: String, data: String) -> Unit,
) {
    composable<RecordNavigationRoute> { backStackEntry ->
        val result = backStackEntry.savedStateHandle.get<String>(InputToMainArgumentKey)
        RecordMainScreen(
            resultFromInput = result,
            navigateToHome = { navigateToHome(null) },
            onClickAdd = navigateToRecordInput,
            onClickEdit = navigateToRecordInputWithData,
            onSave = navigateToHome
        )
    }
    composable<RecordInputNavigationRoute> {
        RecordInputScreen(
            onClickBack = onClickBack,
            onClickDone = popBackStackWithArgument
        )
    }
}
