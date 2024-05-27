package com.example.playlistmaker.db.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.db.entity.dao.PlaylistDao
import com.example.playlistmaker.db.entity.dao.TrackDao
import com.example.playlistmaker.db.entity.dao.TracksForPlaylistDao

@Database(
    version = 4,
    entities = [TrackEntity::class, PlaylistEntity::class, TracksForPlaylistEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracksForPlaylistDao(): TracksForPlaylistDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE track_table ADD COLUMN createdTime INTEGER NOT NULL DEFAULT 0")
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS playlist_table (
                        playlistId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        playlistName TEXT NOT NULL,
                        descriptionPlaylist TEXT NOT NULL,
                        coverArtPath TEXT NOT NULL,
                        tracksId TEXT NOT NULL,
                        numberTracks INTEGER NOT NULL
                    )
                """.trimIndent()
                )
            }
        }
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS tracks_for_playlist_table (
                    trackId INTEGER PRIMARY KEY NOT NULL,
                    previewUrl TEXT,
                    trackName TEXT NOT NULL,
                    artistName TEXT NOT NULL,
                    formattedDuration TEXT NOT NULL,
                    artworkUrl100 TEXT NOT NULL,
                    artworkUrl512 TEXT NOT NULL,
                    collectionName TEXT,
                    releaseYear TEXT NOT NULL,
                    primaryGenreName TEXT NOT NULL,
                    country TEXT NOT NULL,
                    createdTime INTEGER NOT NULL
                )
            """.trimIndent()
                )
            }
        }
    }
}