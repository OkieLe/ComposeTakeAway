package com.example.takeaway.design

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconMenu(
    imageVector: ImageVector,
    description: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = Modifier.wrapContentWidth(),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            tint = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
fun IconMenu(
    imagePainter: Painter,
    description: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = Modifier.wrapContentWidth(),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            painter = imagePainter,
            contentDescription = description,
            tint = MaterialTheme.colors.primaryVariant
        )
    }
}
