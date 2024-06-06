package com.example.playlistmaker.createPlaylist.domain

import com.example.playlistmaker.createPlaylist.domain.model.DomainImage

interface ImageStorageRepository {

    suspend fun saveImageToPrivateStorage(image: DomainImage): String
}