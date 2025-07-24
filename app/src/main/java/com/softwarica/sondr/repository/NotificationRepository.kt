package com.softwarica.sondr.repository

import com.softwarica.sondr.model.NotificationModel

interface NotificationRepository {
    fun saveNotification(notification: NotificationModel, callback: (Boolean, String) -> Unit)
    fun getNotificationsForUser(userId: String, callback: (Boolean, String, List<NotificationModel>) -> Unit)
}
