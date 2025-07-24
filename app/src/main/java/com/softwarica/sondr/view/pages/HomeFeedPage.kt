package com.softwarica.sondr.view.pages

import android.os.Environment
import com.softwarica.sondr.model.PostModel
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.blur
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import com.softwarica.sondr.repository.PostRepository
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.softwarica.sondr.R
import com.softwarica.sondr.components.Loading
import com.softwarica.sondr.ui.components.PostItem
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.repository.PostRepositoryImpl
import com.softwarica.sondr.repository.UserRepository
import com.softwarica.sondr.repository.UserRepositoryImpl
import com.softwarica.sondr.utils.downloadFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedPage() {
    var selectedFilter by remember { mutableStateOf("All") }
    var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }


    var refreshLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val postRepository: PostRepository = remember { PostRepositoryImpl(context) }

    val userRepository: UserRepository = remember { UserRepositoryImpl(context) }
    var currentUserId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        postRepository.getAllPosts { success, message, result ->
            if (success) {
                posts = result.reversed() // newest first
            } else {
                Log.e("Firebase", "Error fetching posts: $message")
            }
        }
    }

    fun refreshPosts() {
        refreshLoading = true
        postRepository.getAllPosts { success, message, result ->
            if (success) {
                posts = result.reversed()
            } else {
                Log.e("Firebase", "Error fetching posts: $message")
            }
            refreshLoading = false
        }

    }
    LaunchedEffect(Unit) {
        refreshPosts()
    }
    LaunchedEffect(Unit) {
        userRepository.getCurrentUserInfo { success, message, user ->
            if (success && user != null) {
                currentUserId = user.userID
            } else {
                Log.e("User", "Error getting current user: $message")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xff121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Feed(
            posts = posts,
            selectedFilter = selectedFilter,
            onFilterChange = { selectedFilter = it },
            currentUserId = currentUserId,
            postRepository = postRepository,
            onRefreshPosts = { refreshPosts() },
        )

        Loading(isLoading = refreshLoading, message = "Refreshing posts...")
    }

    Loading(isLoading = refreshLoading, message = "")


}

// fun to load feed
@Composable
fun Feed(
    posts: List<PostModel>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    currentUserId: String?,
    postRepository: PostRepository,
    onRefreshPosts: () -> Unit,
) {
    val context = LocalContext.current
    val filterOptions = listOf("All", "Whisprs", "Snapshots")
    var fullscreenImageUri by remember { mutableStateOf<String?>(null) }
    val likedPosts = remember { mutableStateOf(setOf<String>()) }

    var deleteLoading by remember { mutableStateOf(false) }

    val filteredPosts = when (selectedFilter) {
        "Whisprs" -> posts.filter { it.type == PostType.WHISPR }
        "Snapshots" -> posts.filter { it.type == PostType.SNAPSHOT }
        else -> posts
    }

    Box(modifier = Modifier.fillMaxSize()) {  // Wrap LazyColumn + overlay in Box

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { chip ->
                        FilterChip(
                            selected = chip == selectedFilter,
                            onClick = { onFilterChange(chip) },
                            label = {
                                Text(
                                    text = chip,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White.copy(alpha = 0.18f),
                                labelColor = Color.White,
                                selectedContainerColor = Color.White,
                                selectedLabelColor = Color.Black
                            ),
                            border = null,
                        )
                    }
                }
            }

            items(filteredPosts, key = { it.postID }) { post ->
                PostItem(
                    post = post,
                    onRequestFullscreen = { uri -> fullscreenImageUri = uri },
                    onLikeToggle = { post, isNowLiked ->
                        if (currentUserId == null) return@PostItem

                        if (isNowLiked) {
                            postRepository.likePost(post.postID, currentUserId) { success, message ->
                                if (!success) Log.e("LikePost", message)
                            }
                        } else {
                            postRepository.unlikePost(post.postID, currentUserId) { success, message ->
                                if (!success) Log.e("UnlikePost", message)
                            }
                        }
                    },
                    currentUserId = currentUserId.toString(),
                    onDownload = { post ->
                        val url = post.mediaRes.toString()
                        val (fileName, mimeType, directory) = when (post.type) {
                            PostType.SNAPSHOT -> Triple("sondr_${post.postID}.jpg", "image/jpeg", Environment.DIRECTORY_PICTURES)
                            PostType.WHISPR -> Triple("sondr_${post.postID}.m4a", "audio/mp4", Environment.DIRECTORY_MUSIC)
                            else -> Triple("sondr_${post.postID}", "*/*", Environment.DIRECTORY_DOWNLOADS)
                        }


                        downloadFile(context, url, fileName, mimeType, directory)
                    },
                    onDeletePost = { postId ->
                        deleteLoading = true
                        postRepository.deletePost(postId) { success, message ->
                            if (success) {
                                Log.d("Delete", "Deleted: $postId")
                                onRefreshPosts()
                                // Optional: refresh list or show snackbar
                            } else {
                                Log.e("Delete", "Failed: $message")
                            }
                            deleteLoading = false  // <-- only set false after delete completes
                        }
                    }

                )
            }

        }
        Loading(isLoading = deleteLoading, message = "Deleting post...")

        // Fullscreen overlay
        if (fullscreenImageUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { fullscreenImageUri = null },  // tap anywhere to close
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = fullscreenImageUri),
                    contentDescription = "Full Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }


}



