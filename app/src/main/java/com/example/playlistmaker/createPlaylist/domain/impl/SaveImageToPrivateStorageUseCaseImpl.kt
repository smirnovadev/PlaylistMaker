package com.example.playlistmaker.createPlaylist.domain.impl

import com.example.playlistmaker.createPlaylist.domain.ImageStorageRepository
import com.example.playlistmaker.createPlaylist.domain.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.createPlaylist.domain.model.DomainImage

class SaveImageToPrivateStorageUseCaseImpl(
    private val imageStorageRepository: ImageStorageRepository
) : SaveImageToPrivateStorageUseCase {
    override suspend fun execute(image: DomainImage): String {
        return imageStorageRepository.saveImageToPrivateStorage(image)
    }
}