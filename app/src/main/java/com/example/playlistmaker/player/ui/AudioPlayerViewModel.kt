package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCase
import com.example.playlistmaker.search.domain.model.Track

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackFromCacheUseCase,
) : ViewModel() {

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                val playbackMillis = mediaPlayer.currentPosition
                playerStateLiveData.value = getCurrentPlayerState().copy(timeMillis = playbackMillis)
                handler.postDelayed(this, TIME_INTERVAL)
            }
        }
    }

    private var trackLiveData = MutableLiveData<Track>()
    private var playerStateLiveData = MutableLiveData<PlayerState>()

    private fun getCurrentPlayerState(): PlayerState {
        return playerStateLiveData.value ?: PlayerState(
            timeMillis = 0,
            isPlaying = false,
            isPrepared = false
        )
    }

    fun getTrackLiveData(): LiveData<Track> = trackLiveData

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    fun loadTrackData() {
        val trackData = getTrackUseCase.execute() ?: error("unexpected no track")
        trackLiveData.value = trackData
        preparePlayer()
    }

    fun releaseResources() {
        handler.removeCallbacks(updateRunnable)
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        val url = trackLiveData.value?.previewUrl
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.value = getCurrentPlayerState().copy(isPrepared = true)
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.value = getCurrentPlayerState().copy(timeMillis = 0, isPlaying = false)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateRunnable)
        playerStateLiveData.value = getCurrentPlayerState().copy(isPlaying = true)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(updateRunnable)
        playerStateLiveData.value = getCurrentPlayerState().copy(isPlaying = false)
    }

    fun onPlaybackButtonClick() {
        val playerState = getCurrentPlayerState()
        when {
            playerState.isPlaying -> {
                pausePlayer()
            }
            !playerState.isPlaying && playerState.isPrepared -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val TIME_INTERVAL = 100L
    }
}