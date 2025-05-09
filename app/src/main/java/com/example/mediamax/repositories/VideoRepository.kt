package com.example.mediamax.repositories

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediamax.model.AppData
import com.example.mediamax.model.MediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoRepository @Inject constructor(val context: Context) {

    private val _videosLiveData= MutableLiveData<List<AppData>>()
    val videosLiveData: LiveData<List<AppData>> get()=_videosLiveData

    private val _foldersLiveData = MutableLiveData<List<Pair<String, Int>>>()
    val foldersLiveData: LiveData<List<Pair<String, Int>>> get() = _foldersLiveData

    private val repositoryScope= CoroutineScope(Dispatchers.IO)

    init {
        loadVideosFromStorage()
    }
    private fun loadVideosFromStorage() {
        repositoryScope.launch {
            val videoList = mutableListOf<AppData>()
            val uri = MediaStore.Video.Media.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME
            )

            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                "${MediaStore.Video.Media.SIZE} ASC "
            )

            cursor?.use {
                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

                while (it.moveToNext()) {
                    val path = it.getString(pathColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getLong(sizeColumn)
                    val durationMillis = it.getLong(durationColumn)
                    val folderName = it.getString(folderNameColumn) ?: "Unknown"

                    val minutes = durationMillis / 1000 / 60
                    val seconds = (durationMillis / 1000) % 60
                    val durationFormatted = String.format("%d:%02d", minutes, seconds)
                    videoList.add(AppData(path, name, durationFormatted, size, MediaType.VIDEO,folderName))
                }
            }
            _videosLiveData.postValue(videoList)
        }
    }
    fun loadVideoFolders() {
        repositoryScope.launch {
            val folderMap = mutableMapOf<String, Int>() // Store folder names and their image counts
            val uri = MediaStore.Video.Media.getContentUri("external")
            val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            cursor?.use {
                val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

                while (it.moveToNext()) {
                    val folderName = it.getString(folderNameColumn)
                    if (!folderName.isNullOrEmpty()) {
                        // Count images for each folder
                        folderMap[folderName] = folderMap.getOrDefault(folderName, 0) + 1
                    }
                }
            }


            val folderList = folderMap.map { it.key to it.value }

            _foldersLiveData.postValue(folderList)
        }
    }
}