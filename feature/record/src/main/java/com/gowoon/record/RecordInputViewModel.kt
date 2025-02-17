package com.gowoon.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.model.record.Category
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.record.navigation.RecordInputNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecordInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val json: Json
) : BaseViewModel<RecordInputState, RecordInputEvent, RecordInputEffect>() {
    override fun createInitialState(): RecordInputState = RecordInputState()
    private val type = savedStateHandle.toRoute<RecordInputNavigationRoute>().type
    private val consumption = savedStateHandle.toRoute<RecordInputNavigationRoute>().consumption

    init {
        initialState()
    }

    override fun handleEvent(event: RecordInputEvent) {
        when (event) {
            is RecordInputEvent.OnChangeCategory -> {
                setState(currentState.copy(category = event.category))
            }

            is RecordInputEvent.ShowDialog -> {
                setState(currentState.copy(showDialog = event.show))
            }
        }
    }

    private fun initialState() {
        consumption?.let {
            val consumption = json.decodeFromString<Consumption>(it)
            setState(
                currentState.copy(
                    showDialog = false,
                    type = consumption.type,
                    category = consumption.category,
                    memo = TextFieldState(consumption.description)
                )
            )
        } ?: run {
            type?.let {
                setState(currentState.copy(showDialog = true, type = it))
            }
        }
    }
}

data class RecordInputState(
    val showDialog: Boolean = false,
    val type: ConsumptionType = ConsumptionType.GOOD,
    val category: Category? = null,
    val memo: TextFieldState = TextFieldState()
) : UiState

sealed class RecordInputEvent : UiEvent {
    data class OnChangeCategory(val category: Category) : RecordInputEvent()
    data class ShowDialog(val show: Boolean) : RecordInputEvent()
}

sealed class RecordInputEffect : UiEffect