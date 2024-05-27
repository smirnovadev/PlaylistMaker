package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerFragment : Fragment() {
    private val binding get() = _binding!!

    private var _binding: FragmentAudioPlayerBinding? = null
    private val viewModel: AudioPlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListPlaylistAdapter()
        adapter.setOnClickListener { playlist ->
            viewModel.addTrackToPlaylist(playlist)
            Timber.tag("mylog").v("playlist Clicked!")
        }
        binding.playlistyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistyRecycler.adapter = adapter

        viewModel.loadTrackData()

        val bottomSheetContainer = binding.playlistsBottomSheet
        val overlay = binding.overlay

        viewModel.getTrackLiveData().observe(viewLifecycleOwner) { track ->
            showTrackData(track)
        }

        viewModel.getTrackAddedState().observe(viewLifecycleOwner) { trackAddedState ->
            when (trackAddedState) {
                is TrackAddedState.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Добавлено в плейлист '${trackAddedState.playlist.playlistName}'",
                        Toast.LENGTH_SHORT
                    ).show()
                    BottomSheetBehavior.from(bottomSheetContainer).apply {
                        state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                is TrackAddedState.Fail -> {
                    Toast.makeText(
                        requireContext(),
                        "Трек уже добавлен в плейлист '${trackAddedState.playlist.playlistName}'",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val addToFavoritesButton = binding.addToFavorites
        viewModel.getFavoriteTrackLiveData().observe(viewLifecycleOwner) { favoriteTrack ->
            addToFavoritesButton.setBackgroundResource(
                if (favoriteTrack) {
                    R.drawable.ic_favorites_button
                } else {
                    R.drawable.ic_add_to_favorites
                }
            )
        }

        addToFavoritesButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { playerState ->
            binding.playbackButton.setBackgroundResource(
                if (playerState.isPlaying) {
                    R.drawable.ic_button_pause
                } else {
                    R.drawable.ic_button_play
                }
            )
            binding.playbackButton.isEnabled = playerState.isPrepared
            updateTimeTextView(playerState.timeMillis)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.playbackButton.setOnClickListener {
            viewModel.onPlaybackButtonClick()
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.getAllPlaylist()
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlists ->
            adapter.playlists.clear()
            adapter.playlists.addAll(playlists)
            adapter.notifyDataSetChanged()
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_createdPlaylistFragment)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        overlay.visibility = View.GONE
                    } else {
                        overlay.visibility = View.VISIBLE
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    /* no-op */
                }
            }
        )
    }

    private fun showTrackData(track: Track) {
        showTrackPlayer(track)
        showTrackDescription(track)
    }

    private fun showTrackPlayer(track: Track) {
        Glide.with(this)
            .load(track.artworkUrl512)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(8))
            .into(binding.trackCover)
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
    }

    private fun showTrackDescription(track: Track) {
        binding.trackTime.text = track.formattedDuration
        binding.trackYear.text = track.releaseYear
        binding.genreTrack.text = track.primaryGenreName
        binding.countryTrack.text = track.country
        if (track.collectionName == null) {
            binding.album.visibility = View.GONE
            binding.collectionName.visibility = View.GONE
        } else {
            binding.album.text = track.collectionName
        }
    }
    override fun onStart() {
        super.onStart()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }
    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateTimeTextView(timeMillis: Int) {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.timesForTrack.text = formatter.format(timeMillis + TIME_TO_CEIL)
    }

    companion object {
        private const val TIME_TO_CEIL = 500
    }
}
