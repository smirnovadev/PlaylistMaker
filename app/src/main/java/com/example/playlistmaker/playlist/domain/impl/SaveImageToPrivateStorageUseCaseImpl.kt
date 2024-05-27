package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.playlist.domain.ImageStorageRepository
import com.example.playlistmaker.playlist.domain.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.playlist.domain.model.DomainImage

class SaveImageToPrivateStorageUseCaseImpl(
    private val imageStorageRepository: ImageStorageRepository
) : SaveImageToPrivateStorageUseCase {
    override suspend fun execute(image: DomainImage): String {
        return imageStorageRepository.saveImageToPrivateStorage(image)
    }
}