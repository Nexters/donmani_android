package com.gowoon.recordlist

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.model.record.Record
import com.gowoon.recordlist.navigation.RecordListNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val json: Json,
) : BaseViewModel<RecordListState, RecordListEvent, RecordListEffect>() {
    override fun createInitialState(): RecordListState = RecordListState()
    private val recordList = savedStateHandle.toRoute<RecordListNavigationRoute>().records

    init {
        initialState()
    }

    override fun handleEvent(event: RecordListEvent) {}

    private fun initialState() {
        setState(currentState.copy(records = json.decodeFromString<List<Record>>(recordList)))
    }
}

data class RecordListState(
    val records: List<Record> = listOf()
) : UiState

sealed interface RecordListEvent : UiEvent
sealed interface RecordListEffect : UiEffect