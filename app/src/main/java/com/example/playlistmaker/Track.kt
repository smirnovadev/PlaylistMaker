package com.example.playlistmaker

import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : Serializable

fun Track.formattedDuration(): String {
    val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    return formatter.format(this.trackTimeMillis)
}
fun Track.getCoverArtwork(): String {
    return this.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}
fun Track.extractYear(): String {
    return try {
        LocalDate.parse(this.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
    } catch (e: Exception) {
        " "
    }
}