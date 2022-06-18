package com.example.takeaway.design

import androidx.compose.runtime.compositionLocalOf
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import javax.inject.Inject

val LocalAudioPlayer = compositionLocalOf<AudioPlayer> {
    error("No AudioPlayer provided")
}

class AudioPlayer @Inject constructor(private val exoPlayer: ExoPlayer) {
    private var playbackStateChanged: ((PlaybackState) -> Unit)? = null

    fun play(url: String, onPlaybackStateChanged: (PlaybackState) -> Unit) {
        playbackStateChanged = onPlaybackStateChanged
        if (url.isBlank()) {
            notifyPlaybackState(PlaybackState.DISABLE, true)
            return
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                notifyPlaybackState(when (state) {
                    Player.STATE_IDLE -> PlaybackState.IDLE
                    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                    Player.STATE_READY -> PlaybackState.READY
                    Player.STATE_ENDED -> PlaybackState.IDLE
                    else -> PlaybackState.DISABLE
                }, state == Player.STATE_ENDED)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    notifyPlaybackState(PlaybackState.PLAYING)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                notifyPlaybackState(PlaybackState.DISABLE, true)
            }
        })
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
    }

    private fun notifyPlaybackState(state: PlaybackState, removeCallback: Boolean = false) {
        playbackStateChanged?.invoke(state)
        if (removeCallback) {
            playbackStateChanged = null
        }
    }
}

enum class PlaybackState {
    IDLE,
    BUFFERING,
    READY,
    PLAYING,
    DISABLE
}
