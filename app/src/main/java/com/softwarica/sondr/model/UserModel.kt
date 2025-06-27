package com.softwarica.sondr.model

data class UserModel(
    val userID: String = "",
    val username: String = "",
    val name: String = "",
    val sondrCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class PostModel(
    val postID: String = "",
    val text: String = "",
    val authorID: String = "",
    val likes: Int = 0,
    val nsfw: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

