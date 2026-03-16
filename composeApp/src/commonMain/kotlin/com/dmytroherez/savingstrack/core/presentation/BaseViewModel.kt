package com.dmytroherez.savingstrack.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseUiState, E : BaseUiEvent>(
    initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val state: StateFlow<S> = _uiState.asStateFlow()

    private val _event = Channel<E>()
    val event = _event.receiveAsFlow()

    protected fun updateState(reducer: (S) -> S) {
        _uiState.update(reducer)
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            _event.send(event)
        }
    }
}

interface BaseUiState
interface BaseUiEvent {
    data class ShowToast(val message: UiText) : BaseUiEvent
}