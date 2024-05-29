package com.example.playlistmaker.search.data.mapper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TrackTimeFormatter {
    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    fun formatToString(trackDuration: Int): String {
        return formatter.format(Date(trackDuration.toLong()))
    }

    fun formatToLong(trackTimeMillis: String): Long {
        val parsedDate = formatter.parse(trackTimeMillis)
        if (parsedDate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate
            return calendar.get(Calendar.MINUTE) * 60 * 1000L + calendar.get(Calendar.SECOND) * 1000L
        }
        return 0L
    }
}