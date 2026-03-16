package com.dmytroherez.savingstrack.core.presentation.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextInput(
    modifier: Modifier,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    Column {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = {
                Text(
                    text = hint,
                )
            },
            trailingIcon = trailingIcon,
            isError = error != null,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            visualTransformation = visualTransformation ?: VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )

        if (error.isNullOrBlank().not()) {
            Text(
                text = error
            )
        }
    }
}