package com.gowoon.recordlist

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.model.record.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val getRecordListUseCase: GetRecordListUseCase
) : BaseViewModel<RecordListState, RecordListEvent, RecordListEffect>() {
    override fun createInitialState(): RecordListState = RecordListState()

    init {
        inititalState()
    }

    override fun handleEvent(event: RecordListEvent) {}

    private fun inititalState() {
        viewModelScope.launch {
            uiEffect.filter { it is RecordListEffect.RefreshTrigger }
                .flatMapLatest { getRecordListUseCase() }.stateIn(this).collect {
                    when (val result = it) {
                        is Result.Success -> {
                            setState(
                                currentState.copy(
                                    records = result.data.filterNotNull()
                                )
                            )
                        }

                        is Result.Error -> {
                            // TODO error handling
                        }
                    }

                }
        }.also { setEffect(RecordListEffect.RefreshTrigger) }
    }
}

data class RecordListState(
    val records: List<Record> = listOf()
) : UiState

sealed interface RecordListEvent : UiEvent
sealed interface RecordListEffect : UiEffect {
    data object RefreshTrigger : RecordListEffect
}