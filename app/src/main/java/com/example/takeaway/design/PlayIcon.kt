package com.example.takeaway.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayDisabled
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun PlayIcon(
    modifier: Modifier = Modifier,
    mediaUrl: String = "",
    state: PlayState = PlayState.IDLE,
    onPlayClick: (String) -> Unit
) {
    Icon(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            onPlayClick(mediaUrl)
        },
        contentDescription = mediaUrl,
        imageVector = when (state) {
            PlayState.IDLE -> Icons.Filled.PlayCircle
            PlayState.BUFFERING -> Icons.Filled.PlayCircle
            PlayState.PLAYING -> Icons.Filled.VolumeUp
            PlayState.ERROR -> Icons.Filled.PlayCircle
            PlayState.DISABLE -> Icons.Filled.PlayDisabled
        },
        tint = when (state) {
            PlayState.BUFFERING -> LocalContentColor.current.copy(alpha = 0.5f)
            PlayState.ERROR -> MaterialTheme.colors.error
            else -> LocalContentColor.current
        }
    )
}

enum class PlayState {
    IDLE,
    BUFFERING,
    PLAYING,
    ERROR,
    DISABLE
}
