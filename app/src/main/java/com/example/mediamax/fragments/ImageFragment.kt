package com.example.mediamax.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediamax.adapter.ImageAdapter
import com.example.mediamax.databinding.FragmentImageBinding
import com.example.mediamax.media_Player.ImagePlayer
import com.example.mediamax.model.AppData
import com.example.mediamax.roomDatabase.Favourite
import com.example.mediamax.roomDatabase.FavouriteDatabase
import com.example.mediamax.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ImageFragment : Fragment(), ImageAdapter.FilesListener {

    private lateinit var binding: FragmentImageBinding
    private lateinit var imageAdapter: ImageAdapter
    private val viewModel: ViewModel by viewModels()
    private var imageList: List<AppData> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeImages()
        return binding.root
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter(emptyList(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = imageAdapter
        }
    }

    private fun observeImages() {
        viewModel.images.observe(viewLifecycleOwner) { images ->
            lifecycleScope.launch(Dispatchers.IO) {
                val favList = FavouriteDatabase.getDatabase(requireContext()).favouriteDao()
                    .getFavouriteData()
                val updatedList = images.map { item ->
                    item.copy(isFavourite = favList.any { it.path == item.path })

                }
                withContext(Dispatchers.Main) {
                    imageList = updatedList
                    imageAdapter.setData(updatedList)
                }
            }
        }
    }

    override fun onClick(pos: Int) {
    }

    override fun onFavClick(pos: Int, isFavourite: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val favDao = FavouriteDatabase.getDatabase(requireContext()).favouriteDao()
            if (isFavourite) {
                favDao.insertFavorite(
                    Favourite(
                        path = imageList[pos].path,
                        type = "IMAGES",
                        size = imageList[pos].size,
                        picname = imageList[pos].picname,
                        duration = imageList[pos].duration
                    )
                )
            } else {
                favDao.deleteFavorite(imageList[pos].path)
            }
        }
    }

}
