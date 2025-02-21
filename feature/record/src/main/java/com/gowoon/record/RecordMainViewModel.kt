package com.gowoon.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.SaveRecordUseCase
import com.gowoon.domain.usecase.tooltip.HideBBSRuleSheetUseCase
import com.gowoon.domain.usecase.tooltip.HideNoConsumptionTooltipUseCase
import com.gowoon.domain.usecase.tooltip.ShowBBSRuleSheetUseCase
import com.gowoon.domain.usecase.tooltip.ShowNoConsumptionTooltipUseCase
import com.gowoon.model.common.EntryDay
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.Record
import com.gowoon.model.record.Record.ConsumptionRecord
import com.gowoon.model.record.Record.NoConsumption
import com.gowoon.record.navigation.RecordNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class RecordMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val showNoConsumptionTooltipUseCase: ShowNoConsumptionTooltipUseCase,
    private val hideNoConsumptionTooltipUseCase: HideNoConsumptionTooltipUseCase,
    private val showBBSRuleSheetUseCase: ShowBBSRuleSheetUseCase,
    private val hideBBSRuleSheetUseCase: HideBBSRuleSheetUseCase,
    private val saveRecordUseCase: SaveRecordUseCase
) : BaseViewModel<RecordMainState, RecordMainEvent, RecordMainEffect>() {
    override fun createInitialState(): RecordMainState = RecordMainState()
    private val showToday = savedStateHandle.toRoute<RecordNavigationRoute>().hasTodayRecord.not()
    private val showYesterday =
        savedStateHandle.toRoute<RecordNavigationRoute>().hasYesterdayRecord.not()

    init {
        initialState()
    }

    override fun handleEvent(event: RecordMainEvent) {
        when (event) {
            is RecordMainEvent.OnClickDayToggle -> {
                setState(currentState.copy(selectedDay = event.selected))
            }

            is RecordMainEvent.OnClickNoConsumptionCheckBox -> {
                val newRecord = if (event.checked) {
                    NoConsumption(consumptionDate = getDate())
                } else {
                    ConsumptionRecord(consumptionDate = getDate())
                }
                updateCurrentDayRecord(newRecord = newRecord)
            }

            is RecordMainEvent.OnClickNoConsumptionTooltip -> {
                hideTooltip()
            }

            is RecordMainEvent.OnChangedConsumption -> {
                updateConsumption(event.consumption)
            }

            is RecordMainEvent.ShowConfirm -> {
                setState(currentState.copy(showConfirm = event.show))
            }

            is RecordMainEvent.OnSaveRecord -> {
                requestSaveRecord(event.record, event.callback)
            }

            is RecordMainEvent.HideBBSRuleSheet -> {
                hideBBSRuleSheet()
            }
        }
    }

    private fun initialState() {
        setState(
            currentState.copy(
                records = mutableMapOf<String, Record>().apply {
                    if (showToday) {
                        put(
                            EntryDay.Today.name,
                            ConsumptionRecord(
                                consumptionDate = LocalDate.now()
                            )
                        )
                    }
                    if (showYesterday) {
                        put(
                            EntryDay.Yesterday.name,
                            ConsumptionRecord(
                                consumptionDate = LocalDate.now().minusDays(1)
                            )
                        )
                    }
                },
                selectedDay = if (showToday) EntryDay.Today else EntryDay.Yesterday,
            )
        )
        viewModelScope.launch {
            showNoConsumptionTooltipUseCase().stateIn(this).collect {
                when (it) {
                    is Result.Success -> {
                        setState(currentState.copy(showTooltip = it.data))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
        viewModelScope.launch {
            showBBSRuleSheetUseCase().stateIn(this).collect {
                when (it) {
                    is Result.Success -> {
                        setState(currentState.copy(showRuleBottomSheet = it.data))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
    }

    private fun updateCurrentDayRecord(newRecord: Record) {
        setState(
            currentState.copy(
                records = currentState.records.toMutableMap().apply {
                    put(currentState.selectedDay.name, newRecord)
                }
            )
        )
    }

    private fun updateConsumption(newConsumption: Consumption) {
        (currentState.records[currentState.selectedDay.name] as? ConsumptionRecord)?.let { originRecord ->
            setState(
                currentState.copy(
                    records = currentState.records.toMutableMap().apply {
                        put(
                            currentState.selectedDay.name,
                            when (newConsumption.type) {
                                ConsumptionType.GOOD -> {
                                    originRecord.copy(goodRecord = newConsumption)
                                }

                                ConsumptionType.BAD -> {
                                    originRecord.copy(badRecord = newConsumption)
                                }
                            }
                        )
                    }
                )
            )
        }
    }

    private fun hideTooltip() {
        viewModelScope.launch {
            if (hideNoConsumptionTooltipUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }

    private fun hideBBSRuleSheet() {
        viewModelScope.launch {
            if (hideBBSRuleSheetUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }

    private fun requestSaveRecord(record: Record, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            when (val result = saveRecordUseCase(record)) {
                is Result.Success -> {
                    Napier.d("gowoon log register success")
                    callback(true)
                }

                is Result.Error -> {
                    // TODO error handling
                    Napier.d("gowoon log register error $result")
                    callback(false)
                }
            }
        }
    }

    private fun getDate(): LocalDate {
        return when (currentState.selectedDay) {
            EntryDay.Today -> LocalDate.now()
            EntryDay.Yesterday -> LocalDate.now().minusDays(1)
        }
    }
}

data class RecordMainState(
    val records: Map<String, Record> = mapOf(),
    val selectedDay: EntryDay = EntryDay.Today,
    val showTooltip: Boolean = false,
    val showRuleBottomSheet: Boolean = false,
    val showConfirm: Boolean = false
) : UiState

sealed interface RecordMainEvent : UiEvent {
    data class OnClickDayToggle(val selected: EntryDay) : RecordMainEvent
    data class OnClickNoConsumptionCheckBox(val checked: Boolean) : RecordMainEvent
    data object OnClickNoConsumptionTooltip : RecordMainEvent
    data class OnChangedConsumption(val consumption: Consumption) : RecordMainEvent
    data class ShowConfirm(val show: Boolean) : RecordMainEvent
    data class OnSaveRecord(val record: Record, val callback: (Boolean) -> Unit) : RecordMainEvent
    data object HideBBSRuleSheet : RecordMainEvent
}

sealed class RecordMainEffect : UiEffect