package com.dmytroherez.savingstrack

import androidx.compose.ui.window.ComposeUIViewController
import com.dmytroherez.savingstrack.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    MainApp()
}