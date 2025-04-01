package com.gowoon.recordlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.HideStarBottleListTooltipUseCase
import com.gowoon.domain.usecase.config.ShowStarBottleListTooltipUseCase
import com.gowoon.model.record.Record
import com.gowoon.recordlist.navigation.RecordListNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val json: Json,
    private val showStarBottleListTooltipUseCase: ShowStarBottleListTooltipUseCase,
    private val hideStarBottleListTooltipUseCase: HideStarBottleListTooltipUseCase
) : BaseViewModel<RecordListState, RecordListEvent, RecordListEffect>() {
    override fun createInitialState(): RecordListState = RecordListState()
    private val recordList = savedStateHandle.toRoute<RecordListNavigationRoute>().records
    private val year = savedStateHandle.toRoute<RecordListNavigationRoute>().year
    private val month = savedStateHandle.toRoute<RecordListNavigationRoute>().month

    init {
        initialState()
    }

    override fun handleEvent(event: RecordListEvent) {
        when (event) {
            RecordListEvent.HideTooltip -> hideTooltip()
        }
    }

    private fun initialState() {
        setState(
            currentState.copy(
                records = json.decodeFromString<List<Record>>(recordList),
                year = year,
                month = month
            )
        )
        viewModelScope.launch {
            showStarBottleListTooltipUseCase().collect {
                when (val result = it) {
                    is Result.Success -> {
                        setState(currentState.copy(showTooltip = result.data))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
    }

    private fun hideTooltip() {
        viewModelScope.launch {
            when (hideStarBottleListTooltipUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(showTooltip = false))
                }

                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }
}

data class RecordListState(
    val records: List<Record> = listOf(),
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val showTooltip: Boolean = false
) : UiState

sealed interface RecordListEvent : UiEvent {
    data object HideTooltip : RecordListEvent
}

sealed interface RecordListEffect : UiEffect