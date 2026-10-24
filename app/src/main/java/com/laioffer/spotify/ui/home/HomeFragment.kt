package com.laioffer.spotify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    HomeScreen(
                        viewModel,
                        onTap = {
                            val direction =
                                HomeFragmentDirections.actionHomeFragmentToPlaylistFragment(it)
                            findNavController().navigate(direction)
                        },
                        onRetry = { viewModel.fetchHomeScreen() }
                    )
                }
            }
        }
    }
}
