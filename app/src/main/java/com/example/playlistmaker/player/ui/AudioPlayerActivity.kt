package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.playlist.ui.CreatePlaylistFragment
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheetContainer = binding.playlistsBottomSheet
        val adapter = PlaylistAdapter()
        adapter.setOnClickListener { playlist ->
            viewModel.addTrackToPlaylist(playlist)
            Timber.tag("mylog").v( "playlist Clicked!")
        }
        binding.playlistyRecycler.layoutManager = LinearLayoutManager(this)
        binding.playlistyRecycler.adapter = adapter


        viewModel.getTrackLiveData().observe(this) { track ->
            showTrackData(track)
        }

        viewModel.getTrackAddedState().observe(this) { trackAddedState ->
            when (trackAddedState) {
                is TrackAddedState.Success -> {
                    Toast.makeText(this, "Добавлено в плейлист '${trackAddedState.playlist.playlistName}'", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.from(bottomSheetContainer).apply {
                        state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
                is TrackAddedState.Fail -> {
                    Toast.makeText(this, "Трек уже добавлен в плейлист '${trackAddedState.playlist.playlistName}'", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val addToFavoritesButton = binding.addToFavorites

        viewModel.getFavoriteTrackLiveData().observe(this) { favoriteTrack ->
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

        viewModel.getPlayerStateLiveData().observe(this) { playerState ->
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
            finish()
        }
        viewModel.loadTrackData()

        binding.playbackButton.setOnClickListener {
            viewModel.onPlaybackButtonClick()
        }

        val overlay = binding.overlay

        binding.addToPlaylist.setOnClickListener {
            viewModel.getAllPlaylist()
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.getPlaylistLiveData().observe(this) { playlists ->
            adapter.playlists.clear()
            adapter.playlists.addAll(playlists)
            adapter.notifyDataSetChanged()
        }

        binding.newPlaylistButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, CreatePlaylistFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit()
        }


        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
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

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseResources()
    }

    private fun updateTimeTextView(timeMillis: Int) {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.timesForTrack.text = formatter.format(timeMillis + TIME_TO_CEIL)
    }

    companion object {
        private const val TIME_TO_CEIL = 500
    }
}
