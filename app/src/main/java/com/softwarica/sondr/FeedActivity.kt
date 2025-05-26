package com.softwarica.sondr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import com.softwarica.sondr.ui.model.Post
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.softwarica.sondr.ui.components.PostItem
import com.softwarica.sondr.ui.model.PostType
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.ui.theme.LoraFont


// main class
class FeedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeedBody()
        }
    }
}

@Composable
fun FeedBody() {
    val filterOptions = listOf("All", "Whisprs", "Snapshots")
    var selectedFilter by remember { mutableStateOf("All") }

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
//        Post(
//            postId = "snap-0x4f5ts5",
//            time = "12 min ago",
//            caption = "when the silence speaks 🫀🫀",
//            type = PostType.WHISPR,
//            mediaRes = R.drawable.audiowaveform, // placeholder for whispr UI
//            likes = 77,
//            author = "TheAluEater"
//        ),
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

//    val context = LocalContext.current
//    val activity = context as Activity

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xff121212)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.sondr_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // space between logo and text
                    Text(
                        text = "Sondr.",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = LoraFont,
                        color = Color.White
                    )
                }

                MoreOptionsMenu()
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color.White.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { chip ->
                    FilterChip(
                        selected = chip == selectedFilter,
                        onClick = { selectedFilter = chip },
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

            Feed(dummyPosts)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_home_24),
                    contentDescription = "home_icon",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                        // TODO
                    },
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = "search_icon",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                        // TODO
                    },
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_add_circle_24),
                    contentDescription = "add_icon",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                        // TODO
                    },
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_notifications_none_24),
                    contentDescription = "notification_icon",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                        // TODO
                    },
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_person_outline_24),
                    contentDescription = "profile_icon",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                        // TODO
                    },
                    tint = Color.White
                )


                }

        }
    }
}

@Composable
fun MoreOptionsMenu() {
    var expanded by remember { mutableStateOf(true) }
    val bgColor = Color(0xFF242830)
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
            .background(Color.Transparent)
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_menu_24),
            contentDescription = "menu_icon",
            modifier = Modifier.clickable {
                expanded = true
            },
            tint = Color.White
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(bgColor)
                .padding(vertical = 0.dp)
                .clip(RoundedCornerShape(0.dp)),

            // if something goes wrong in future, look up "properties = PopupProperties"

        ) {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
            )

                {
                    DropdownMenuItem(
                        text = {
                            Text("Settings", fontFamily = InterFont, color = Color.White)
                        },

                        onClick = { expanded = false },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_settings_24),
                                contentDescription = "Settings Icon",
                                tint = Color.White
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("About Sondr.", fontFamily = InterFont, color = Color.White)
                        },
                        onClick = { expanded = false },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_info_24),
                                contentDescription = "About Icon",
                                tint = Color.White
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Help & Support", fontFamily = InterFont, color = Color.White)
                        },
                        onClick = { expanded = false },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_help_24),
                                contentDescription = "Help Icon",
                                tint = Color.White
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Logout", fontFamily = InterFont, color = Color.Red)
                        },
                        onClick = { expanded = false },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_logout_24),
                                contentDescription = "Logout Icon",
                                tint = Color.Red
                            )
                        }
                    )
                }
        }
    }
}

// fun to load feed
@Composable
fun Feed(posts: List<Post>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(674.dp)
    ) {
        items(posts.size) { index ->
            val post = posts[index]
            PostItem(post = post)
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 1.dp,
        color = Color.White.copy(alpha = 0.3f)
    )
}

@Preview
@Composable
fun PreviewFeed() {
    FeedBody()
}