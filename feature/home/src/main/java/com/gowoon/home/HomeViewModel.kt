package com.gowoon.home

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.HideStarBottleOpenSheetUseCase
import com.gowoon.domain.usecase.config.HideYesterdayTooltipUseCase
import com.gowoon.domain.usecase.config.ShowStarBottleOpenSheetUseCase
import com.gowoon.domain.usecase.config.ShowYesterdayTooltipUseCase
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import com.gowoon.model.record.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserNicknameUseCase: GetUserNicknameUseCase,
    private val getRecordListUseCase: GetRecordListUseCase,
    private val showYesterdayTooltipUseCase: ShowYesterdayTooltipUseCase,
    private val hideYesterdayTooltipUseCase: HideYesterdayTooltipUseCase,
    private val showStarBottleOpenSheetUseCase: ShowStarBottleOpenSheetUseCase,
    private val hideStarBottleOpenSheetUseCase: HideStarBottleOpenSheetUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {

    override fun createInitialState(): HomeState {
        return HomeState()
    }

    init {
        initialState()
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.HideTooltip -> {
                hideTooltip()
            }

            is HomeEvent.OnAddRecord -> {
                setState(
                    currentState.copy(
                        newRecord = event.newRecord,
                        recordAdded = event.recordAdded
                    )
                )
                setEffect(HomeEffect.RefreshTrigger)
            }

            is HomeEvent.HideBottomSheet -> {
                hideSheet()
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getUserNicknameUseCase().stateIn(this).collect {
                when (val result = it) {
                    is Result.Success -> {
                        result.data?.let { nickname ->
                            setState(currentState.copy(nickname = nickname))
                        }
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
        viewModelScope.launch {
            uiEffect.filter { it is HomeEffect.RefreshTrigger }
                .flatMapLatest {
                    val today = LocalDate.now()
                    getRecordListUseCase(today.year, today.monthValue)
                }.stateIn(this).collect {
                    when (val result = it) {
                        is Result.Success -> {
                            val records = result.data.filterNotNull()
                            setState(
                                currentState.copy(
                                    records = records,
                                    hasToday = hasRecordOfDay(records, LocalDate.now()),
                                    hasYesterday = hasRecordOfDay(
                                        records,
                                        LocalDate.now().minusDays(1)
                                    )
                                )
                            )
                        }

                        is Result.Error -> {
                            // TODO error handling
                        }
                    }

                }
        }.also { setEffect(HomeEffect.RefreshTrigger) }
        viewModelScope.launch {
            showYesterdayTooltipUseCase().collect {
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
            showStarBottleOpenSheetUseCase().stateIn(this).collect {
                when (it) {
                    is Result.Success -> {
                        setState(currentState.copy(showBottomSheet = it.data))
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
            if (hideYesterdayTooltipUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }

    private fun hideSheet() {
        viewModelScope.launch {
            if (hideStarBottleOpenSheetUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }

    private fun hasRecordOfDay(records: List<Record>, date: LocalDate): Boolean {
        return records.any { record -> record.date == date }
    }
}

data class HomeState(
    val nickname: String = "",
    val records: List<Record> = listOf(),
    val newRecord: Record? = null,
    val recordAdded: Boolean = false,
    val hasToday: Boolean = false,
    val hasYesterday: Boolean = false,
    val showTooltip: Boolean = true,
    val showBottomSheet: Boolean = false
) : UiState

sealed interface HomeEvent : UiEvent {
    data object HideTooltip : HomeEvent
    data class OnAddRecord(val newRecord: Record?, val recordAdded: Boolean) : HomeEvent
    data object HideBottomSheet : HomeEvent
}

sealed interface HomeEffect : UiEffect {
    data object RefreshTrigger : HomeEffect
}

