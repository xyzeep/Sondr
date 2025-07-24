package com.softwarica.sondr.repository

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.softwarica.sondr.model.NotificationModel

class NotificationRepositoryImpl(private val context: Context): NotificationRepository {

    private val notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")


    override fun saveNotification(
        notification: NotificationModel,
        callback: (Boolean, String) -> Unit
    ) {
        val newId = notificationsRef.push().key
        if (newId == null) {
            callback(false, "Failed to generate notification ID")
            return
        }
        val notifToSave = notification.copy(id = newId)
        notificationsRef.child(newId).setValue(notifToSave)
            .addOnSuccessListener { callback(true, "Notification saved") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to save notification") }
    }

    override fun getNotificationsForUser(
        userId: String,
        callback: (Boolean, String, List<NotificationModel>) -> Unit
    ) {
        notificationsRef.orderByChild("toUserId").equalTo(userId)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notifications = snapshot.children.mapNotNull {
                        it.getValue(NotificationModel::class.java)
                    }
                    callback(true, "Notifications fetched", notifications)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }
}
