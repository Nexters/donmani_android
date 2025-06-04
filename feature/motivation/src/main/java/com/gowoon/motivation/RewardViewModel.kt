package com.gowoon.motivation

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.reward.GetFeedbackSummaryUseCase
import com.gowoon.domain.usecase.reward.GetFeedbackUseCase
import com.gowoon.domain.usecase.reward.GetGiftCountUseCase
import com.gowoon.domain.usecase.reward.HideRewardFirstBottomSheetUseCase
import com.gowoon.domain.usecase.reward.ShowRewardFirstOpenBottomSheetUseCase
import com.gowoon.model.reward.Gift
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val showRewardFirstOpenBottomSheetUseCase: ShowRewardFirstOpenBottomSheetUseCase,
    private val hideRewardFirstBottomSheetUseCase: HideRewardFirstBottomSheetUseCase,
    private val getFeedbackSummaryUseCase: GetFeedbackSummaryUseCase,
    private val getFeedbackUseCase: GetFeedbackUseCase,
    private val getGiftCountUseCase: GetGiftCountUseCase
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

            RewardEvent.HideFirstBottomSheet -> {
                hideBottomSheet()
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getFeedbackSummaryUseCase().combine(showRewardFirstOpenBottomSheetUseCase()) { feedback, show ->
                if (feedback is Result.Success && show is Result.Success) {
                    Result.Success(
                        currentState.copy(
                            step = Step.Main(
                                getMainState(
                                    feedback.data.third,
                                    feedback.data.first
                                )
                            ),
                            dayStreakCount = feedback.data.third,
                            showFirstBottomSheet = show.data
                        )
                    )
                } else {
                    Result.Error(message = (feedback as? Result.Error)?.message + (show as? Result.Error)?.message)
                }
            }.collect { state ->
                when (state) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(state.data)
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
                requestFeedback()
            }

            is Step.Feedback -> {
                requestGiftCount()
            }

            is Step.GiftOpen -> {
                requestGiftList()
            }

            else -> {}
        }
    }

    private fun requestFeedback() {
        viewModelScope.launch {
            getFeedbackUseCase().collect { result ->
                when (result) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(currentState.copy(step = Step.Feedback(result.data)))
                    }
                }
            }
        }
    }

    private fun requestGiftCount() {
        viewModelScope.launch {
            getGiftCountUseCase().collect { result ->
                when (result) {
                    is Result.Error -> {
                        // TODO error handling
                    }

                    is Result.Success -> {
                        setState(currentState.copy(step = Step.GiftOpen(result.data)))
                    }
                }
            }
        }
    }

    private fun requestGiftList() {
        setState(currentState.copy(step = Step.GiftConfirm(listOf())))
    }

    private fun hideBottomSheet() {
        viewModelScope.launch {
            if (hideRewardFirstBottomSheetUseCase() is Result.Error) {
                // TODO error handling
            }
        }
    }
}

data class RewardState(
    val step: Step? = null,
    val dayStreakCount: Int = 0,
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
    data object HideFirstBottomSheet : RewardEvent
}

sealed interface RewardEffect : UiEffect