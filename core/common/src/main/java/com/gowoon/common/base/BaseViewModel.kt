package com.gowoon.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel() {
    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _state.asStateFlow()

    private val _event: Channel<Event> = Channel()
    val uiEvent = _event.receiveAsFlow()

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val uiEffect = _effect.asSharedFlow()

    val currentState: State
        get() = uiState.value

    abstract fun handleEvent(event: Event)

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            uiEvent.collect {
                handleEvent(it)
            }
        }
    }

    protected fun setState(state: State) {
        _state.value = state
    }

    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    protected fun setEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}

interface UiState
interface UiEvent
interface UiEffect