package com.softwarica.sondr.model

data class NotificationModel(
    val id: String = "",
    val type: String = "",
    val fromUserId: String = "",
    val toUserId: String = "",
    val postId: String? = null,
    val message: String = "",
    val timestamp: Long = 0L,
    val read: Boolean = false
)
