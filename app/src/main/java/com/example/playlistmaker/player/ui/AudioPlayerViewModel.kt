package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackFromCacheUseCase,
) : ViewModel() {
    private var timerJob: Job? = null
    private var mediaPlayer = MediaPlayer()

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
        timerJob?.cancel()
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
            timerJob?.cancel()
            playerStateLiveData.value =
                getCurrentPlayerState().copy(timeMillis = 0, isPlaying = false)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        playerStateLiveData.value = getCurrentPlayerState().copy(isPlaying = true)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
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

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                val playbackMillis = mediaPlayer.currentPosition
                playerStateLiveData.value =
                    getCurrentPlayerState().copy(timeMillis = playbackMillis)
                delay(TIME_INTERVAL)
            }
        }
    }

    companion object {
        private const val TIME_INTERVAL = 300L
    }
}