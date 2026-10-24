# Mini Spotify

[![Android CI](https://github.com/Huizeyuanyang/mini-spotify/actions/workflows/android.yml/badge.svg)](https://github.com/Huizeyuanyang/mini-spotify/actions/workflows/android.yml)

A Spotify-style music streaming app for Android, built with Kotlin and Jetpack Compose. Browse albums on the home feed, open an album to see its playlist, play songs with a floating mini player, and save favorite albums to a local database.

## Features

- **Home feed**: albums grouped into sections, loaded from a REST backend, with a loading state and tap-to-retry on network errors
- **Playlist page**: album cover on a spinning vinyl record while music plays, album info, and the song list with the current song highlighted
- **Mini player**: a floating player bar above the bottom navigation with play/pause, shared across all screens
- **Favorites**: favorite or unfavorite an album from its playlist page; favorites are stored locally and work offline
- **Bottom navigation**: switch between Home and Favorite tabs

## Tech Stack

| Area | Library |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose (Material), hosted in Fragments |
| Architecture | MVVM with Repository pattern, `StateFlow` UI state |
| Dependency injection | Hilt |
| Navigation | Navigation Component + Safe Args |
| Networking | Retrofit + Gson |
| Local database | Room (with Kotlin coroutines and `Flow`) |
| Media playback | ExoPlayer |
| Image loading | Coil |

## Project Structure

```
app/src/main/java/com/laioffer/spotify/
├── MainActivity.kt          # Hosts navigation and the player bar
├── MainApplication.kt       # Hilt entry point and global Coil image loader
├── datamodel/               # Album, Section, Playlist, Song
├── network/                 # Retrofit API and Hilt module
├── database/                # Room database, DAO and Hilt module
├── repository/              # Home, Playlist and FavoriteAlbum repositories
├── player/                  # ExoPlayer module, PlayerViewModel, PlayerBar
└── ui/
    ├── home/                # Home feed screen
    ├── playlist/            # Album playlist screen
    ├── favorite/            # Favorite albums screen
    └── theme/               # Compose theme
```

## Backend

The app expects a backend server running at `http://10.0.2.2:8080/`, which is how the Android emulator reaches `localhost` on your computer. It uses two endpoints:

| Method | Path | Returns |
| --- | --- | --- |
| GET | `/feed` | List of sections, each with a title and a list of albums |
| GET | `/playlist/{id}` | The songs of the album with that id |

To use a different server, change `BASE_URL` in `network/NetworkModule.kt`. Cleartext HTTP is only allowed for `10.0.2.2` (see `res/xml/network_security_config.xml`), so add your host there too if it is not HTTPS.

## Getting Started

**Requirements:** Android Studio (Electric Eel or newer), JDK 11+, Android SDK 33. The app runs on Android 6.0 (API 23) and above.

1. Clone the repository:
   ```bash
   git clone https://github.com/Huizeyuanyang/mini-spotify.git
   ```
2. Open the project in Android Studio and let Gradle sync.
3. Start the backend server on port 8080.
4. Run the app on an emulator.

You can also build from the command line:

```bash
./gradlew assembleDebug
```

## Continuous Integration

Every push runs the GitHub Actions workflow in `.github/workflows/android.yml`, which builds the debug APK and runs the unit tests. The built APK can be downloaded from the workflow run page under **Artifacts**, so you can get an installable APK without building locally.
