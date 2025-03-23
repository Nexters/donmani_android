package com.gowoon.splash

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.ShowOnBoardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val showOnBoardingUseCase: ShowOnBoardingUseCase
) : BaseViewModel<SplashState, SplashEvent, SplashEffect>() {
    override fun createInitialState() = SplashState()

    init {
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
}

enum class Route { Home, OnBoarding }
data class SplashState(val nextRoute: Route? = null) : UiState
sealed interface SplashEvent : UiEvent
sealed interface SplashEffect : UiEffect