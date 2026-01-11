package com.gowoon.starbottlelist

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.HideStarBottleListBannerUseCase
import com.gowoon.domain.usecase.config.ShowStarBottleListBannerUseCase
import com.gowoon.domain.usecase.record.GetMonthlySummaryUseCase
import com.gowoon.model.record.BottleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarBottleListViewModel @Inject constructor(
    private val showStarBottleListBannerUseCase: ShowStarBottleListBannerUseCase,
    private val hideStarBottleListBannerUseCase: HideStarBottleListBannerUseCase,
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase
) : BaseViewModel<StarBottleListState, StarBottleListEvent, StarBottleListEffect>() {
    override fun createInitialState(): StarBottleListState = StarBottleListState()

    init {
        initialState()
    }

    override fun handleEvent(event: StarBottleListEvent) {
        when (event) {
            is StarBottleListEvent.HideBanner -> hideBanner()
            is StarBottleListEvent.ShowYearPicker -> {
                setState(currentState.copy(showYearPickerBottomSheet = event.show))
            }

            is StarBottleListEvent.ChangeCurrentYear -> {
                setState(currentState.copy(selectedYear = event.year))
            }

            is StarBottleListEvent.FetchMonthlySummaryData -> {
                if (!currentState.monthlySummaryList.containsKey(event.year)) {
                    setEffect(StarBottleListEffect.RefreshTrigger(event.year))
                }
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            showStarBottleListBannerUseCase().collect {
                when (val result = it) {
                    is Result.Success -> {
                        setState(currentState.copy(showBanner = result.data))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
        viewModelScope.launch {
            uiEffect.filterIsInstance<StarBottleListEffect.RefreshTrigger>()
                .flatMapMerge { effect ->
                    getMonthlySummaryUseCase(effect.year).map { Pair(effect.year, it) }
                }
                .stateIn(this).collect { data ->
                    when (val result = data.second) {
                        is Result.Success -> {
                            val lastMonth = result.data.last().month
                            val summaryList = result.data.map {
                                Pair(
                                    it.month,
                                    BottleState.OPENED(
                                        count = it.recordCount,
                                        total = it.totalDaysInMonth
                                    )
                                )
//                                val date = LocalDate.now()
//                                if (data.first > date.year || (data.first == date.year && it.month > date.monthValue)) {
//                                    Pair(
//                                        it.month,
//                                        BottleState.LOCKED
//                                    )
//                                } else {
//                                    Pair(
//                                        it.month,
//                                        BottleState.OPENED(
//                                            count = it.recordCount,
//                                            total = it.totalDaysInMonth
//                                        )
//                                    )
//                                }
                            }.toMutableList<Pair<Int, BottleState>>().apply {
                                for (i in lastMonth + 1..12) {
                                    add(Pair(i, BottleState.LOCKED))
                                }

                            }
                            setState(
                                currentState.copy(
                                    monthlySummaryList = currentState.monthlySummaryList + (data.first to summaryList)
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

    private fun hideBanner() {
        viewModelScope.launch {
            when (hideStarBottleListBannerUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(showBanner = false))
                }

                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }

    fun showToast(message: String) {
        setEffect(StarBottleListEffect.ShowToast(message))
    }
}

data class StarBottleListState(
    val showBanner: Boolean = false,
    val showYearPickerBottomSheet: Boolean = false,
    val yearList: List<Int> = listOf(2025, 2026)
    //(2025..LocalDate.now().year).toList()
    ,
    val selectedYear: Int = yearList.last(),
    val monthlySummaryList: Map<Int, List<Pair<Int, BottleState>>> = mapOf()
) : UiState

sealed interface StarBottleListEvent : UiEvent {
    data object HideBanner : StarBottleListEvent
    data class ShowYearPicker(val show: Boolean) : StarBottleListEvent
    data class ChangeCurrentYear(val year: Int) : StarBottleListEvent
    data class FetchMonthlySummaryData(val year: Int) : StarBottleListEvent
}

sealed interface StarBottleListEffect : UiEffect {
    data class ShowToast(val message: String) : StarBottleListEffect
    data class RefreshTrigger(val year: Int) : StarBottleListEffect
}