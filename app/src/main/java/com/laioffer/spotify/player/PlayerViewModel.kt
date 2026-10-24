package com.laioffer.spotify.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.laioffer.spotify.datamodel.Album
import com.laioffer.spotify.datamodel.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
) : ViewModel(), Player.Listener {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        exoPlayer.addListener(this)
        // Poll the playback position once per second while playing.
        viewModelScope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    val position = exoPlayer.currentPosition
                    val duration = exoPlayer.duration.coerceAtLeast(0L)
                    _uiState.update { it.copy(currentMs = position, durationMs = duration) }
                }
                delay(1000)
            }
        }
    }

    fun load(song: Song, album: Album) {
        _uiState.value = PlayerUiState(album = album, song = song, isPlaying = false)
        val mediaItem = MediaItem.Builder().setUri(song.src).build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun play() {
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _uiState.update { it.copy(isPlaying = isPlaying) }
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.e("SpotifyPlayer", "Playback error", error)
        _uiState.update { it.copy(isPlaying = false) }
    }

    override fun onCleared() {
        exoPlayer.removeListener(this)
        exoPlayer.stop()
        super.onCleared()
    }
}

data class PlayerUiState(
    val album: Album? = null,
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val currentMs: Long = 0,
    val durationMs: Long = 0,
)
