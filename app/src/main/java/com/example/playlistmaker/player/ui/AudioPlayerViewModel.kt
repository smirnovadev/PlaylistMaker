package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.entity.domain.db.AddFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.db.ClearFavoriteTrackUseCase
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val getTrackUseCase: GetTrackFromCacheUseCase,
    private val addFavoriteTrackUseCase: AddFavoriteTrackUseCase,
    private val clearFavoriteTrackUseCase: ClearFavoriteTrackUseCase
) : ViewModel() {
    private var timerJob: Job? = null
    private var mediaPlayer = MediaPlayer()

    private var trackLiveData = MutableLiveData<Track>()
    private var playerStateLiveData = MutableLiveData<PlayerState>()
    private var isFavoriteTrackLiveData = MutableLiveData<Boolean>()

    private fun getCurrentPlayerState(): PlayerState {
        return playerStateLiveData.value ?: PlayerState(
            timeMillis = 0,
            isPlaying = false,
            isPrepared = false
        )
    }

    fun getTrackLiveData(): LiveData<Track> = trackLiveData

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData
    fun getFavoriteTrackLiveDaya(): LiveData<Boolean> = isFavoriteTrackLiveData

    fun loadTrackData() {
        val trackData = getTrackUseCase.execute() ?: error("unexpected no track")
        trackLiveData.value = trackData
        isFavoriteTrackLiveData.value = trackData.isFavorite
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

    fun onFavoriteClicked() {
        val track = trackLiveData.value ?: return
        viewModelScope.launch {
            if (isFavoriteTrackLiveData.value == true) {
                clearFavoriteTrackUseCase.execute(track)
                isFavoriteTrackLiveData.postValue(false)
            } else {
                addFavoriteTrackUseCase.execute(track)
                isFavoriteTrackLiveData.postValue(true)
            }
        }
    }

    companion object {
        private const val TIME_INTERVAL = 300L
    }
}