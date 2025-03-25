package com.gowoon.statistics

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.statistics.navigation.StatisticsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<StatisticsState, StatisticsEvent, StatisticsEffect>() {
    private val year = savedStateHandle.toRoute<StatisticsNavigation>().year
    private val month = savedStateHandle.toRoute<StatisticsNavigation>().month
    override fun createInitialState(): StatisticsState = StatisticsState()

    init {
        initialState()
    }

    override fun handleEvent(event: StatisticsEvent) {
    }

    private fun initialState(){
        setState(currentState.copy(year = year, month = month))
    }
}

data class StatisticsState(
    val year: String = LocalDate.now().year.toString().takeLast(2),
    val month: Int = LocalDate.now().monthValue
) : UiState
sealed interface StatisticsEvent : UiEvent
sealed interface StatisticsEffect : UiEffect