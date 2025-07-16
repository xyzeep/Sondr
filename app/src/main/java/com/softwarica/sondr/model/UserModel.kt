package com.softwarica.sondr.model

data class UserModel(
    val userID: String = "",
    val username: String = "",
    val name: String = "",
    val sondrCode: String = "",
    val followers: Int = 0,
    val posts: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)



