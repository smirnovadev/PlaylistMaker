package com.example.playlistmaker.playlist.domain

import android.net.Uri

interface SaveImageToPrivateStorageUseCase {
    suspend fun execute(uri: Uri): String

}