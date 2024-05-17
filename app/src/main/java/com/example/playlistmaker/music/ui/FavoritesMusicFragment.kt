package com.example.playlistmaker.music.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesMusicBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesMusicFragment : Fragment() {
    private val favoritesMusicViewModel: FavoritesMusicViewModel by viewModel()

    private var _binding: FragmentFavoritesMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteTracksAdapter: TrackAdapter
    private val trackList = ArrayList<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesMusicViewModel.getFavoriteTrackLiveData()
            .observe(viewLifecycleOwner) { favoriteState ->
                when (favoriteState) {
                    is FavoriteState.Empty -> showEmpty()
                    is FavoriteState.Content -> showContent(favoriteState.favoriteTrack)
                }
            }
        favoriteTracksAdapter = TrackAdapter(onClickAction = { trackData ->
            navigateToAudioPlayerActivity(trackData)
        })
        favoriteTracksAdapter.trackList = trackList
        val recyclerView = binding.favoritesRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoriteTracksAdapter
    }

    private fun showEmpty() {
        binding.emptyContainer.visibility = View.VISIBLE
        binding.favoritesRecycler.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        trackList.clear()
        trackList.addAll(tracks)
        favoriteTracksAdapter.notifyDataSetChanged()
        binding.emptyContainer.visibility = View.GONE
        binding.favoritesRecycler.visibility = View.VISIBLE
    }

    private fun navigateToAudioPlayerActivity(trackData: Track) {
        favoritesMusicViewModel.saveTrackToCache(trackData)
        findNavController().navigate(R.id.action_musicFragment_to_audioPlayerActivity)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        favoritesMusicViewModel.getAllFavoriteTrack()

    }

    companion object {
        fun newInstance() = FavoritesMusicFragment()
    }


}
