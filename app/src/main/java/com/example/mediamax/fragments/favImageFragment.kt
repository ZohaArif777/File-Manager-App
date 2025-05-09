package com.example.mediamax.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediamax.R
import com.example.mediamax.media_Player.ImagePlayer

class FavImageFragment : FavouriteFragment() {
    override val mediaType: String = "IMAGES"

    override fun getPlayerIntent(path: String): Intent {
        return Intent(requireContext(), ImagePlayer::class.java).apply {
            putExtra("IMAGE_PATH", path)
        }
    }
}
