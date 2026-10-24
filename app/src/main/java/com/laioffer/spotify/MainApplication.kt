package com.laioffer.spotify

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class MainApplication : Application(), ImageLoaderFactory {

    // Wikimedia rejects image requests without a User-Agent (HTTP 403), so every
    // AsyncImage in the app (home, favorite, playlist, player bar) shares this loader.
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header("User-Agent", "SpotifyDemo/1.0 (Android)")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .crossfade(true)
            .build()
    }
}
