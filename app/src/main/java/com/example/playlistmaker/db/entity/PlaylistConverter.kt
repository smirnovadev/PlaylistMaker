package com.example.playlistmaker.db.entity

import androidx.room.TypeConverter
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTrackIdList(trackIds: List<Long>): String {
        return gson.toJson(trackIds)
    }

    @TypeConverter
    fun toTrackIdList(trackIdJson: String): List<Long> {
        return gson.fromJson(trackIdJson, object : TypeToken<List<Long>>() {}.type)
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            descriptionPlaylist = playlist.description,
            coverArtPath = playlist.coverArtPath,
            trackIdList = fromTrackIdList(playlist.trackIdList),
            numberTracks = playlist.numberTracks
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            description = playlist.descriptionPlaylist,
            coverArtPath = playlist.coverArtPath,
            trackIdList = toTrackIdList(playlist.trackIdList),
            numberTracks = playlist.numberTracks
        )
    }
}