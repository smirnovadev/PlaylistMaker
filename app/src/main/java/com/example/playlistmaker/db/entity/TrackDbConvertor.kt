package com.example.playlistmaker.db.entity



import com.example.playlistmaker.search.domain.model.Track

class TrackDbConvertor {
    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
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

    fun map(track: FavoriteTrackEntity): Track {
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

    fun mapToTracksForPlaylistEntity(track: Track): TracksForPlaylistEntity {
        return TracksForPlaylistEntity(
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
    fun mapToTrack(trackForPlaylist: TracksForPlaylistEntity): Track {
        return Track(
            trackId = trackForPlaylist.trackId,
            artistName = trackForPlaylist.artistName,
            artworkUrl100 = trackForPlaylist.artworkUrl100,
            artworkUrl512 = trackForPlaylist.artworkUrl512,
            collectionName = trackForPlaylist.collectionName,
            country = trackForPlaylist.country,
            trackName = trackForPlaylist.trackName,
            previewUrl = trackForPlaylist.previewUrl,
            formattedDuration = trackForPlaylist.formattedDuration,
            primaryGenreName = trackForPlaylist.primaryGenreName,
            releaseYear = trackForPlaylist.releaseYear

        )
    }
}