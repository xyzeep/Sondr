package com.softwarica.sondr.model

enum class PostType {
    SNAPSHOT,
    WHISPR
}

data class PostModel(
    val postID: String = "",
    val authorID: String = "",
    val author: String = "",
    val type: PostType =  PostType.SNAPSHOT,
    val caption: String = "",
    val likes: Int = 0,
    val nsfw: Boolean = false,
    val isPrivate: Boolean = false,
    val mediaRes: String? = null,
    val likedBy: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)