package com.example.playlistmaker.createPlaylist.domain

import com.example.playlistmaker.createPlaylist.domain.model.DomainImage

interface SaveImageToPrivateStorageUseCase {
    suspend fun execute(image: DomainImage): String

}