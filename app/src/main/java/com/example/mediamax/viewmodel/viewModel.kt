package com.example.mediamax.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mediamax.model.AppData
import com.example.mediamax.model.MediaType
import com.example.mediamax.repositories.AudioRepository
import com.example.mediamax.repositories.Repository
import com.example.mediamax.repositories.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository,
    private val videorepo: VideoRepository,
    private val audiorepo: AudioRepository
) : ViewModel() {

    val images: LiveData<List<AppData>> get() = repository.imagesLiveData
    val videos: LiveData<List<AppData>> get()=videorepo.videosLiveData
    val audios: LiveData<List<AppData>> get()=audiorepo.audiosLiveData


}

