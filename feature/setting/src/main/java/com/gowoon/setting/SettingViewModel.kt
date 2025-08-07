package com.gowoon.setting

import androidx.lifecycle.viewModelScope
import com.gowoon.common.base.BaseViewModel
import com.gowoon.common.base.UiEffect
import com.gowoon.common.base.UiEvent
import com.gowoon.common.base.UiState
import com.gowoon.domain.common.Result
import com.gowoon.domain.usecase.user.GetNoticeStatusUseCase
import com.gowoon.domain.usecase.user.GetRewardStatusUseCase
import com.gowoon.domain.usecase.user.GetUserNicknameUseCase
import com.gowoon.domain.usecase.user.UpdateNoticeStatusUseCase
import com.gowoon.domain.usecase.user.UpdateUserNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserNicknameUseCase: GetUserNicknameUseCase,
    private val updateUserNicknameUseCase: UpdateUserNicknameUseCase,
    private val getNoticeStatusUseCase: GetNoticeStatusUseCase,
    private val updateNoticeStatusUseCase: UpdateNoticeStatusUseCase,
    private val getRewardStatusUseCase: GetRewardStatusUseCase,
//    private val getBgmStateUseCase: GetBgmStateUseCase,
//    private val updateBgmStateUseCase: UpdateBgmStateUseCase,
//    private val hasBgmItemUseCase: HasBgmItemUseCase
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

//            is SettingEvent.OnClickSoundToggle -> {
//                updateBgmState(event.toastMessage)
//            }
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
            uiEffect.filter { it is SettingEffect.RefreshTrigger }.collectLatest {
                when (val result = getNoticeStatusUseCase()) {
                    is Result.Success -> {
                        setState(currentState.copy(newNotice = !result.data))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
        viewModelScope.launch {
            uiEffect.filter { it is SettingEffect.RefreshTrigger }.collectLatest {
                when (val result = getRewardStatusUseCase()) {
                    is Result.Success -> {
                        setState(currentState.copy(newItem = result.data))
                    }

                    is Result.Error -> {
                        // TODO error handling
                    }
                }
            }
        }
//        viewModelScope.launch {
//            getBgmStateUseCase().collect {
//                Napier.d("gowoon toggle status")
//                when (it) {
//                    is Result.Success -> {
//                        setState(currentState.copy(soundState = it.data))
//                    }
//
//                    is Result.Error -> {
//                        // TODO error handling
//                    }
//                }
//            }
//        }
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

//    private fun updateBgmState(toastMessage: String) {
//        viewModelScope.launch {
//            when (val result = hasBgmItemUseCase()) {
//                is Result.Error -> {
//                    // TODO error handling
//                    Napier.d("gowoon error")
//                }
//
//                is Result.Success -> {
//                    if (result.data) {
//                        Napier.d("gowoon has bgm")
//                        if (updateBgmStateUseCase(!currentState.soundState) is Result.Error) {
//                            // TODO error handling
//                        }
//                    } else {
//                        Napier.d("gowoon no bgm")
//                        showToast(toastMessage)
//                    }
//                }
//            }
//        }
//    }

    fun showToast(message: String) {
        setEffect(SettingEffect.ShowToast(message))
    }

    fun refreshReddot() {
        setEffect(SettingEffect.RefreshTrigger)
    }
}

data class SettingState(
    val nickname: String = "",
    val dialogState: SettingDialogType? = null,
    val newNotice: Boolean = false,
    val newItem: Boolean = false,
//    val soundState: Boolean = false
) : UiState

enum class SettingDialogType { BBS_RULE, EDIT_NICKNAME }

sealed interface SettingEvent : UiEvent {
    data class ShowDialog(val type: SettingDialogType?) : SettingEvent
    data class OnChangeNickName(val nickname: String, val callback: (Boolean) -> Unit) :
        SettingEvent

    data object UpdateNoticeStatusAsRead : SettingEvent
//    data class OnClickSoundToggle(val toastMessage: String) : SettingEvent
}

sealed interface SettingEffect : UiEffect {
    data class ShowToast(val message: String) : SettingEffect
    data object RefreshTrigger : SettingEffect
}