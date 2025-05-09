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

class AudioRepository @Inject constructor(private val context: Context) {

    private val _audiosLiveData= MutableLiveData<List<AppData>>()
    val audiosLiveData: LiveData<List<AppData>> get()=_audiosLiveData

    private val _foldersLiveData = MutableLiveData<List<Pair<String, Int>>>()
    val foldersLiveData: LiveData<List<Pair<String, Int>>> get() = _foldersLiveData

    private val repositoryScope= CoroutineScope(Dispatchers.IO)
    init{
        loadAudioFromStorage()
    }
    private fun loadAudioFromStorage() {
        repositoryScope.launch {
            val AudioList = mutableListOf<AppData>()
            val uri = MediaStore.Audio.Media.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME
            )
            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                "${MediaStore.Audio.Media.DURATION} DESC"
            )

            cursor?.use {
                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)

                while (it.moveToNext()) {
                    val path = it.getString(pathColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getLong(sizeColumn)
                    val folderName = it.getString(folderNameColumn) ?: "Unknown"

                    val durationMillis = it.getLong(durationColumn)
                    val minutes = durationMillis / 1000 / 60
                    val seconds = (durationMillis / 1000) % 60
                    val durationFormatted = String.format("%d:%02d", minutes, seconds)
                    AudioList.add(AppData(path, name, durationFormatted, size, MediaType.AUDIO,folderName))
                }
            }
            _audiosLiveData.postValue(AudioList)
        }
    }
    fun loadAudioFolders() {
        repositoryScope.launch {
            val folderMap = mutableMapOf<String, Int>()
            val uri = MediaStore.Audio.Media.getContentUri("external")
            val projection = arrayOf(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)

            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            cursor?.use {
                val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)

                while (it.moveToNext()) {
                    val folderName = it.getString(folderNameColumn)
                    if (!folderName.isNullOrEmpty()) {

                        folderMap[folderName] = folderMap.getOrDefault(folderName, 0) + 1
                    }
                }
            }


            val folderList = folderMap.map { it.key to it.value }

            _foldersLiveData.postValue(folderList)
        }
    }
}