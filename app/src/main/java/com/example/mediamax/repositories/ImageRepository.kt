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

class Repository @Inject constructor(
    private val context: Context
) {
    private val _foldersLiveData = MutableLiveData<List<Pair<String, Int>>>()
    val foldersLiveData: LiveData<List<Pair<String, Int>>> get() = _foldersLiveData

    private val _imagesLiveData = MutableLiveData<List<AppData>>()
    val imagesLiveData: LiveData<List<AppData>> get() = _imagesLiveData

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        loadAllImageFolders()
    }

    fun loadAllImageFolders() {
        repositoryScope.launch {
            val imageList = mutableListOf<AppData>()
            val folderMap = mutableMapOf<String, Int>()
            val uri = MediaStore.Images.Media.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            )

            val cursor = context.contentResolver.query(
                uri, projection, null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )

            cursor?.use {
                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val folderNameColumn =
                    it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                while (it.moveToNext()) {
                    val path = it.getString(pathColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getLong(sizeColumn)
                    val folderName = it.getString(folderNameColumn) ?: "Unknown"

                    imageList.add(AppData(path, name, folderName, size, MediaType.IMAGE))
                    folderMap[folderName] = folderMap.getOrDefault(folderName, 0) + 1
                }
            }

            _imagesLiveData.postValue(imageList)

            val folderList = folderMap.map { it.key to it.value }
            _foldersLiveData.postValue(folderList)
        }
    }
}


//    fun loadImagesFromFolder(folderName: String) {
//        repositoryScope.launch {
//            val imageList = mutableListOf<AppData>()
//            val uri = MediaStore.Images.Media.getContentUri("external")
//            val projection = arrayOf(
//                MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
//            )
//
//            val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
//            val selectionArgs = arrayOf(folderName)
//
//            val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
//
//            cursor?.use {
//                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
//
//                while (it.moveToNext()) {
//                    val path = it.getString(pathColumn)
//                    val name = it.getString(nameColumn)
//                    val size = it.getLong(sizeColumn)
//                    imageList.add(AppData(path, name, folderName, size, MediaType.IMAGE))
//                }
//            }
//            _imagesLiveData.postValue(imageList)
//        }
//    }

