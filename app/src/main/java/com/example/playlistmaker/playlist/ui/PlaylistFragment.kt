package com.example.playlistmaker.playlist.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.createPlaylist.ui.CreatePlaylistFragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val binding get() = _binding!!

    private var _binding: FragmentPlaylistBinding? = null

    private lateinit var trackAdapter: TrackAdapter

    private val viewModel: PlaylistViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetContainer = binding.playlistsBottomSheet
        val menuBottomSheetContainer = binding.menuBottomSheet
        val overlay = binding.overlay

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        val menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            isHideable = true
        }

        menuBottomSheetBehavior.addBottomSheetCallback(
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

        binding.buttonMenuPlaylist.setOnClickListener {
            menuBottomSheetContainer.visibility = View.VISIBLE
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            overlay.visibility = View.VISIBLE
        }

        binding.editInformationMenuBottomSheet.setOnClickListener {
            val playlistScreenState = viewModel.getPlaylistScreenStateLiveData().value
            playlistScreenState?.let { state ->
                navigateToCreatePlaylist(state.playlist)
            }
        }

        binding.toolbar.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.deletePlaylistMenuBottomSheet.setOnClickListener {
            val playlistScreenState = viewModel.getPlaylistScreenStateLiveData().value
            playlistScreenState?.let { state ->
                showDialogToDeletePlaylist(state.playlist)
            }

        }

        val playlistId = requireArguments().getLong(KEY_PLAYLIST_ID)
        viewModel.loadPlaylist(playlistId)

        viewModel.getPlaylistScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScreenState.Empty -> showEmpty(state)
                is PlaylistScreenState.Content -> showContent(state)
            }
        }

        trackAdapter = TrackAdapter(onClickAction = { track ->
            navigateToAudioPlayer(track)
        })

        binding.playlistsBottomSheetRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsBottomSheetRecycler.adapter = trackAdapter

        trackAdapter.setOnLongClickListener { track ->
            showDialogToDeleteTrack(track.trackId)
        }

    }

    private fun navigateToAudioPlayer(track: Track) {
        findNavController().navigate(R.id.action_playlistFragment_to_audioPlayerFragment)
        viewModel.saveTrackToCashe(track)
    }

    private fun navigateToCreatePlaylist(playlist: Playlist) {
        findNavController().navigate(R.id.action_playlistFragment_to_createdPlaylistFragment,
            CreatePlaylistFragment.createArgs(playlist.playlistId)
        )
    }

    private fun showContent(state: PlaylistScreenState.Content) {
        binding.emptyRecycler.visibility = View.GONE
        binding.playlistsBottomSheetRecycler.visibility = View.VISIBLE

        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(state.tracks)
        trackAdapter.notifyDataSetChanged()

        val tracksCount = state.tracks.size
        showTracksDurationAndCount(state.durationMin, tracksCount)
        showPlaylist(state.playlist)

        binding.buttonSharePlaylist.setOnClickListener {
            sharePlaylist(state)
        }
        binding.shareMenuBottomSheet.setOnClickListener {
            sharePlaylist(state)

        }
    }

    private fun sharePlaylist(state: PlaylistScreenState.Content) {
        val message = createPlaylistMessage(
            state.playlist.playlistName,
            state.playlist.description,
            state.tracks
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(shareIntent)
        binding.menuBottomSheet.visibility = View.GONE
        binding.overlay.visibility = View.GONE
    }

    private fun showEmpty(state: PlaylistScreenState.Empty) {
        binding.emptyRecycler.visibility = View.VISIBLE
        binding.playlistsBottomSheetRecycler.visibility = View.GONE

        showTracksDurationAndCount(0, 0)
        showPlaylist(state.playlist)

        binding.buttonSharePlaylist.setOnClickListener {
            makeToastForEmptyPlaylist()
        }
        binding.shareMenuBottomSheet.setOnClickListener {
            makeToastForEmptyPlaylist()
        }
    }

    private fun makeToastForEmptyPlaylist() {
        Toast.makeText(
            requireContext(), getString(R.string.toast_empty_playlist),
            Toast.LENGTH_SHORT
        ).show()
        binding.menuBottomSheet.visibility = View.GONE
        binding.overlay.visibility = View.GONE
    }

    private fun showTracksDurationAndCount(totalMinutes: Long, tracksCount: Int) {
        val minutesText = resources.getQuantityString(
            R.plurals.minutes,
            totalMinutes.toInt(),
            totalMinutes.toInt()
        )
        val tracksCountText =
            resources.getQuantityString(R.plurals.tracks, tracksCount, tracksCount)
        val text = "$minutesText • $tracksCountText"
        binding.durationOfAllTracks.text = text
        binding.tracksNumber.text = tracksCountText
    }


    private fun showPlaylist(playlist: Playlist) {
        Glide.with(requireActivity())
            .load(playlist.coverArtPath)
            .centerCrop()
            .placeholder(R.drawable.track_placeholder_45)
            .into(binding.playlistImage)
        binding.namePlaylist.text = playlist.playlistName
        binding.descriptionText.text = playlist.description

        Glide.with(requireActivity())
            .load(playlist.coverArtPath)
            .centerCrop()
            .placeholder(R.drawable.track_placeholder_45)
            .into(binding.playlistCoverImage)

        binding.playlistName.text = playlist.playlistName
    }

    private fun showDialogToDeletePlaylist(playlist: Playlist) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialogTheme)
            .setTitle(getString(R.string.do_you_want_to_delete_playlist, playlist.playlistName))
            .setPositiveButton(R.string.yes) { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().popBackStack()
            }
            .setNeutralButton(R.string.no) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogToDeleteTrack(trackId: Long) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialogTheme)
            .setTitle(getString(R.string.do_you_want_to_delete_track))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deleteTrackForId(trackId)
            }
            .setNeutralButton(getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createPlaylistMessage(
        playlistName: String,
        playlistDescription: String,
        tracks: List<Track>
    ): String {

        val tracksCountString =
            resources.getQuantityString(R.plurals.tracks, tracks.size, tracks.size)

        val tracksInfo = tracks.mapIndexed { index, track ->
            getString(
                R.string.track_info_format,
                index + 1,
                track.artistName,
                track.trackName,
                track.formattedDuration
            )
        }.joinToString(separator = "\n")

        return """
            $playlistName
            $playlistDescription
            $tracksCountString
            
            $tracksInfo
        """.trimIndent()
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

    companion object {
        private const val KEY_PLAYLIST_ID = "id_playlist"

        fun createArgs(playlistId: Long): Bundle {
            return bundleOf(
                KEY_PLAYLIST_ID to playlistId
            )
        }
    }
}