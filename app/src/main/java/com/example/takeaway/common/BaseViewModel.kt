package com.example.takeaway.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

interface UiAction
interface UiState
interface UiEvent

abstract class BaseViewModel<UA: UiAction, US: UiState, UE: UiEvent>: ViewModel() {
    private val _events = Channel<UE>()
    val events = _events.receiveAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<US> = _state.asStateFlow()

    abstract val initialState: US

    abstract fun submit(action: UA)

    protected fun updateState(state: US) {
        viewModelScope.launch { _state.emit(state) }
    }

    protected fun sendEvent(event: UE) {
        viewModelScope.launch { _events.send(event) }
    }
}
