package com.gowoon.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.HideStarBottleOpenSheetUseCase
import com.gowoon.domain.usecase.config.HideYesterdayTooltipUseCase
import com.gowoon.domain.usecase.config.ShowStarBottleOpenSheetUseCase
import com.gowoon.domain.usecase.config.ShowYesterdayTooltipUseCase
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.domain.usecase.reward.GetRewardReceivedTooltipStateUseCase
import com.gowoon.domain.usecase.reward.HideRewardReceivedTooltipUseCase
import com.gowoon.domain.usecase.reward.ShowRewardReceivedTooltipUseCase
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import com.gowoon.home.navigation.HomeNavigationRoute
import com.gowoon.model.record.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserNicknameUseCase: GetUserNicknameUseCase,
    private val getRecordListUseCase: GetRecordListUseCase,
    private val showYesterdayTooltipUseCase: ShowYesterdayTooltipUseCase,
    private val hideYesterdayTooltipUseCase: HideYesterdayTooltipUseCase,
    private val showStarBottleOpenSheetUseCase: ShowStarBottleOpenSheetUseCase,
    private val hideStarBottleOpenSheetUseCase: HideStarBottleOpenSheetUseCase,
    private val getRewardReceivedTooltipStateUseCase: GetRewardReceivedTooltipStateUseCase,
    private val hideRewardReceivedTooltipUseCase: HideRewardReceivedTooltipUseCase,
    private val showRewardReceivedTooltipUseCase: ShowRewardReceivedTooltipUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    private val _referrer =
        MutableStateFlow(Pair(false, savedStateHandle.toRoute<HomeNavigationRoute>().referrer))
    val referrer = _referrer.asStateFlow()

    private val _isFromFcm =
        MutableStateFlow(Pair(false, savedStateHandle.toRoute<HomeNavigationRoute>().isFromFcm))
    val isFromFcm = _isFromFcm.asStateFlow()

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

            is HomeEvent.UpdateRewardTooltipState -> {

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
                .flatMapLatest { getRecordListUseCase(currentState.year, currentState.month) }
                .stateIn(this).collect {
                    when (val result = it) {
                        is Result.Success -> {
                            val records = result.data.records.filterNotNull()
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
        viewModelScope.launch {
            getRewardReceivedTooltipStateUseCase().stateIn(this).collect {
                when (it) {
                    is Result.Success -> {
                        setState(currentState.copy(showRewardTooltip = it.data))
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

    private fun updateRewardTooltip(state: Boolean) {
        viewModelScope.launch {
            if (state) {
                showRewardReceivedTooltipUseCase().let {
                    if (it is Result.Error) {
                        // TODO error handling
                    }
                }
            } else {
                hideRewardReceivedTooltipUseCase().let {
                    if (it is Result.Error) {
                        // TODO error handling
                    }
                }
            }
        }
    }

    fun sendViewMainGA4Event() {
        referrer.value.second?.let { referrerName ->
            FirebaseAnalyticsUtil.sendEvent(
                trigger = FirebaseAnalyticsUtil.EventTrigger.VIEW,
                eventName = "main",
                Pair("referrer", referrerName)
            )
        }
        _referrer.value = Pair(true, referrer.value.second)
    }

    fun updateIsFromFcmState() {
        _isFromFcm.value = Pair(true, isFromFcm.value.second)
    }
}

data class HomeState(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val nickname: String = "",
    val records: List<Record> = listOf(),
    val newRecord: Record? = null,
    val recordAdded: Boolean = false,
    val hasToday: Boolean = false,
    val hasYesterday: Boolean = false,
    val showTooltip: Boolean = true,
    val showBottomSheet: Boolean = false,
    val showRewardTooltip: Boolean = false
) : UiState

sealed interface HomeEvent : UiEvent {
    data object HideTooltip : HomeEvent
    data class OnAddRecord(val newRecord: Record?, val recordAdded: Boolean) : HomeEvent
    data object HideBottomSheet : HomeEvent
    data class UpdateRewardTooltipState(val state: Boolean) : HomeEvent
}

sealed interface HomeEffect : UiEffect {
    data object RefreshTrigger : HomeEffect
}

