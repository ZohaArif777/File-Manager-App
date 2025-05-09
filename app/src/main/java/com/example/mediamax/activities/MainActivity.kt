package com.example.mediamax.activities

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
//
//    private val viewModel: ViewModel by viewModels()
//    private lateinit var imageAdapter: ImageAdapter
//    private lateinit var binding: ActivityMainBinding
//    private var filesList: List<AppData> = emptyList()
//    private var dataType: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.recy.layoutManager = LinearLayoutManager(this)
//        dataType = intent.getStringExtra("DATA_TYPE")
//
//        imageAdapter = ImageAdapter(emptyList(), object : ImageAdapter.FilesListener {
//            override fun onClick(pos: Int) {
//                openMediaPlayer(pos)
//            }
//
//            override fun onFavClick(pos: Int, isFavourite: Boolean) {
//                handleFavouriteClick(pos, isFavourite)
//            }
//        })
//        binding.recy.adapter = imageAdapter
//        when (dataType) {
//            "Images" -> observeData(viewModel.images, "Images")
//            "Video" -> observeData(viewModel.videos, "Videos")
//            "Audio" -> observeData(viewModel.audios, "Audios")
//        }
//    }
//
//    private fun observeData(liveData: androidx.lifecycle.LiveData<List<AppData>>, title: String) {
//
//        binding.Title.text = title
//        liveData.observe(this) { dataList ->
//            CoroutineScope(Dispatchers.IO).launch {
//                val favList = FavouriteDatabase.getDatabase(this@MainActivity).favouriteDao()
//                    .getFavouriteData()
//                val updatedList = dataList.map { item ->
//                    item.copy(isFavourite = favList.any { it.path == item.path })
//                }
//                runOnUiThread {
//                    filesList = updatedList
//                    imageAdapter.setData(updatedList)
//                }
//            }
//        }
//    }
//
//    private fun openMediaPlayer(pos: Int) {
//        val intent = when (dataType) {
//            "Images" -> Intent(this, ImagePlayer::class.java).apply {
//                putExtra("IMAGE_PATH", filesList[pos].path)
//            }
//
//            "Video" -> Intent(this, VideoPlayer::class.java).apply {
//                putExtra("VIDEO_URI", filesList[pos].path)
//            }
//
//            else -> Intent(this, AudioPlayer::class.java).apply {
//                putExtra("audioFile", filesList[pos].path)
//            }
//        }
//        startActivity(intent)
//    }
//
//    private fun handleFavouriteClick(pos: Int, isFavourite: Boolean) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val favDao = FavouriteDatabase.getDatabase(this@MainActivity).favouriteDao()
//            if (isFavourite) {
//                favDao.insertFavorite(
//                    Favourite(
//                        path = filesList[pos].path,
//                        type = when (dataType) {
//                            "Images" -> "IMAGES"
//                            "Video" -> "VIDEO"
//                            else -> "AUDIO"
//                        },
//                        size = filesList[pos].size,
//                        picname = filesList[pos].picname,
//                        duration = filesList[pos].duration
//                    )
//                )
//            } else {
//                favDao.deleteFavorite(filesList[pos].path)
//            }
//        }
//    }
//
}