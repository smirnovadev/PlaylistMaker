package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.playlist.domain.model.DomainImage

interface ImageStorageRepository {

    suspend fun saveImageToPrivateStorage(image: DomainImage): String
}