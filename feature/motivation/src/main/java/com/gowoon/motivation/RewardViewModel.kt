package com.gowoon.motivation

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.reward.GetFeedbackSummaryUseCase
import com.gowoon.model.reward.Gift
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val getFeedbackSummaryUseCase: GetFeedbackSummaryUseCase
) : BaseViewModel<RewardState, RewardEvent, RewardEffect>() {
    override fun createInitialState(): RewardState {
        return RewardState()
    }

    init {
        initialState()
    }

    override fun handleEvent(event: RewardEvent) {
        when (event) {
            is RewardEvent.GoToNextStep -> {
                setNextStep()
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getFeedbackSummaryUseCase().collect { result ->
                when (result) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(
                            currentState.copy(
                                step = Step.Main(
                                    getMainState(
                                        result.data.third,
                                        result.data.first
                                    )
                                ),
                                dayStreakCount = result.data.third,
                                showFirstBottomSheet = result.data.first && result.data.second
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMainState(dayStreakCount: Int, hasNotOpened: Boolean): MainState {
        return if (dayStreakCount == 0) {
            MainState.NO_RECORD
        } else {
            if (hasNotOpened) {
                MainState.AVAILABLE_GIFT
            } else {
                if (dayStreakCount >= 14) {
                    MainState.DONE
                } else {
                    MainState.NO_AVAILABLE_GIFT
                }
            }
        }
    }

    private fun setNextStep() {
        when (currentState.step) {
            is Step.Main -> {
                // TODO post feedback
                // setState(currentState.copy(step = Step.Feedback()))
            }

            is Step.Feedback -> TODO()
            is Step.GiftConfirm -> TODO()
            is Step.GiftOpen -> TODO()
            null -> {}
        }
    }
}

data class RewardState(
    val step: Step? = null,
    val dayStreakCount: Int = 0,
    val giftList: List<Gift> = listOf(),
    val showFirstBottomSheet: Boolean = false
) : UiState

sealed interface Step {
    data class Main(val state: MainState) : Step
    data class Feedback(val feedback: com.gowoon.model.reward.Feedback) : Step
    data class GiftOpen(val giftCount: Int) : Step
    data class GiftConfirm(val giftList: List<Gift>) : Step
}

enum class MainState {
    NO_RECORD, NO_AVAILABLE_GIFT, AVAILABLE_GIFT, DONE
}

sealed interface RewardEvent : UiEvent {
    data object GoToNextStep : RewardEvent
}

sealed interface RewardEffect : UiEffect