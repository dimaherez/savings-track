package com.dmytroherez.savingstrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.screen.Screen
import com.dmytroherez.savingstrack.domain.usecase.auth.GetCurrentUserUC
import com.dmytroherez.savingstrack.presentation.auth.AuthScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCurrentUserUC: GetCurrentUserUC
): ViewModel() {

    private val _initScreen = MutableSharedFlow<Screen>(replay = 1)
    val initScreen = _initScreen.asSharedFlow()

    init {
        getInitialScreen()
    }

    private fun getInitialScreen() {
        viewModelScope.launch {
            getCurrentUserUC()
                .onSuccess {
                    _initScreen.emit(MainScreen)
                }
                .onFailure {
                    _initScreen.emit(AuthScreen)
                }
        }
    }

}