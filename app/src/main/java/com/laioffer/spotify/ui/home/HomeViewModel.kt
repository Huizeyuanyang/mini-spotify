package com.laioffer.spotify.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.spotify.datamodel.Section
import com.laioffer.spotify.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(feed = emptyList(), isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Load once per ViewModel, so returning to the Home tab doesn't refetch.
        fetchHomeScreen()
    }

    fun fetchHomeScreen() {
        _uiState.value = HomeUiState(feed = emptyList(), isLoading = true)
        viewModelScope.launch {
            _uiState.value = try {
                HomeUiState(feed = repository.getHomeSections(), isLoading = false)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to load home feed", e)
                HomeUiState(feed = emptyList(), isLoading = false, hasError = true)
            }
        }
    }
}

data class HomeUiState(
    val feed: List<Section>,
    val isLoading: Boolean,
    val hasError: Boolean = false,
)
