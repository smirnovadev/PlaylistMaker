package com.example.playlistmaker.playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.playlist.domain.ImageStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageStorageRepositoryImpl(private val context: Context): ImageStorageRepository {
    override suspend fun saveImageToPrivateStorage(uri: Uri): String = withContext(Dispatchers.IO) {
        val albumName = "myalbum"
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(filePath, fileName)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }
        }
        return@withContext file.absolutePath
    }
}