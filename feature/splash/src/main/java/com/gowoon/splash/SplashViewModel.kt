package com.gowoon.splash

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.ShowOnBoardingUseCase
import com.gowoon.domain.usecase.user.GetRegistrationStateUseCase
import com.gowoon.domain.usecase.user.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val showOnBoardingUseCase: ShowOnBoardingUseCase,
    private val getRegistrationStateUseCase: GetRegistrationStateUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
) : BaseViewModel<SplashState, SplashEvent, SplashEffect>() {
    override fun createInitialState() = SplashState()

    init {
        registerUser()
        initialState()
    }

    override fun handleEvent(event: SplashEvent) {}

    private fun initialState() {
        viewModelScope.launch {
            when (val result = showOnBoardingUseCase()) {
                is Result.Success -> {
                    val route = if (result.data) {
                        Route.OnBoarding
                    } else {
                        Route.Home
                    }
                    setState(currentState.copy(nextRoute = route))
                }

                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            when (val state = getRegistrationStateUseCase()) {
                is Result.Success -> {
                    if (!state.data) {
                        when (val result = registerUserUseCase()) {
                            is Result.Success -> {
                                setState(currentState.copy(registerSucceed = true))
                            }

                            is Result.Error -> {
                                // TODO error handling
                            }
                        }
                    } else {
                        setState(currentState.copy(registerSucceed = true))
                    }
                }

                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }
}

enum class Route { Home, OnBoarding }
data class SplashState(val nextRoute: Route? = null, val registerSucceed: Boolean = false) : UiState
sealed interface SplashEvent : UiEvent
sealed interface SplashEffect : UiEffect