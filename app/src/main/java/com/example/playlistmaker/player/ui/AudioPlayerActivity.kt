package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding

    private lateinit var viewModel: AudioPlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

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

        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        viewModel.loadTrackData()

        binding.playbackButton.setOnClickListener {
            viewModel.onPlaybackButtonClick()
        }
    }

    private fun showTrackData(track: Track) {
        Glide.with(this)
            .load(track.artworkUrl512)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(8))
            .into(binding.trackCover)
        val formattedTrackTimes = track.formattedDuration
        val formattedTrackYear = track.releaseYear
        binding.trackTime.text = formattedTrackTimes

        if (track.collectionName == null) {
            binding.album.visibility = View.GONE
            binding.collectionName.visibility = View.GONE
        } else {
            binding.album.text = track.collectionName
        }

        binding.trackYear.text = formattedTrackYear
        binding.genreTrack.text = track.primaryGenreName
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.countryTrack.text = track.country
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
