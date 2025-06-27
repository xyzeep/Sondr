package com.softwarica.sondr.model

data class UserModel(
    val userID: String = "",
    val username: String = "",
    val sondrCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
