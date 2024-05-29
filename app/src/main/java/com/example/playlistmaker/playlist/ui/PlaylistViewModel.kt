package com.example.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.db.entity.domain.GetPlaylistByIdUseCase
import com.example.playlistmaker.playlist.domain.DeletePlaylistUseCase
import com.example.playlistmaker.playlist.domain.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.GetAllTracksForPlaylistUseCase
import com.example.playlistmaker.search.data.mapper.TrackTimeFormatter
import com.example.playlistmaker.search.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
    private val getAllTracksForPlaylistUseCase: GetAllTracksForPlaylistUseCase,
    private val trackTimeFormatter: TrackTimeFormatter,
    private val saveTrackToCacheUseCase: SaveTrackToCacheUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel() {

    private val playlistScreenStateLiveData = MutableLiveData<PlaylistScreenState>()

    fun getPlaylistScreenStateLiveData(): LiveData<PlaylistScreenState> =
        playlistScreenStateLiveData

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            getPlaylistByIdUseCase.execute(playlistId).collect { playlist ->
                getAllTracksForPlaylist(playlist.trackIdList, playlist)
            }
        }
    }

    fun saveTrackToCashe(track: Track) {
        saveTrackToCacheUseCase.execute(track)
    }


    fun getAllTracksForPlaylist(playlistTrackIds: List<Long>, playlist: Playlist) {
        viewModelScope.launch {
            getAllTracksForPlaylistUseCase.execute(playlistTrackIds)
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        playlistScreenStateLiveData.value = PlaylistScreenState.Empty(
                            playlist
                        )
                    } else {
                        playlistScreenStateLiveData.value = PlaylistScreenState.Content(
                            playlist = playlist,
                            tracks = tracks,
                            durationMin = calculateTotalDuration(tracks)
                        )
                    }
                }
        }
    }

    fun deleteTrackForId(trackId: Long) {
        val playlist = playlistScreenStateLiveData.value?.playlist ?: return
        viewModelScope.launch {
            deleteTrackFromPlaylistUseCase.execute(trackId, playlist)
                .collect { updatePlaylist ->
                    getAllTracksForPlaylist(updatePlaylist.trackIdList, playlist)
                }
        }
    }

    fun deletePlaylist() {
        val playlist = playlistScreenStateLiveData.value?.playlist ?: return
        viewModelScope.launch {
            deletePlaylistUseCase.execute(playlist).collect { /* no-op */ }
        }
    }

    private fun calculateTotalDuration(tracks: List<Track>): Long {
        val totalDurationMillis: Long = tracks.sumOf {
            val duration = trackTimeFormatter.formatToLong(it.formattedDuration)
            duration
        }
        return totalDurationMillis / (1000 * 60)
    }
}