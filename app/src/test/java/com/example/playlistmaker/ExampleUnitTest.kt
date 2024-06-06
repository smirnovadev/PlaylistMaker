package com.example.playlistmaker

import com.example.playlistmaker.db.entity.PlaylistEntity
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val gson = Gson()

    private val allPlaylists = listOf(
        PlaylistEntity(
            playlistId = 1,
            playlistName = "my_songs",
            descriptionPlaylist = "",
            coverArtPath = "",
            trackIdList = "",
            numberTracks = 0
        )
    )
    @Test
    fun addition_isCorrect() {
        val trackId = 1
        val trackIdString = gson.toJson(trackId)
        assertEquals(trackIdString, trackId.toString())
        val tracksId = gson.toJson(listOf(11, 12, 13))
        println(tracksId)

        "[11, 12, 13]".contains("1")

        assertTrue(tracksId.contains(trackIdString))

    }
}