package com.softwarica.sondr.ui.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.softwarica.sondr.R
import com.softwarica.sondr.model.PostModel
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.utils.getTimeAgo


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(
    post: PostModel,
    currentUserId: String,
    onRequestFullscreen: (String) -> Unit,
    onLikeToggle: (PostModel, Boolean) -> Unit
){

    val isNSFW = post.nsfw
    var isBlurred by remember { mutableStateOf(isNSFW) }
    var isLiked by remember { mutableStateOf(post.likedBy.contains(currentUserId)) }
    var likeCount by remember { mutableStateOf(post.likes) }
    var isFullscreen by remember { mutableStateOf(false) }
    var optionsExpanded by remember { mutableStateOf(false) }
    // new
    var showHeart by remember { mutableStateOf(false) }

    // new
    LaunchedEffect(showHeart) {
        if (showHeart) {
            kotlinx.coroutines.delay(600)
            showHeart = false
        }
    }

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
            Text(text = "@${post.authorID}", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = { optionsExpanded = true }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_more_horiz_24),
                        contentDescription = "More options",
                        tint = Color.White,
                        modifier = Modifier.width(32.dp)
                    )
                }

                if (optionsExpanded) {
                    Dialog(onDismissRequest = { optionsExpanded = false }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .background(Color(0xFF242830), shape = RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                DropdownMenuItem(
                                    text = { Text("Download", color = Color.White) },
                                    onClick = {
                                        optionsExpanded = false
                                        // TODO: handle download
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Report",  color = Color.White) },
                                    onClick = {
                                        optionsExpanded = false
                                        // TODO: handle report
                                    }
                                )
                                if (post.authorID == currentUserId) {
                                    DropdownMenuItem(
                                        text = { Text("Delete",  color = Color.Red) },
                                        onClick = {
                                            optionsExpanded = false
                                            // TODO: handle delete
                                        }
                                    )
                                }
                            }
                        }
                    }
                }




        }

        when (post.type) {
            PostType.SNAPSHOT -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (!isLiked) {
                                        isLiked = true
                                        likeCount += 1
                                        onLikeToggle(post, true)
                                    }
                                    showHeart = true
                                }
                                ,
                                onLongPress = {
                                    onRequestFullscreen(post.mediaRes.toString())
                                },
                                onTap = {
                                    if (post.nsfw) isBlurred = !isBlurred
                                }
                            )
                        }

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
                    // new
                    DoubleTapHeartOverlay(showHeart)
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

        if(post.caption != "") {
            Text(text = post.caption, color = Color.White, fontSize = 18.sp)

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier

                    .clickable {
                        isLiked = !isLiked
                        likeCount += if (isLiked) 1 else -1
                        onLikeToggle(post, isLiked) // pass to parent to handle Firestore update
                    }
            ){
                Icon(
                    painter = painterResource(if (isLiked) R.drawable.heart_solid else R.drawable.heart_empty),

                    contentDescription = "like",
                    modifier = Modifier
                        .width(32.dp),
                    tint = (if (isLiked) Color.Red else Color.White)
                )


                Text(text = "$likeCount likes", color = Color.White, fontSize = 18.sp)

            }
            Text(text = getTimeAgo(post.createdAt), color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontWeight = FontWeight.Normal)

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

@Composable
private fun DoubleTapHeartOverlay(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(R.drawable.heart_solid),
                contentDescription = "Floating Heart",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(150.dp),
                tint = (Color.Red.copy(alpha = 0.5f))
            )
        }
    }
}