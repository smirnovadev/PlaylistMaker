package com.example.playlistmaker.music.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesMusicBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesMusicFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesMusicFragment()
    }

    private val favoritesMusicViewModel: FavoritesMusicViewModel by viewModel()

    private lateinit var binding: FragmentFavoritesMusicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

}
