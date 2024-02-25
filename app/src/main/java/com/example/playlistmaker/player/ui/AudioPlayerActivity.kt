package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTrackLiveData().observe(this) { track ->
            showTrackData(track)
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
