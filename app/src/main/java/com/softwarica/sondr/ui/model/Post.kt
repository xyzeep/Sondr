package com.softwarica.sondr.ui.model

enum class PostType {
    WHISPR,
    SNAPSHOT
}

data class Post(
    val postId: String,
    val time: String,
    val caption: String,
    val type: PostType,
    val mediaRes: Int,
    val likes: Int,
    val author: String
)
