package com.example.mediamax.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediamax.R
import com.example.mediamax.model.MediaType

class FolderAdapter(
    private val folders: List<Pair<String, Int>>,
    private val mediaType: MediaType, // Accept media type to adjust behavior
    private val onFolderClick: (String) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_list, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val (folderName, itemCount) = folders[position]
        holder.bind(folderName, itemCount)
    }

    override fun getItemCount(): Int = folders.size

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val folderNameTextView: TextView = itemView.findViewById(R.id.folderText)
        private val itemCountTextView: TextView = itemView.findViewById(R.id.path)

        fun bind(folderName: String, itemCount: Int) {
            folderNameTextView.text = folderName

            // Adjust text based on media type
            val mediaTypeText = when (mediaType) {
                MediaType.IMAGE -> "$itemCount images"
                MediaType.AUDIO -> "$itemCount audios"
                MediaType.VIDEO -> "$itemCount videos"
            }
            itemCountTextView.text = mediaTypeText

            itemView.setOnClickListener {
                onFolderClick(folderName)
            }
        }
    }
}
