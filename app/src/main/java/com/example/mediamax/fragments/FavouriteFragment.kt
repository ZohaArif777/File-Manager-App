package com.example.mediamax.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediamax.R
import com.example.mediamax.adapter.ImageAdapter
import com.example.mediamax.databinding.FragmentFavouriteBinding
import com.example.mediamax.model.AppData
import com.example.mediamax.model.MediaType
import com.example.mediamax.roomDatabase.Favourite
import com.example.mediamax.roomDatabase.FavouriteDatabase
import com.example.mediamax.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class FavouriteFragment: Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var mediaAdapter: ImageAdapter
    private val viewModel: ViewModel by viewModels()
    private var mediaList: MutableList<AppData> = mutableListOf()

    abstract val mediaType: String
    abstract fun getPlayerIntent(path: String): Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaAdapter = ImageAdapter(
            emptyList(),
            object : ImageAdapter.FilesListener {
                override fun onClick(pos: Int) {
                    val mediaPath = mediaList[pos].path
                    val intent = getPlayerIntent(mediaPath)
                    startActivity(intent)
                }

                override fun onFavClick(pos: Int, isFavourite: Boolean) {
                }
            },
            showFavoriteIcon = false
        )

        binding.favrecy.layoutManager = LinearLayoutManager(requireContext())
        binding.favrecy.adapter = mediaAdapter

        loadMedia()
    }

    private fun loadMedia() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favList =
                FavouriteDatabase.getDatabase(requireContext()).favouriteDao().getFavouriteData()
            val filteredList = favList.filter { it.type == mediaType }
            val appDataList = filteredList.map { it.toAppData() }

            withContext(Dispatchers.Main) {
                mediaList = appDataList.toMutableList()
                mediaAdapter.setData(appDataList)
            }
        }
    }

    private fun Favourite.toAppData(): AppData {
        return AppData(
            path = this.path,
            picname = this.picname,
            size = this.size,
            duration = this.duration,
            type = when (this.type) {
                "IMAGES" -> MediaType.IMAGE
                "VIDEO" -> MediaType.VIDEO
                else -> MediaType.AUDIO

            },
            isFavourite = true
        )
    }
}
