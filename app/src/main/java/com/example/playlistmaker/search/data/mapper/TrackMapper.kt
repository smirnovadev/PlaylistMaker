package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class TrackMapper {
    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    fun map(dto: TrackDto): Track {
        val formattedDuration = formatter.format(dto.trackTimeMillis)
        val releaseYear: String = try {
            LocalDate.parse(dto.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
        } catch (e: DateTimeParseException) {
            Timber.e(e.cause, "error while parsing track")
            ""
        } catch (e: NullPointerException) {
            Timber.e("no release year for track id=${dto.trackId}")
            ""
        }
        val artworkUrl512 = dto.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        return Track(
            previewUrl = dto.previewUrl,
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            formattedDuration = formattedDuration,
            artworkUrl100 = dto.artworkUrl100,
            artworkUrl512 = artworkUrl512,
            collectionName = dto.collectionName,
            releaseYear = releaseYear,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country
        )

    }
}