package com.softwarica.sondr.view.pages

import com.softwarica.sondr.model.PostModel
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedPage() {

    var selectedFilter by remember { mutableStateOf("All") }

    data class BottomNavItem(val label: String, val iconResId: Int)

    val dummyPosts = listOf(
        PostModel(
            postID = "1",
            text = "Beautiful sunset at the beach",
            authorID = "user123",
            author = "Alice",
            type = PostType.SNAPSHOT,
            caption = "Golden hour vibes 🌅",
            likes = 120,
            nsfw = false,
            private = false,
            mediaRes = "https://picsum.photos/600/400?random=1",
            createdAt = System.currentTimeMillis() - 5 * 60 * 1000 // 5 minutes ago
        ),
        PostModel(
            postID = "2",
            text = "Quiet moments in the forest",
            authorID = "user456",
            author = "Bob",
            type = PostType.WHISPR,
            caption = "Nature's peace 🍃",
            likes = 85,
            nsfw = false,
            private = false,
            mediaRes = "https://picsum.photos/600/100?random=2",
            createdAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000 // 2 hours ago
        ),
        PostModel(
            postID = "3",
            text = "Delicious homemade pizza",
            authorID = "chef007",
            author = "Charlie",
            type = PostType.SNAPSHOT,
            caption = "Dinner is served! 🍕",
            likes = 230,
            nsfw = false,
            private = false,
            mediaRes = "https://picsum.photos/600/400?random=3",
            createdAt = System.currentTimeMillis() - 24 * 60 * 60 * 1000 // 1 day ago
        ),
        PostModel(
            postID = "4",
            text = "Calm lake at dawn",
            authorID = "naturelover",
            author = "Dana",
            type = PostType.WHISPR,
            caption = "Peaceful mornings",
            likes = 95,
            nsfw = false,
            private = false,
            mediaRes = "https://picsum.photos/600/100?random=4",
            createdAt = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000 // 3 days ago
        ),
        PostModel(
            postID = "5",
            text = "City lights at night",
            authorID = "urbanexplorer",
            author = "Eli",
            type = PostType.SNAPSHOT,
            caption = "City never sleeps 🌃",
            likes = 310,
            nsfw = false,
            private = false,
            mediaRes = "https://picsum.photos/600/400?random=5",
            createdAt = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000 // 7 days ago
        ),
        PostModel(
            postID = "6",
            text = "Cozy reading nook",
            authorID = "bookworm",
            author = "Fiona",
            type = PostType.WHISPR,
            caption = "Lost in stories 📚",
            likes = 180,
            nsfw = false,
            private = false,
            mediaRes = "https://picsum.photos/600/100?random=6",
            createdAt = System.currentTimeMillis() - 30 * 60 * 1000 // 30 minutes ago
        )
    )

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xff121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Feed(
            posts = dummyPosts,
            selectedFilter = selectedFilter,
            onFilterChange = { selectedFilter = it }
        )

    }
}

// fun to load feed
@Composable
fun Feed(posts: List<PostModel>, selectedFilter: String, onFilterChange: (String) -> Unit) {
    val filterOptions = listOf("All", "Whisprs", "Snapshots")

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
        items(posts.size) { index ->
            val post = posts[index]
            PostItem(post = post)
        }
    }
}
