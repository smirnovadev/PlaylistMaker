package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson


class SearchHistory(val sharedPref: SharedPreferences) {

    fun saveTrack(track: Music) {
        val historyJson: String? = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        var listMusic = if (historyJson == null) {
            mutableListOf()
        } else {
            Gson().fromJson(historyJson, Array<Music>::class.java).toMutableList()
        }

        listMusic.add(track)
        listMusic = getUniqueMusicList(listMusic)
        if (listMusic.size > MAX_TRACKS) {
            listMusic.removeAt(0)
        }

        val gsonFromListMusic = Gson().toJson(listMusic)
        sharedPref.edit()
            .putString(UNIQ_TRACKS_KEY, gsonFromListMusic)
            .apply()
    }

    fun getAllTracks(): List<Music> {
        val historyJson = sharedPref.getString(UNIQ_TRACKS_KEY, null)
        return if (historyJson == null) {
            listOf()
        } else {
            Gson().fromJson(historyJson, Array<Music>::class.java).toList().reversed()
        }
    }

    fun clearHistory() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    private fun getUniqueMusicList(list: List<Music>): MutableList<Music> {
        return list.distinctBy { it.trackId }.toMutableList()
    }



    companion object {
        private const val MAX_TRACKS = 10
        private const val UNIQ_TRACKS_KEY = "key_for_uniq_keys"
    }
}