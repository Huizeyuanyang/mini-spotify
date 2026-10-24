package com.laioffer.spotify.repository

import com.laioffer.spotify.datamodel.Section
import com.laioffer.spotify.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class HomeRepository @Inject constructor(private val networkApi: NetworkApi) {

    suspend fun getHomeSections(): List<Section> = withContext(Dispatchers.IO) {
        val response = networkApi.getHomeFeed().execute()
        if (!response.isSuccessful) {
            throw IOException("Home feed request failed: HTTP ${response.code()}")
        }
        response.body() ?: emptyList()
    }
}
