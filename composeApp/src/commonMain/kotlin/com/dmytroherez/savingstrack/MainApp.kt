package com.dmytroherez.savingstrack

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.dmytroherez.savingstrack.auth.presentation.auth.AuthScreen

@Composable
fun MainApp() {
    MaterialTheme {
        Navigator(AuthScreen)
    }
}