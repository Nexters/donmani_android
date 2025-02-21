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

            is RecordInputEvent.ShowCategoryDialog -> {
                setState(currentState.copy(showCategoryDialog = event.show))
            }

            is RecordInputEvent.ShowExitWaringBottomSheet -> {
                setState(currentState.copy(showExitWarningBottomSheet = event.show))
            }
        }
    }

    private fun initialState() {
        consumption?.let {
            val consumption = json.decodeFromString<Consumption>(it)
            setState(
                currentState.copy(
                    originalContent = consumption,
                    showCategoryDialog = false,
                    type = consumption.type,
                    category = consumption.category,
                    memo = TextFieldState(consumption.description)
                )
            )
        } ?: run {
            type?.let {
                setState(currentState.copy(showCategoryDialog = true, type = it))
            }
        }
    }

    // 새로 작성시에는 작성된 내용이 있는지, 수정시에는 변경되었는지
    fun changedRecord(): Boolean {
        with(currentState) {
            return if (originalContent == null) {
                // 새로 작성
                !(category == null && memo.text.isEmpty())
            } else {
                // 수정
                !(originalContent.category == category && originalContent.description == memo.text)
            }
        }
    }

    fun showToast(message: String) {
        setEffect(RecordInputEffect.ShowToast(message))
    }
}

data class RecordInputState(
    val originalContent: Consumption? = null,
    val showCategoryDialog: Boolean = false,
    val type: ConsumptionType = ConsumptionType.GOOD,
    val category: Category? = null,
    val memo: TextFieldState = TextFieldState(),
    val showExitWarningBottomSheet: Boolean = false
) : UiState

sealed interface RecordInputEvent : UiEvent {
    data class OnChangeCategory(val category: Category) : RecordInputEvent
    data class ShowCategoryDialog(val show: Boolean) : RecordInputEvent
    data class ShowExitWaringBottomSheet(val show: Boolean) : RecordInputEvent
}

sealed interface RecordInputEffect : UiEffect {
    data class ShowToast(val message: String) : RecordInputEffect
}