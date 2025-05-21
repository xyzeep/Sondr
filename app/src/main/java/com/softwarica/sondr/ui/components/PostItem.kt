package com.softwarica.sondr.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.ui.model.Post
import com.softwarica.sondr.ui.model.PostType


@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = post.postId, color = Color.Gray, fontSize = 12.sp)
            Text(text = post.time, color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (post.type) {
            PostType.SNAPSHOT -> {
                Image(
                    painter = painterResource(id = post.mediaRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            PostType.WHISPR -> {
                Image(
                    painter = painterResource(id = post.mediaRes),
                    contentDescription = "WHISPR",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = post.caption, color = Color.White)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${post.likes} likes", color = Color.White, fontSize = 14.sp)
            Text(text = "by ${post.author}", color = Color.LightGray, fontSize = 14.sp)
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
