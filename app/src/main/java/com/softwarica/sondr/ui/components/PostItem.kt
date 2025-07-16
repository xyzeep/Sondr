package com.softwarica.sondr.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@Composable
fun PostItem(post: PostModel) {
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
                Image(
                    painter = rememberAsyncImagePainter(model = post.mediaRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
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
}
