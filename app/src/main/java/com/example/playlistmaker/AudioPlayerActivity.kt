package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.AudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity: AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding

    private var mediaPlayer = MediaPlayer()
    private var url: String? = null
    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                val playbackMillis = mediaPlayer.currentPosition
                updateTimeTextView(playbackMillis)
                handler.postDelayed(this, TIME_INTERVAL)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val trackData = intent.extras?.getSerializable(KEY_TRACK_DATA) as Track
        url = trackData.previewUrl
        showTrackData(trackData)
        preparePlayer()

        binding.playbackControl.setOnClickListener {
            playbackControl()
        }
    }

    private fun showTrackData(track: Track){
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(8))
            .into(binding.trackCover)
        val formattedTrackTimes = track.formattedDuration()
        val formattedTrackYear = track.extractYear()
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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playbackControl.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            updateTimeTextView(0)
            binding.playbackControl.setBackgroundResource(R.drawable.ic_button_play)
        }
    }
    private  fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateRunnable)
        binding.playbackControl.setBackgroundResource(R.drawable.ic_button_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(updateRunnable)
        binding.playbackControl.setBackgroundResource(R.drawable.ic_button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        mediaPlayer.release()
    }
    private fun updateTimeTextView(timeMillis: Int) {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.timesForTrack.text = formatter.format(timeMillis + TIME_TO_CEIL)
    }
    companion object {
        const val KEY_TRACK_DATA = "KEY_TRACK_DATA"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_INTERVAL = 100L
        private const val TIME_TO_CEIL = 500
    }
}
