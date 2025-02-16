package com.gowoon.record.navigation

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gowoon.model.record.ConsumptionType
import com.gowoon.record.RecordMainScreen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

const val InputToMainArgumentKey = "inputToMain"

@Parcelize
data class InputToMainArgument(
    val type: String,
    val category: String,
    val memo: String
) : Parcelable

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
    onClickBack: () -> Unit,
    navigateToRecordInput: (type: ConsumptionType) -> Unit,
    popBackStackWithArgument: (data: InputToMainArgument) -> Unit
) {
    composable<RecordNavigationRoute> {
        RecordMainScreen(
            onClickBack = onClickBack,
            onClickAdd = navigateToRecordInput
        )
    }
    composable<RecordInputNavigationRoute> {
        RecordInputScreen(
            onClickBack = onClickBack
        )
    }
}
