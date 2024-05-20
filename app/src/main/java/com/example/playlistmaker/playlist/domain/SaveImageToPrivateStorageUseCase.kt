package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.playlist.domain.model.DomainImage

interface SaveImageToPrivateStorageUseCase {
    suspend fun execute(image: DomainImage): String

}