package com.example.playlistmaker.db.entity



import com.example.playlistmaker.search.domain.model.Track

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            artistName = track.artistName,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = track.artworkUrl512,
            collectionName = track.collectionName,
            country = track.country,
            trackName = track.trackName,
            previewUrl = track.previewUrl,
            formattedDuration = track.formattedDuration,
            primaryGenreName = track.primaryGenreName,
            releaseYear = track.releaseYear,
            createdTime = System.currentTimeMillis())
    }

    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.trackId,
            artistName = track.artistName,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl512 = track.artworkUrl512,
            collectionName = track.collectionName,
            country = track.country,
            trackName = track.trackName,
            previewUrl = track.previewUrl,
            formattedDuration = track.formattedDuration,
            primaryGenreName = track.primaryGenreName,
            releaseYear = track.releaseYear

        )
    }
}