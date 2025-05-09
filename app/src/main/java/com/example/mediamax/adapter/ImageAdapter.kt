package com.example.mediamax.adapter

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediamax.R
import com.example.mediamax.databinding.ItemListBinding
import com.example.mediamax.media_Player.AudioPlayer
import com.example.mediamax.media_Player.ImagePlayer
import com.example.mediamax.media_Player.VideoPlayer
import com.example.mediamax.model.AppData
import com.example.mediamax.model.MediaType

class ImageAdapter(
    var mediaList: List<AppData>,
    var listener: FilesListener,
    private val showFavoriteIcon: Boolean = true
) : RecyclerView.Adapter<ImageAdapter.MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(mediaList[position])
    }

    override fun getItemCount(): Int = mediaList.size

    interface FilesListener {
        fun onClick(pos: Int)
        fun onFavClick(pos: Int, favourite: Boolean)
    }

    inner class MediaViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(appData: AppData) {
            when (appData.type) {
                MediaType.IMAGE -> {
                    Glide.with(binding.root.context)
                        .load(appData.path)
                        .into(binding.image)
                    binding.duration.text = ""
                    binding.duration.visibility = View.GONE
                }

                MediaType.VIDEO -> {
                    Glide.with(binding.root.context)
                        .load(appData.path)
                        .placeholder(R.drawable.videoplay)
                        .into(binding.image)
                    binding.duration.text = " ${appData.duration}"
                }

                MediaType.AUDIO -> {
                    val thumbnail = getAudioThumbnail(appData.path) ?: R.drawable.music
                    Glide.with(binding.root.context)
                        .load(thumbnail)
                        .into(binding.image)
                    binding.duration.text = " ${appData.duration}"
                }
            }

            val formattedSize = formatFileSize(appData.size)
            binding.size.text = " $formattedSize"
            binding.text.text = appData.picname

            if (showFavoriteIcon) {
                binding.imgButton.visibility = View.VISIBLE
                binding.imgButton.isSelected = appData.isFavourite
                binding.imgButton.setImageResource(
                    if (appData.isFavourite) R.drawable.mark else R.drawable.unmark
                )
                binding.imgButton.setOnClickListener {
                    appData.isFavourite = !appData.isFavourite
                    binding.imgButton.setImageResource(
                        if (appData.isFavourite) R.drawable.mark else R.drawable.unmark
                    )
                    listener.onFavClick(adapterPosition, appData.isFavourite)
                }
            } else {
                binding.imgButton.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {
                when (appData.type) {
                    MediaType.IMAGE -> openImage(appData.path)
                    MediaType.VIDEO -> openVideo(appData.path)
                    MediaType.AUDIO -> openAudio(appData.path)
                }
            }
        }

        private fun openImage(imagePath: String) {
            val intent = Intent(itemView.context, ImagePlayer::class.java)
            intent.putExtra("IMAGE_PATH", imagePath)
            itemView.context.startActivity(intent)
        }

        private fun openVideo(videoPath: String) {
            val intent = Intent(itemView.context, VideoPlayer::class.java)
            intent.putExtra("VIDEO_URI", videoPath)
            itemView.context.startActivity(intent)
        }

        private fun openAudio(audioPath: String) {
            val intent = Intent(itemView.context, AudioPlayer::class.java)
            intent.putExtra("audioFile", audioPath)
            itemView.context.startActivity(intent)
        }
    }

    fun setData(mediaList: List<AppData>) {
        this.mediaList = mediaList
        notifyDataSetChanged()
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

    private fun formatFileSize(sizeInBytes: Long): String {
        return when {
            sizeInBytes < 1024 -> "$sizeInBytes Bytes"
            sizeInBytes < 1024 * 1024 -> String.format("%.2f KB", sizeInBytes / 1024.0)
            sizeInBytes < 1024 * 1024 * 1024 -> String.format(
                "%.2f MB",
                sizeInBytes / (1024.0 * 1024)
            )

            else -> String.format("%.2f GB", sizeInBytes / (1024.0 * 1024 * 1024))
        }
    }
}