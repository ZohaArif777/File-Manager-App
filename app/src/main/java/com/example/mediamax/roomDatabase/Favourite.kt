package com.example.mediamax.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val path:String="",
    val picname:String="",
    val duration: String? ="",
    val size:Long=0,
    val type: String,
)
