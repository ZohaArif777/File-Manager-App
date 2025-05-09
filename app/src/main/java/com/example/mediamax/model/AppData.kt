package com.example.mediamax.model

data class AppData(
    val path:String="",
    val picname:String="",
    val duration: String? ="",
    val size:Long=0,
    val type: MediaType,
    var folderName:String?="",
    var relativePath:String?="",
    var isFavourite: Boolean = false

)
enum class MediaType {
    IMAGE, VIDEO,AUDIO
}
