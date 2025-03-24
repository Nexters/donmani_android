package com.gowoon.onboarding.component

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.HideOnBoardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val hideOnBoardingUseCase: HideOnBoardingUseCase
) : BaseViewModel<OnBoardingState, OnBoardingEvent, OnBoardingEffect>() {
    override fun createInitialState(): OnBoardingState = OnBoardingState()

    init {
        initCollector()
    }

    override fun handleEvent(event: OnBoardingEvent) {
        when (event) {
            OnBoardingEvent.GoToGuide -> {
                setState(currentState.copy(step = Step.GUIDE))
            }

            is OnBoardingEvent.ShowBottomSheet -> {
                setState(currentState.copy(showBottomSheet = event.state))
                if (event.state) {
                    setEffect(OnBoardingEffect.HideOnBoarding)
                }
            }

            is OnBoardingEvent.UpdateNextRoute -> {
                setState(currentState.copy(route = event.route))
            }
        }
    }

    private fun initCollector() {
        viewModelScope.launch {
            uiEffect.collect {
                if (it is OnBoardingEffect.HideOnBoarding) {
                    if (hideOnBoardingUseCase() is Result.Error) {
                        // TODO error handling
                    }
                }
            }
        }
    }
}

enum class Step { INTRO, GUIDE }
enum class Route { HOME, RECORD }

data class OnBoardingState(
    val step: Step = Step.INTRO,
    val route: Route? = null,
    val showBottomSheet: Boolean = false
) : UiState

sealed interface OnBoardingEvent : UiEvent {
    data object GoToGuide : OnBoardingEvent
    data class ShowBottomSheet(val state: Boolean) : OnBoardingEvent
    data class UpdateNextRoute(val route: Route) : OnBoardingEvent
}

sealed interface OnBoardingEffect : UiEffect {
    data object HideOnBoarding : OnBoardingEffect
}