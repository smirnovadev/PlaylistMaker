package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson


class TrackHistoryRepositoryImpl(private val sharedPref: SharedPreferences,
                                 private val gson: Gson): SearchHistoryRepository {

    override fun saveTrackHistoryList(listTrack: MutableList<Track>) {
        val gsonFromListMusic = gson.toJson(listTrack)
        sharedPref.edit()
            .putString(UNIQ_TRACKS_KEY, gsonFromListMusic)
            .apply()
    }

    override fun getTrackHistoryList(): MutableList<Track> {
        val historyJson: String? = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        val listTrack = if (historyJson == null) {
            mutableListOf()
        } else {
            gson.fromJson(historyJson, Array<Track>::class.java).toMutableList()
        }
        return listTrack
    }

    override fun clearHistory() {
        sharedPref.edit().clear().apply()
    }

    companion object {
        private const val UNIQ_TRACKS_KEY = "key_for_uniq_keys"
    }
}