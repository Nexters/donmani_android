package com.gowoon.setting

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.user.GetNoticeStatusUseCase
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import com.gowoon.domain.usecase.user.UpdateNoticeStatusUseCase
import com.gowoon.domain.usecase.user.UpdateUserNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserNicknameUseCase: GetUserNicknameUseCase,
    private val updateUserNicknameUseCase: UpdateUserNicknameUseCase,
    private val getNoticeStatusUseCase: GetNoticeStatusUseCase,
    private val updateNoticeStatusUseCase: UpdateNoticeStatusUseCase
) : BaseViewModel<SettingState, SettingEvent, SettingEffect>() {
    override fun createInitialState(): SettingState = SettingState()

    init {
        initialState()
    }

    override fun handleEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.ShowDialog -> {
                setState(currentState.copy(dialogState = event.type))
            }

            is SettingEvent.OnChangeNickName -> {
                updateUserNickname(event.nickname, event.callback)
            }

            is SettingEvent.UpdateNoticeStatusAsRead -> {
                updateNoticeStatus()
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
        viewModelScope.launch {
            when (val result = getNoticeStatusUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(newNotice = result.data))
                }

                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }

    private fun updateUserNickname(nickname: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            when (updateUserNicknameUseCase(nickname)) {
                is Result.Error -> {
                    // TODO error handling
                    callback(false)
                }

                is Result.Success -> {
                    callback(true)
                }
            }
        }
    }

    private fun updateNoticeStatus() {
        viewModelScope.launch {
            when (updateNoticeStatusUseCase()) {
                is Result.Success -> {
                    setState(currentState.copy(newNotice = false))
                }

                is Result.Error -> {
                    // TODO error handling
                }
            }
        }
    }

    fun showToast(message: String) {
        setEffect(SettingEffect.ShowToast(message))
    }
}

data class SettingState(
    val nickname: String = "",
    val dialogState: SettingDialogType? = null,
    val newNotice: Boolean = false
) : UiState

enum class SettingDialogType { BBS_RULE, EDIT_NICKNAME }

sealed interface SettingEvent : UiEvent {
    data class ShowDialog(val type: SettingDialogType?) : SettingEvent
    data class OnChangeNickName(val nickname: String, val callback: (Boolean) -> Unit) :
        SettingEvent

    data object UpdateNoticeStatusAsRead : SettingEvent
}

sealed interface SettingEffect : UiEffect {
    data class ShowToast(val message: String) : SettingEffect
}