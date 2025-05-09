package com.example.mediamax.fragments

import android.content.Intent
import com.example.mediamax.media_Player.VideoPlayer

class FavVideoFragment : FavouriteFragment() {
    override val mediaType: String = "VIDEO"

    override fun getPlayerIntent(path: String): Intent {
        return Intent(requireContext(), VideoPlayer::class.java).apply {
            putExtra("VIDEO_URI", path)
        }
    }
}
