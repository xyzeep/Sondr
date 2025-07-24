package com.softwarica.sondr.model

// This is how the users details are stored in the firebase realtime database.
data class UserModel(
    val userID: String = "",
    val username: String = "",
    val bio: String = "",
    val sondrCode: String = "",
    val followers: Int = 0,
    val posts: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)



