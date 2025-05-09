package com.example.mediamax.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamax.R
import com.example.mediamax.adapter.FolderAdapter
import com.example.mediamax.model.MediaType
import com.example.mediamax.repositories.Repository

class FolderFragment : Fragment() {

    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folder, container, false)

        folderRecyclerView = view.findViewById(R.id.folderrecy)
        folderRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        repository = Repository(requireContext())
      repository.foldersLiveData.observe(viewLifecycleOwner, Observer { folderList ->
            if (folderList.isNotEmpty()) {
                folderAdapter = FolderAdapter(folderList, MediaType.IMAGE) { folderName ->
                    // Handle folder selection if needed
                }
                folderRecyclerView.adapter = folderAdapter
            }
        })

        repository.loadAllImageFolders()

        return view
    }
}
