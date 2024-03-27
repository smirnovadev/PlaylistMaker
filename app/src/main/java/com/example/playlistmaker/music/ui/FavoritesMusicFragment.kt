package com.example.playlistmaker.music.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesMusicBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesMusicFragment : Fragment() {


    private val favoritesMusicViewModel: FavoritesMusicViewModel by viewModel()

    private  var _binding: FragmentFavoritesMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance() = FavoritesMusicFragment()
    }

}
