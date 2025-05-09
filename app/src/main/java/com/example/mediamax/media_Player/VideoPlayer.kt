package com.example.mediamax.media_Player

import android.os.Bundle
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediamax.R
import com.example.mediamax.databinding.ActivityVideoPlayerBinding

class VideoPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val videoUri = intent.getStringExtra("VIDEO_URI")
        mediaController = MediaController(this)
        binding.videoView.setVideoPath(videoUri)
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.start()
    }
}

