package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.db.entity.AppDatabase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackHistoryRepositoryImpl(private val sharedPref: SharedPreferences,
                                 private val gson: Gson,
                                 private val appDatabase: AppDatabase
): SearchHistoryRepository {

    override fun saveTrackHistoryList(listTrack: MutableList<Track>) {
        val gsonFromListMusic = gson.toJson(listTrack)
        sharedPref.edit()
            .putString(UNIQ_TRACKS_KEY, gsonFromListMusic)
            .apply()
    }

    override  fun getTrackHistoryList(): Flow<MutableList<Track>> = flow {
        val historyJson: String? = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        val listTrack = if (historyJson == null) {
            mutableListOf()
        } else {
            gson.fromJson(historyJson, Array<Track>::class.java).toMutableList()
        }
        val favoritesTracksId = appDatabase.trackDao().getAllTrackIds()
        listTrack.forEach { track ->
            if (favoritesTracksId.contains(track.trackId)) {
                track.isFavorite = true
            }
        }
        emit(listTrack)
    }


    override fun clearHistory() {
        sharedPref.edit().clear().apply()
    }

    companion object {
        private const val UNIQ_TRACKS_KEY = "key_for_uniq_keys"
    }
}