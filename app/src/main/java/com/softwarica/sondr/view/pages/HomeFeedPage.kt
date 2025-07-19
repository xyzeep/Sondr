package com.softwarica.sondr.view.pages

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.softwarica.sondr.R
import com.softwarica.sondr.ui.components.PostItem
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.repository.PostRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedPage() {
    var selectedFilter by remember { mutableStateOf("All") }
    var posts by remember { mutableStateOf<List<PostModel>>(emptyList()) }

    val context = LocalContext.current
    val postRepository: PostRepository = remember { PostRepositoryImpl(context) }

    LaunchedEffect(Unit) {
        postRepository.getAllPosts { success, message, result ->
            if (success) {
                posts = result.reversed() // newest first
            } else {
                Log.e("Firebase", "Error fetching posts: $message")
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
            onFilterChange = { selectedFilter = it }
        )
    }
}

// fun to load feed
@Composable
fun Feed(posts: List<PostModel>, selectedFilter: String, onFilterChange: (String) -> Unit) {
    val filterOptions = listOf("All", "Whisprs", "Snapshots")

    val filteredPosts = when (selectedFilter) {
        "Whisprs" -> posts.filter { it.type == PostType.WHISPR }
        "Snapshots" -> posts.filter { it.type == PostType.SNAPSHOT }
        else -> posts
    }

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
//        items(filteredPosts) { post ->
//            PostItem(post = post)
//        }

        items(filteredPosts, key = { it.postID }) { post ->
            PostItem(post = post)
        }
    }
}
