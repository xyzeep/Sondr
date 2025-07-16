package com.softwarica.sondr.model

data class PostModel(
    val postID: String = "",
    val text: String = "",
    val authorID: String = "",
    val likes: Int = 0,
    val nsfw: Boolean = false,
    val private: Boolean = false,
    val mediaURL: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)