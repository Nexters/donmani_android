package com.gowoon.statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetCategoryStatisticsUseCase
import com.gowoon.model.record.Category
import com.gowoon.statistics.navigation.StatisticsNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoryStatisticsUseCase: GetCategoryStatisticsUseCase
) : BaseViewModel<StatisticsState, StatisticsEvent, StatisticsEffect>() {
    private val year = savedStateHandle.toRoute<StatisticsNavigation>().year
    private val month = savedStateHandle.toRoute<StatisticsNavigation>().month
    override fun createInitialState(): StatisticsState = StatisticsState()

    init {
        initialState()
    }

    override fun handleEvent(event: StatisticsEvent) {
    }

    private fun initialState() {
        setState(currentState.copy(year = year, month = month))
        viewModelScope.launch {
            getCategoryStatisticsUseCase(year = year, month = month).stateIn(this).collect {
                when (it) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(currentState.copy(categoryCounts = it.data))
                    }
                }
            }
        }
    }
}

data class StatisticsState(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val categoryCounts: Map<Category, Int> = mapOf()
) : UiState

sealed interface StatisticsEvent : UiEvent
sealed interface StatisticsEffect : UiEffect