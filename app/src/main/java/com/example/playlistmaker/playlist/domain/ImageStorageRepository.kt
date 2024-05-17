package com.example.playlistmaker.playlist.domain

import android.net.Uri

interface ImageStorageRepository {

    suspend fun saveImageToPrivateStorage(uri: Uri): String
}