package com.dmytroherez.savingstrack.auth.presentation.home

import com.dmytroherez.savingstrack.core.presentation.BaseViewModel

class HomeViewModel : BaseViewModel<HomeState, HomeEvent> (
    HomeState()
) {

    fun onAction(action: HomeAction) {
        when(action) {
            HomeAction.SomeAction -> {

            }
        }
    }
}