package com.softwarica.sondr.view.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.model.NotificationModel
import com.softwarica.sondr.model.UserModel
import com.softwarica.sondr.repository.NotificationRepositoryImpl
import com.softwarica.sondr.repository.UserRepositoryImpl
import com.softwarica.sondr.ui.components.NotificationItem

@Composable
fun NotificationPage() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("sondr_prefs", Context.MODE_PRIVATE)
    val savedUsername = sharedPreferences.getString("currentUsername", "") ?: ""

    var userModel by remember { mutableStateOf<UserModel?>(null) }
    val userRepository = remember { UserRepositoryImpl(context) }

    var notifications by remember { mutableStateOf<List<NotificationModel>>(emptyList()) }
    val notificationRepository = remember { NotificationRepositoryImpl(context) }


    LaunchedEffect(Unit) {
        userRepository.getCurrentUserInfo { success, message, user ->
            if (success && user != null) {
                userModel = user

                // Fetch notifications for this user
                notificationRepository.getNotificationsForUser(user.userID) { notifSuccess, notifMsg, notifList ->
                    if (notifSuccess) {
                        notifications = notifList
                    } else {
                        Log.e("NotificationScreen", "Failed to fetch notifications: $notifMsg")
                    }
                }
            } else {
                Log.e("NotificationScreen", "Failed to fetch user info: $message")
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .background(colorResource(id = R.color.background))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            item {
                Text(
                    text = "Notifications",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            if (notifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "You have no notifications yet.",
                            color = Color.LightGray,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                items(notifications) { notification ->
                    NotificationItem(notification)
                }
            }


        }
    }
}

@Composable
@Preview
fun NotificationScreenPreview() {
    NotificationPage()
}