package com.softwarica.sondr.view.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.model.PostModel
import com.softwarica.sondr.ui.components.PostItem
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.repository.PostRepository
import com.softwarica.sondr.repository.PostRepositoryImpl

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("sondr_prefs", Context.MODE_PRIVATE)
    val savedUsername = sharedPreferences.getString("currentUsername", "") ?: ""

    var selectedFilter by remember { mutableStateOf("All") }

    var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }
    val postRepository: PostRepository = remember { PostRepositoryImpl(context) }

    LaunchedEffect(Unit) {
        postRepository.getAllPosts { success, message, result ->
            if (success) {
                // Filter posts by current user (match author or authorID)
                posts = result.filter { it.author == savedUsername || it.authorID == savedUsername }.reversed()
            } else {
                Log.e("Firebase", "Error fetching posts: $message")
            }
        }
    }



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
                text = "you're halfway there",
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
                    ProfileStat("321", "Posts")
                    ProfileStat("06-25", "Joined")
                    ProfileStat("1,023", "Followers")
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
                    onFilterChange = { selectedFilter = it }
                )

            }
        }
    }
}

@Composable
fun ProfileFeed(posts: List<PostModel>, selectedFilter: String, onFilterChange: (String) -> Unit) {
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
                PostItem(post = post)
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
