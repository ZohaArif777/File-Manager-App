package com.example.mediamax.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mediamax.R
import com.example.mediamax.adapter.FragmentAdapter
import com.example.mediamax.databinding.ActivityDashboardBinding
import com.example.mediamax.fragments.*
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapter: FragmentAdapter
    private var currentCategory = "Images"
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager("Images")
        setupBottomNav()
    }

    private fun setupViewPager(category: String) {
        currentCategory = category

        val fragments = when (category) {
            "Images" -> listOf(ImageFragment(), FolderFragment(), FavImageFragment())
            "Videos" -> listOf(VideoFragment(), VideoFolder(), FavVideoFragment())
            "Audios" -> listOf(AudioFragment(), AudioFolder(), FavAudioFragment())
            else -> listOf(ImageFragment(), FolderFragment(), FavImageFragment())
        }

        adapter = FragmentAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> category
                1 -> "Folders"
                2 -> "Favorites"
                else -> "Unknown"
            }
        }.attach()
    }

    private fun setupBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_images -> setupViewPager("Images")
                R.id.nav_videos -> setupViewPager("Videos")
                R.id.nav_audios -> setupViewPager("Audios")
            }
            true
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    handler.postDelayed({
                        when (currentCategory) {
                            "Images" -> switchToCategory("Videos")
                            "Videos" -> switchToCategory("Audios")
                            "Audios" -> switchToCategory("Images") // Loop back
                        }
                    }, 300)
                }
            }
        })
    }

    private fun switchToCategory(category: String) {
        runOnUiThread {
            setupViewPager(category)
            val itemId = when (category) {
                "Images" -> R.id.nav_images
                "Videos" -> R.id.nav_videos
                "Audios" -> R.id.nav_audios
                else -> R.id.nav_images
            }
            binding.bottomNavigationView.selectedItemId = itemId
        }
    }
}
