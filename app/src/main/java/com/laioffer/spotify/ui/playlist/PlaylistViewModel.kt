package com.laioffer.spotify.ui.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.spotify.datamodel.Album
import com.laioffer.spotify.datamodel.Song
import com.laioffer.spotify.repository.FavoriteAlbumRepository
import com.laioffer.spotify.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val favoriteAlbumRepository: FavoriteAlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PlaylistUiState(
            album = Album.empty(),
            isFavorite = false,
            playlist = emptyList()
        )
    )
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    fun fetchPlaylist(album: Album) {
        _uiState.update { it.copy(album = album) }

        viewModelScope.launch {
            try {
                val playlist = playlistRepository.getPlaylist(album.id)
                _uiState.update { it.copy(playlist = playlist.songs) }
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Failed to load playlist ${album.id}", e)
            }
        }

        // Observe favorite status; cancel any previous observer first.
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            favoriteAlbumRepository.isFavoriteAlbum(album.id).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }

    fun toggleFavorite(isFavorite: Boolean) {
        val album = _uiState.value.album
        viewModelScope.launch {
            if (isFavorite) {
                favoriteAlbumRepository.favoriteAlbum(album)
            } else {
                favoriteAlbumRepository.unFavoriteAlbum(album)
            }
        }
    }
}

data class PlaylistUiState(
    val album: Album,
    val isFavorite: Boolean,
    val playlist: List<Song>,
)
