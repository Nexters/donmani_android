package com.gowoon.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.model.record.Category
import com.gowoon.model.record.ConsumptionType
import com.gowoon.record.navigation.RecordInputNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<RecordInputState, RecordInputEvent, RecordInputEffect>() {
    override fun createInitialState(): RecordInputState = RecordInputState()

    init {
        setState(currentState.copy(type = savedStateHandle.toRoute<RecordInputNavigationRoute>().type))
    }

    override fun handleEvent(event: RecordInputEvent) {
        when (event) {
            is RecordInputEvent.OnChangeCategory -> {
                setState(currentState.copy(category = event.category))
            }
        }
    }
}

data class RecordInputState(
    val type: ConsumptionType = ConsumptionType.GOOD,
    val category: Category? = null,
    val memo: TextFieldState = TextFieldState()
) : UiState

sealed class RecordInputEvent : UiEvent {
    data class OnChangeCategory(val category: Category) : RecordInputEvent()
}

sealed class RecordInputEffect : UiEffect