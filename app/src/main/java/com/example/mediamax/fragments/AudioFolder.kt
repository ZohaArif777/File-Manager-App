package com.example.mediamax.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamax.R
import com.example.mediamax.adapter.FolderAdapter
import com.example.mediamax.model.MediaType
import com.example.mediamax.repositories.AudioRepository
import com.example.mediamax.repositories.VideoRepository


class AudioFolder : Fragment() {

    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var repository: AudioRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_audio_folder, container, false)


        folderRecyclerView = view.findViewById(R.id.audioFolder)
        folderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        repository = AudioRepository(requireContext())
        repository.foldersLiveData.observe(viewLifecycleOwner, { folderList ->
            if (folderList.isNotEmpty()) {

                folderAdapter = FolderAdapter(folderList, MediaType.AUDIO) { folderName ->
                    loadAudioFromFolder()
                }
                folderRecyclerView.adapter = folderAdapter
            }
        })


        repository.loadAudioFolders()

        return view
    }

    private fun loadAudioFromFolder() {
        repository.loadAudioFolders()
    }
}