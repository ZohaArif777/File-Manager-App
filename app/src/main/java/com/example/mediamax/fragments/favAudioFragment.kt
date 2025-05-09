package com.example.mediamax.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediamax.R
import com.example.mediamax.media_Player.AudioPlayer

class FavAudioFragment : FavouriteFragment() {
    override val mediaType: String = "AUDIO"

    override fun getPlayerIntent(path: String): Intent {
        return Intent(requireContext(), AudioPlayer::class.java).apply {
            putExtra("audioFile", path)
        }
    }
}
