package com.example.mediamax.media_Player

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mediamax.R
import com.example.mediamax.databinding.ActivityAudioPlayerBinding
import java.util.concurrent.TimeUnit

class AudioPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val audioFile = intent.getStringExtra("audioFile")
        if (audioFile != null) {
            setupMediaPlayer(audioFile)
            loadAudioThumbnailAndDuration(audioFile)
        } else {
            binding.audioname.text = "No audio file provided."
            return
        }

        binding.btnPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                binding.btnPause.setImageResource(R.drawable.play)
            } else {
                mediaPlayer?.start()
                binding.btnPause.setImageResource(R.drawable.pause)
                updateSeekBar()
            }
        }

        binding.btnNext.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.prepare()
            binding.btnPause.setImageResource(R.drawable.play)
        }

        binding.audioSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        updateSeekBar()
    }

    private fun setupMediaPlayer(audioFile: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioFile)
            prepare()
            start()
        }

        binding.audioname.text = audioFile.substringAfterLast('/')
        mediaPlayer?.setOnPreparedListener {
            val duration = mediaPlayer?.duration ?: 0
            binding.audioSeekbar.max = duration
            binding.endDuration.text = formatDuration(duration)
            updateSeekBar()
        }
        mediaPlayer?.setOnCompletionListener {
            binding.btnPause.setImageResource(R.drawable.play)
            binding.audioSeekbar.progress = 0
        }
    }

    private fun loadAudioThumbnailAndDuration(filePath: String) {
        val thumbnail = getAudioThumbnail(filePath) ?: R.drawable.discs
        Glide.with(this)
            .load(thumbnail)
            .into(binding.audioimg)
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filePath)
        val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
        retriever.release()

        val durationText = if (durationMs != null) {
            "Duration: ${formatDuration(durationMs.toInt())}"
        } else {
            "Duration not available"
        }
        binding.endDuration.text = durationText
    }

    private fun getAudioThumbnail(filePath: String): ByteArray? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(filePath)
            val thumbnail = retriever.embeddedPicture
            retriever.release()
            thumbnail
        } catch (e: Exception) {
            null
        }
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            binding.audioSeekbar.progress = it.currentPosition
            binding.startDuration.text = formatDuration(it.currentPosition)
            if (it.isPlaying) {
                handler.postDelayed({ updateSeekBar() }, 500)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }

}
