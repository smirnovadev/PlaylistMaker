package com.example.playlistmaker.search.data

import android.content.Context
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson


class TrackHistoryRepositoryImpl(context: Context): SearchHistoryRepository {

    private val sharedPref = context.getSharedPreferences(
        SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE
    )

    override fun saveTrackHistoryList(listTrack: MutableList<Track>) {
        val gsonFromListMusic = Gson().toJson(listTrack)
        sharedPref.edit()
            .putString(UNIQ_TRACKS_KEY, gsonFromListMusic)
            .apply()
    }

    override fun getTrackHistoryList(): MutableList<Track> {
        val historyJson: String? = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        val listTrack = if (historyJson == null) {
            mutableListOf()
        } else {
            Gson().fromJson(historyJson, Array<Track>::class.java).toMutableList()
        }
        return listTrack
    }

    override fun clearHistory() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
    companion object {
        private const val SEARCH_HISTORY_PREFERENCES = "key_for_search_history"
        private const val UNIQ_TRACKS_KEY = "key_for_uniq_keys"
    }
}