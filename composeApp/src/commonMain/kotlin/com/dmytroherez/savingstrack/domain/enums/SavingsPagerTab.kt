package com.dmytroherez.savingstrack.domain.enums

import com.dmytroherez.savingstrack.core.presentation.UiText
import savingstrack.composeapp.generated.resources.Res
import savingstrack.composeapp.generated.resources.crypto
import savingstrack.composeapp.generated.resources.fiat

enum class SavingsPagerTab(
    val title: UiText
) {
    FIAT(UiText.StringResourceId(Res.string.fiat)),
    CRYPTO(UiText.StringResourceId(Res.string.crypto))
}