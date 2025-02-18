package com.gowoon.home

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.user.GetRegistrationStateUseCase
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import com.gowoon.domain.usecase.user.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRegistrationStateUseCase: GetRegistrationStateUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUserNicknameUseCase: GetUserNicknameUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    override fun createInitialState(): HomeState {
        return HomeState()
    }

    init {
        registerUser()
        initialState()
    }

    override fun handleEvent(event: HomeEvent) {
        TODO("Not yet implemented")
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
    }
}

data class HomeState(
    val nickname: String = ""
) : UiState

sealed class HomeEvent : UiEvent
sealed class HomeEffect : UiEffect

