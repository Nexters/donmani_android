package com.gowoon.record

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.model.common.EntryDay
import com.gowoon.model.record.ConsumptionRecord
import com.gowoon.model.record.NoConsumption
import com.gowoon.model.record.Record
import com.gowoon.record.navigation.RecordNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class RecordMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<RecordMainState, RecordMainEvent, RecordMainEffect>() {
    private val showToday = savedStateHandle.toRoute<RecordNavigationRoute>().hasTodayRecord.not()
    private val showYesterday =
        savedStateHandle.toRoute<RecordNavigationRoute>().hasYesterdayRecord.not()

    init {
        setEvent(RecordMainEvent.InitState)
    }

    override fun createInitialState(): RecordMainState = RecordMainState()
    override fun handleEvent(event: RecordMainEvent) {
        when (event) {
            is RecordMainEvent.InitState -> {
                setState(
                    RecordMainState(
                        todayRecord = if (showToday) ConsumptionRecord() else null,
                        yesterdayRecord = if (showYesterday) ConsumptionRecord() else null,
                        selectedDay = if (showToday) EntryDay.Today else EntryDay.Yesterday
                    )
                )
            }

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
        }
    }

    private fun updateCurrentDayRecord(newRecord: Record) {
        when (currentState.selectedDay) {
            EntryDay.Today -> setState(currentState.copy(todayRecord = newRecord))
            EntryDay.Yesterday -> setState(currentState.copy(yesterdayRecord = newRecord))
        }
    }
}

data class RecordMainState(
    val todayRecord: Record? = null,
    val yesterdayRecord: Record? = null,
    val selectedDay: EntryDay = EntryDay.Today
) : UiState

sealed class RecordMainEvent : UiEvent {
    data object InitState : RecordMainEvent()
    data class OnClickDayToggle(val selected: EntryDay) : RecordMainEvent()
    data class OnClickNoConsumptionCheckBox(val checked: Boolean) : RecordMainEvent()
}

sealed class RecordMainEffect : UiEffect