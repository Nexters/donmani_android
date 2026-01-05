package com.gowoon.starbottlelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.record.GetRecordListUseCase
import com.gowoon.model.common.BBSState
import com.gowoon.model.record.BottleState
import com.gowoon.starbottlelist.navigation.StarBottleNavigationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarBottleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecordListUseCase: GetRecordListUseCase,
//    private val getBgmStateUseCase: GetBgmStateUseCase
) : BaseViewModel<StarBottleState, StarBottleEvent, StarBottleEffect>() {
    private val year = savedStateHandle.toRoute<StarBottleNavigationRoute>().year
    private val month = savedStateHandle.toRoute<StarBottleNavigationRoute>().month
    private val bottleState = savedStateHandle.toRoute<StarBottleNavigationRoute>().state

    init {
        initialState()
    }

    override fun createInitialState() = StarBottleState()

    override fun handleEvent(event: StarBottleEvent) {}

    private fun initialState() {
        viewModelScope.launch {
            if (bottleState == BottleState.OPENED::class.simpleName) {
                getRecordListUseCase(
                    year = year,
                    month = month
                ).stateIn(this).collect {
                    when (val result = it) {
                        is Result.Success -> {
                            setState(
                                currentState.copy(
                                    year = year,
                                    month = month,
                                    bbsState = result.data
                                )
                            )
                        }

                        is Result.Error -> {
                            Napier.d("gowoon collect failure")
                            // TODO error handling
                        }
                    }

                }
            } else {
                setState(currentState.copy(year = year, month = month))
            }
        }
//        viewModelScope.launch {
//            getBgmStateUseCase().stateIn(this).collect {
//                when (it) {
//                    is Result.Error -> {
//                        // TODO error handling
//                    }
//
//                    is Result.Success -> {
//                        setState(currentState.copy(bgmPlayOn = it.data))
//                    }
//                }
//            }
//        }
    }
}

data class StarBottleState(
    val year: Int? = null,
    val month: Int? = null,
    val bbsState: BBSState = BBSState(),
//    val bgmPlayOn: Boolean = false
) : UiState

sealed interface StarBottleEvent : UiEvent
sealed interface StarBottleEffect : UiEffect