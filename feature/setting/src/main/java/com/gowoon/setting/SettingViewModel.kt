package com.gowoon.setting

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserNicknameUseCase: GetUserNicknameUseCase
) : BaseViewModel<SettingState, SettingEvent, SettingEffect>() {
    override fun createInitialState(): SettingState = SettingState()

    init {
        initialState()
    }

    override fun handleEvent(event: SettingEvent) {
        when(event){
            is SettingEvent.ShowDialog -> {
                setState(currentState.copy(showDialog = event.show))
            }
        }
    }

    private fun initialState() {
        viewModelScope.launch {
            getUserNicknameUseCase().stateIn(this).collect {
                when (val result = it) {
                    is Result.Success -> {
                        setState(currentState.copy(nickname = result.data ?: ""))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
    }

}

data class SettingState(
    val nickname: String = "",
    val showDialog: Boolean = false
) : UiState

sealed interface SettingEvent : UiEvent {
    data class ShowDialog(val show: Boolean): SettingEvent
}
sealed interface SettingEffect : UiEffect