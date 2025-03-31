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
import dagger.hilt.android.lifecycle.HiltViewModel
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
            StarBottleListEvent.HideBanner -> hideBanner()
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
            getMonthlySummaryUseCase().collect {
                when (val result = it) {
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
                        }.toMutableList<Pair<Int, BottleState>>().apply {
                            for (i in lastMonth + 1..12) {
                                add(Pair(i, BottleState.LOCKED))
                            }
                        }
                        setState(currentState.copy(monthlySummaryList = summaryList))
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
}

sealed class BottleState {
    data class OPENED(val count: Int, val total: Int) : BottleState()
    data object LOCKED : BottleState()
}

data class StarBottleListState(
    val showBanner: Boolean = false,
    val monthlySummaryList: List<Pair<Int, BottleState>> = listOf()
) : UiState

sealed interface StarBottleListEvent : UiEvent {
    data object HideBanner : StarBottleListEvent
}

sealed interface StarBottleListEffect : UiEffect