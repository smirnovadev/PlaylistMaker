package com.example.playlistmaker.playlist.domain.impl

import android.net.Uri
import com.example.playlistmaker.playlist.domain.ImageStorageRepository
import com.example.playlistmaker.playlist.domain.SaveImageToPrivateStorageUseCase

class SaveImageToPrivateStorageUseCaseImpl(
    private val imageStorageRepository: ImageStorageRepository
) : SaveImageToPrivateStorageUseCase {
    override suspend fun execute(uri: Uri): String {
        return imageStorageRepository.saveImageToPrivateStorage(uri)
    }
}