package com.example.takeaway.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayDisabled
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun PlayIcon(
    modifier: Modifier = Modifier,
    mediaUrl: String = "",
) {
    val playbackState = remember {
        mutableStateOf(if (mediaUrl.isBlank()) PlaybackState.DISABLE else PlaybackState.IDLE)
    }
    val audioPlayer = LocalAudioPlayer.current
    Icon(
        modifier = modifier.clickable(
            enabled = playbackState.value == PlaybackState.IDLE,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            audioPlayer.play(mediaUrl) {
                playbackState.value = it
            }
        },
        contentDescription = mediaUrl,
        imageVector = when (playbackState.value) {
            PlaybackState.IDLE, PlaybackState.BUFFERING, PlaybackState.READY -> Icons.Filled.PlayCircle
            PlaybackState.PLAYING -> Icons.Filled.VolumeUp
            PlaybackState.DISABLE -> Icons.Filled.PlayDisabled
        },
        tint = when (playbackState.value) {
            PlaybackState.BUFFERING, PlaybackState.READY -> LocalContentColor.current.copy(alpha = 0.3f)
            PlaybackState.DISABLE -> LocalContentColor.current.copy(alpha = 0.5f)
            else -> LocalContentColor.current
        }
    )
}
