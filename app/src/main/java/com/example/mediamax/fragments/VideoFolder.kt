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
import com.example.mediamax.repositories.Repository
import com.example.mediamax.repositories.VideoRepository

class VideoFolder : Fragment() {

    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var repository: VideoRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_folder, container, false)


        folderRecyclerView = view.findViewById(R.id.videoFolder)
        folderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        repository = VideoRepository(requireContext())
        repository.foldersLiveData.observe(viewLifecycleOwner, { folderList ->
            if (folderList.isNotEmpty()) {

                folderAdapter = FolderAdapter(folderList,MediaType.VIDEO) { folderName ->
                    loadVideosFromFolder()
                }
                folderRecyclerView.adapter = folderAdapter
            }
        })


        repository.loadVideoFolders()

        return view
    }

    private fun loadVideosFromFolder() {
        repository.loadVideoFolders()
    }
}