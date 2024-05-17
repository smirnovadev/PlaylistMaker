package com.example.playlistmaker.music.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.playlist.domain.model.Playlist
import com.example.playlistmaker.playlist.ui.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private lateinit var playlistAdapter: PlaylistAdapter
    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllPlaylist()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_musicFragment_to_createdPlaylistFragment)
        }

        viewModel.getPlaylistTrackLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Empty -> showEmptyConteiner()
                is PlaylistState.Content -> showContent(it.playlist)
            }
        }
        val recyclerView = binding.recyclerViewPlaylist
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistAdapter = PlaylistAdapter()
        recyclerView.adapter = playlistAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()
        binding.emptyContainer.visibility = View.GONE
        binding.recyclerViewPlaylist.visibility = View.VISIBLE
    }

    private fun showEmptyConteiner() {
        binding.emptyContainer.visibility = View.VISIBLE
        binding.recyclerViewPlaylist.visibility = View.GONE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}
