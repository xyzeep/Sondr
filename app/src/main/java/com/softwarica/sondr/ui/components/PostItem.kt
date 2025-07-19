package com.softwarica.sondr.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.model.PostModel
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.utils.getTimeAgo


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(post: PostModel, onRequestFullscreen: (String) -> Unit) {

    val isNSFW = post.nsfw
    var isBlurred by remember { mutableStateOf(isNSFW) }
    var isFullscreen by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "by ${post.authorID}", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Icon(
                painter = painterResource(R.drawable.baseline_more_horiz_24),
                contentDescription = "more_options",
                modifier = Modifier.clickable {
                    // TODO
                },
                tint = Color.White
            )
        }
        Spacer(Modifier.height(6.dp))
        when (post.type) {
            PostType.SNAPSHOT -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .combinedClickable(
                            onClick = { if (post.nsfw) isBlurred = !isBlurred },
                            onLongClick = { onRequestFullscreen(post.mediaRes.toString()) }
                        )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = post.mediaRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(if (isBlurred) 16.dp else 0.dp)
                    )

                    if (isBlurred) {
                        Text(
                            text = "NSFW",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)

                        )
                    }
                }

            }
            PostType.WHISPR -> {
                Image(
                    painter = rememberAsyncImagePainter(model = post.mediaRes),
                    contentDescription = "WHISPR",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = post.caption, color = Color.White, fontSize = 16.sp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(R.drawable.heart),
                    contentDescription = "like",
                    modifier = Modifier
                        .width(28.dp)
                        .clickable {
                        // TODO
                    },
                    tint = Color.White
                )
                Text(text = "${post.likes} likes", color = Color.White, fontSize = 14.sp)
            }
            Text(text = getTimeAgo(post.createdAt), color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontWeight = FontWeight.Bold)

        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.3f)
        )
    }

    if (isFullscreen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .clickable { isFullscreen = false },  // tap to close fullscreen
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = post.mediaRes),
                contentDescription = "Full Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }

}
