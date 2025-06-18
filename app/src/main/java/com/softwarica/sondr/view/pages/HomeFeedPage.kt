package com.softwarica.sondr.view.pages

import com.softwarica.sondr.ui.model.Post
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
import com.softwarica.sondr.ui.model.PostType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedPage() {

    var selectedFilter by remember { mutableStateOf("All") }

    data class BottomNavItem(val label: String, val iconResId: Int)

    val dummyPosts = listOf(
        Post(
            postId = "snap-0x4f5ts5",
            time = "3 mins ago",
            caption = "are aliens real? 🤔",
            type = PostType.SNAPSHOT,
            mediaRes = R.drawable.quadeca, // post SNAPSHOT
            likes = 42,
            author = "oggyboggy"
        ),
        Post(
            postId = "snap-0x5g5ts5",
            time = "3 min ago",
            caption = "when life gives you lemons, throw it in the bin",
            type = PostType.SNAPSHOT,
            mediaRes = R.drawable.alu, // post SNAPSHOT
            likes = 42,
            author = "ChandramaKoDaag"
        ),
        Post(
            postId = "snap-0x4f5tf7",
            time = "1 hour ago",
            caption = "chickens are birds too",
            type = PostType.SNAPSHOT,
            mediaRes = R.drawable.quadeca, // post SNAPSHOT
            likes = 42,
            author = "heroHiraLal"
        ),
        Post(
            postId = "snap-0x4f5ghw",
            time = "33 mins ago",
            caption = "why are you here?",
            type = PostType.SNAPSHOT,
            mediaRes = R.drawable.quadeca, // post SNAPSHOT
            likes = 42,
            author = "smartFella"
        ),
        Post(
            postId = "snap-0x4ggts5",
            time = "55 mins ago",
            caption = "i am just boreddd",
            type = PostType.SNAPSHOT,
            mediaRes = R.drawable.quadeca, // post SNAPSHOT
            likes = 42,
            author = "fartSmella"
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
fun Feed(posts: List<Post>, selectedFilter: String, onFilterChange: (String) -> Unit) {
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
