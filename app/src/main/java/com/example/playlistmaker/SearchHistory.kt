package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson


class SearchHistory(val sharedPref: SharedPreferences) {

    fun saveTrack(track: Track) {
        val historyJson: String? = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        var listTrack = if (historyJson == null) {
            mutableListOf()
        } else {
            Gson().fromJson(historyJson, Array<Track>::class.java).toMutableList()
        }
        listTrack.removeAll { it.trackId == track.trackId }
        listTrack.add(track)


        if (listTrack.size > MAX_TRACKS) {
            listTrack.removeAt(0)
        }

        val gsonFromListMusic = Gson().toJson(listTrack)
        sharedPref.edit()
            .putString(UNIQ_TRACKS_KEY, gsonFromListMusic)
            .apply()
    }

    fun getAllTracks(): List<Track> {
        val historyJson = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        return if (historyJson == null) {
            listOf()
        } else {
            Gson().fromJson(historyJson, Array<Track>::class.java).toList().reversed()
        }
    }

    fun clearHistory() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }




    companion object {
        private const val MAX_TRACKS = 10
        private const val UNIQ_TRACKS_KEY = "key_for_uniq_keys"
    }
}