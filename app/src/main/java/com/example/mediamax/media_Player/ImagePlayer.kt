package com.example.mediamax.media_Player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mediamax.R
import com.example.mediamax.databinding.ActivityImagePlayerBinding

class ImagePlayer : AppCompatActivity() {
    private lateinit var binding: ActivityImagePlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imagePath = intent.getStringExtra("IMAGE_PATH")

        Glide.with(this)
            .load(imagePath)
            .into(binding.imgview)
    }
}