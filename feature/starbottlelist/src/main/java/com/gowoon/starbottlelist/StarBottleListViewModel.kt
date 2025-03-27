package com.gowoon.starbottlelist

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.config.HideStarBottleListBannerUseCase
import com.gowoon.domain.usecase.config.ShowStarBottleListBannerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarBottleListViewModel @Inject constructor(
    private val showStarBottleListBannerUseCase: ShowStarBottleListBannerUseCase,
    private val hideStarBottleListBannerUseCase: HideStarBottleListBannerUseCase
) : BaseViewModel<StarBottleListState, StarBottleListEvent, StarBottleListEffect>() {
    override fun createInitialState(): StarBottleListState = StarBottleListState()

    init {
        initialState()
    }

    override fun handleEvent(event: StarBottleListEvent) {
        when(event){
            StarBottleListEvent.HideBanner -> hideBanner()
        }
    }

    private fun initialState(){
        viewModelScope.launch {
            showStarBottleListBannerUseCase().collect {
                when(val result = it){
                    is Result.Success -> {
                        setState(currentState.copy(showBanner = result.data))
                    }
                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
    }

    private fun hideBanner(){
        viewModelScope.launch {
            when(hideStarBottleListBannerUseCase()){
                is Result.Success -> {
                    setState(currentState.copy(showBanner = false))
                }
                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }
}

data class StarBottleListState(
    val showBanner: Boolean = false
) : UiState

sealed interface StarBottleListEvent : UiEvent {
    data object HideBanner : StarBottleListEvent
}
sealed interface StarBottleListEffect: UiEffect