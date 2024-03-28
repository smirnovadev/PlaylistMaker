package com.example.playlistmaker.music.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMusicBinding
import com.google.android.material.tabs.TabLayoutMediator


class MusicFragment : Fragment() {

    private  var _binding: FragmentMusicBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setTitle(R.string.music)
        binding.viewPager.adapter = MusicViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites_track)
                1 -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        tabMediator.detach()
    }

}