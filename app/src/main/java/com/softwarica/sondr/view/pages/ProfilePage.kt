package com.softwarica.sondr.view.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.softwarica.sondr.R
import com.softwarica.sondr.model.PostModel
import com.softwarica.sondr.ui.components.PostItem
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.model.UserModel
import com.softwarica.sondr.repository.PostRepository
import com.softwarica.sondr.repository.PostRepositoryImpl
import com.softwarica.sondr.repository.UserRepositoryImpl
import com.softwarica.sondr.utils.downloadImage
import com.softwarica.sondr.utils.formatTimestampToDate

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("sondr_prefs", Context.MODE_PRIVATE)
    val savedUsername = sharedPreferences.getString("currentUsername", "") ?: ""
    var fullscreenImageUri by remember { mutableStateOf<String?>(null) }

    var selectedFilter by remember { mutableStateOf("All") }


    var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }
    val postRepository: PostRepository = remember { PostRepositoryImpl(context) }
    val userRepository = UserRepositoryImpl(context)

    var userModel by remember { mutableStateOf<UserModel?>(null) }
    var postsCount by remember { mutableIntStateOf(0) }

    var currentUserId by remember { mutableStateOf<String?>(null) }



    LaunchedEffect(Unit) {
        userRepository.getCurrentUserInfo { success, message, user ->
            if (success && user != null) {
                userModel = user
                currentUserId = user.userID
            } else {
                Log.e("ProfileScreen", "Failed to fetch user info: $message")
            }
        }

        postRepository.getAllPosts { success, message, allPosts ->
            if (success) {
                val userPosts = allPosts.filter { it.author == savedUsername || it.authorID == savedUsername }
                posts = userPosts.reversed()
                postsCount = userPosts.size
            } else {
                Log.e("ProfileScreen", "Error fetching posts: $message")
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(Color(0xff005CC7), Color(0xFF121212))
//                )
//            )
                .background(colorResource(id = R.color.background))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { Spacer(Modifier.height(50.dp)) }

            item {
                Image(
                    painter = painterResource(R.drawable.blue_user),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )
            }

            item {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "@$savedUsername",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = userModel?.bio ?: "",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStat(value = postsCount.toString(), label = "Posts")
                        ProfileStat(
                            value = formatTimestampToDate(userModel?.createdAt ?: 0L),
                            label = "Joined"
                        )
                        ProfileStat(
                            value = userModel?.followers?.toString() ?: "0",
                            label = "Followers"
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                        .padding(vertical = 8.dp)
                ) {
                    ProfileFeed(
                        posts = posts,
                        selectedFilter = selectedFilter,
                        onFilterChange = { selectedFilter = it },
                        onRequestFullscreen = { uri -> fullscreenImageUri = uri },
                        currentUserId = currentUserId,
                        onLikeToggle = { post, isLiked ->
                            if (currentUserId == null) return@ProfileFeed

                            if (isLiked) {
                                postRepository.likePost(post.postID,
                                    currentUserId!!
                                ) { success, message ->
                                    if (!success) Log.e("ProfileScreen", message)
                                }
                            } else {
                                postRepository.unlikePost(post.postID,
                                    currentUserId!!
                                ) { success, message ->
                                    if (!success) Log.e("ProfileScreen", message)
                                }
                            }
                        },
                        postRepository = postRepository
                    )





                }
            }

        }
    }
    fullscreenImageUri?.let { uri ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .clickable { fullscreenImageUri = null },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Fullscreen Image",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }

}

@Composable
fun ProfileFeed(
    posts: List<PostModel>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onRequestFullscreen: (String) -> Unit,
    currentUserId: String?,
    onLikeToggle: (post: PostModel, isNowLiked: Boolean) -> Unit,
    postRepository: PostRepository
)


 {
     val context = LocalContext.current
    val filterOptions = listOf("All", "Whisprs", "Snapshots")

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { chip ->
                FilterChip(
                    selected = chip == selectedFilter,
                    onClick = { onFilterChange(chip) },
                    label = {
                        Text(text = chip, fontWeight = FontWeight.Bold)
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

        Spacer(Modifier.height(12.dp))

        val filteredPosts = when (selectedFilter) {
            "Whisprs" -> posts.filter { it.type == PostType.WHISPR }
            "Snapshots" -> posts.filter { it.type == PostType.SNAPSHOT }
            else -> posts
        }

        Column {
            filteredPosts.forEach { post ->
                PostItem(
                    post = post,
                    onRequestFullscreen = { uri -> onRequestFullscreen(uri) },
                    currentUserId = currentUserId.toString(),
                    onLikeToggle = onLikeToggle,
                    onDownload = { post ->
                        downloadImage(context, post.mediaRes.toString(), "sondr_${post.postID}.jpg")
                    },
                    onDeletePost = { postId ->
                        postRepository.deletePost(postId) { success, message ->
                            if (success) {
                                Log.d("Delete", "Deleted: $postId")
                                // Optional: refresh list or show snackbar
                            } else {
                                Log.e("Delete", "Failed: $message")
                            }
                        }
                    }
                )




            }
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, color = Color.LightGray, fontSize = 14.sp)
    }
}
