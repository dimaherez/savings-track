package com.dmytroherez.savingstrack.core.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ButtonRound(
    modifier: Modifier,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    onCLick: () -> Unit
) {
    TextButton(
        modifier = modifier
            .clip(CircleShape)
            .size(48.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        onClick = {
            onCLick()
        },
    ) {
        when {
            painter != null -> {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            imageVector != null -> {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}