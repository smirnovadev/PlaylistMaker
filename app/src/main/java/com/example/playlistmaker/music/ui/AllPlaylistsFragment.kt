package com.example.playlistmaker.music.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentAllPlaylistBinding
import com.example.playlistmaker.playlist.ui.PlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllPlaylistsFragment : Fragment() {
    private lateinit var playlistAdapter: GridPlaylistAdapter
    private val viewModel: AllPlaylistsViewModel by viewModel()

    private var _binding: FragmentAllPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPlaylistBinding.inflate(inflater, container, false)
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
                is AllPlaylistsState.Empty -> showEmptyConteiner()
                is AllPlaylistsState.Content -> showContent(it.playlist)
            }
        }
        val recyclerView = binding.recyclerViewPlaylist
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistAdapter = GridPlaylistAdapter()
        playlistAdapter.setOnClickListener { playlist ->
            navigateToPlaylist(playlist)
        }
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

    private fun navigateToPlaylist(playlist: Playlist) {
        findNavController().navigate(R.id.action_musicFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist.playlistId)
        )
    }

    companion object {
        fun newInstance() = AllPlaylistsFragment()
    }

}
