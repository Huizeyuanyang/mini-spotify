package com.laioffer.spotify.repository

import com.laioffer.spotify.datamodel.Playlist
import com.laioffer.spotify.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class PlaylistRepository @Inject constructor(private val networkApi: NetworkApi) {

    suspend fun getPlaylist(id: Int): Playlist = withContext(Dispatchers.IO) {
        val response = networkApi.getPlaylist(id).execute()
        if (!response.isSuccessful) {
            throw IOException("Playlist request failed: HTTP ${response.code()}")
        }
        response.body() ?: throw IOException("Playlist $id has an empty body")
    }
}
