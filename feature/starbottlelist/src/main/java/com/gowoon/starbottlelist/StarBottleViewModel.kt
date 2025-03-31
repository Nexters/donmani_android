package com.gowoon.starbottlelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.model.record.BottleState
import com.gowoon.model.record.Record
import com.gowoon.starbottlelist.navigation.StarBottleNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StarBottleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecordListUseCase: GetRecordListUseCase
) : BaseViewModel<StarBottleState, StarBottleEvent, StarBottleEffect>() {
    private val month = savedStateHandle.toRoute<StarBottleNavigationRoute>().month
    private val bottleState = savedStateHandle.toRoute<StarBottleNavigationRoute>().state

    init {
        initialState()
    }

    override fun createInitialState() =
        StarBottleState(year = LocalDate.now().year)

    override fun handleEvent(event: StarBottleEvent) {}

    private fun initialState() {
        viewModelScope.launch {
            if (bottleState == BottleState.OPENED::class.simpleName) {
                getRecordListUseCase(
                    year = currentState.year,
                    month = month
                ).stateIn(this).collect {
                    when (val result = it) {
                        is Result.Success -> {
                            val records = result.data.filterNotNull()
                            setState(
                                currentState.copy(
                                    month = month,
                                    records = records
                                )
                            )
                        }

                        is Result.Error -> {
                            Napier.d("gowoon collect failure")
                            // TODO error handling
                        }
                    }

                }
            } else {
                setState(currentState.copy(month = month))
            }
        }
    }
}

data class StarBottleState(
    val year: Int,
    val month: Int? = null,
    val records: List<Record> = listOf()
) : UiState

sealed interface StarBottleEvent : UiEvent
sealed interface StarBottleEffect : UiEffect