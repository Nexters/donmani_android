package com.gowoon.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.tooltip.GetNoConsumptionTooltipStateUseCase
import com.gowoon.domain.usecase.tooltip.HideNoConsumptionTooltipUseCase
import com.gowoon.model.common.EntryDay
import com.gowoon.model.record.ConsumptionRecord
import com.gowoon.model.record.NoConsumption
import com.gowoon.model.record.Record
import com.gowoon.record.navigation.RecordNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RecordMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoConsumptionTooltipStateUseCase: GetNoConsumptionTooltipStateUseCase,
    private val hideNoConsumptionTooltipUseCase: HideNoConsumptionTooltipUseCase
) : BaseViewModel<RecordMainState, RecordMainEvent, RecordMainEffect>() {
    override fun createInitialState(): RecordMainState = RecordMainState()

    init {
        initialState(
            showToday = savedStateHandle.toRoute<RecordNavigationRoute>().hasTodayRecord.not().not(),
            showYesterday = savedStateHandle.toRoute<RecordNavigationRoute>().hasYesterdayRecord.not()
        )
    }
    override fun handleEvent(event: RecordMainEvent) {
        when (event) {
            is RecordMainEvent.OnClickDayToggle -> {
                setState(currentState.copy(selectedDay = event.selected))
            }

            is RecordMainEvent.OnClickNoConsumptionCheckBox -> {
                val newRecord = if (event.checked) {
                    NoConsumption
                } else {
                    ConsumptionRecord()
                }
                updateCurrentDayRecord(newRecord = newRecord)
            }

            is RecordMainEvent.OnClickNoConsumptionTooltip -> {
                hideTooltip()
            }
        }
    }

    private fun initialState(showToday: Boolean, showYesterday: Boolean) {
        viewModelScope.launch {
            getNoConsumptionTooltipStateUseCase().stateIn(this).collect {
                when (it) {
                    is Result.Success -> {
                        setState(
                            currentState.copy(
                                todayRecord = if (showToday) ConsumptionRecord() else null,
                                yesterdayRecord = if (showYesterday) ConsumptionRecord() else null,
                                selectedDay = if (showToday) EntryDay.Today else EntryDay.Yesterday,
                                showTooltip = it.data
                            )
                        )
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
    }

    private fun updateCurrentDayRecord(newRecord: Record) {
        when (currentState.selectedDay) {
            EntryDay.Today -> setState(currentState.copy(todayRecord = newRecord))
            EntryDay.Yesterday -> setState(currentState.copy(yesterdayRecord = newRecord))
        }
    }

    private fun hideTooltip() {
        viewModelScope.launch {
            if (hideNoConsumptionTooltipUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }
}

data class RecordMainState(
    val todayRecord: Record? = null,
    val yesterdayRecord: Record? = null,
    val selectedDay: EntryDay = EntryDay.Today,
    val showTooltip: Boolean = false
) : UiState

sealed class RecordMainEvent : UiEvent {
    data class OnClickDayToggle(val selected: EntryDay) : RecordMainEvent()
    data class OnClickNoConsumptionCheckBox(val checked: Boolean) : RecordMainEvent()
    data object OnClickNoConsumptionTooltip : RecordMainEvent()
}

sealed class RecordMainEffect : UiEffect