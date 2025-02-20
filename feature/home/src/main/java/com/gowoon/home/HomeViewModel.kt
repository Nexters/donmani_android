package com.gowoon.home

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.domain.usecase.user.GetRegistrationStateUseCase
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import com.gowoon.domain.usecase.user.RegisterUserUseCase
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
    private val getRegistrationStateUseCase: GetRegistrationStateUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUserNicknameUseCase: GetUserNicknameUseCase,
    private val getRecordListUseCase: GetRecordListUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    override fun createInitialState(): HomeState {
        return HomeState()
    }

    init {
        registerUser()
        initialState()
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.HideTooltip -> {
                setState(currentState.copy(showTooltip = false))
            }

            is HomeEvent.OnAddRecord -> {
                setState(currentState.copy(newRecord = event.newRecord))
                setEffect(HomeEffect.RefreshTrigger)
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            when (val state = getRegistrationStateUseCase()) {
                is Result.Success -> {
                    if (!state.data) {
                        if (registerUserUseCase() is Result.Error) {
                            // TODO error handling
                        }
                    }
                }

                is Result.Error -> {
                    // TODO error handling
                }
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
                .flatMapLatest { getRecordListUseCase() }.stateIn(this).collect {
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
    }

    private fun hasRecordOfDay(records: List<Record>, date: LocalDate): Boolean {
        return records.any { record -> record.date == date }
    }
}

data class HomeState(
    val nickname: String = "",
    val records: List<Record> = listOf(),
    val newRecord: Record? = null,
    val hasToday: Boolean = false,
    val hasYesterday: Boolean = false,
    val showTooltip: Boolean = true
) : UiState

sealed interface HomeEvent : UiEvent {
    data object HideTooltip : HomeEvent
    data class OnAddRecord(val newRecord: Record?) : HomeEvent
}

sealed interface HomeEffect : UiEffect {
    data object RefreshTrigger : HomeEffect
}

