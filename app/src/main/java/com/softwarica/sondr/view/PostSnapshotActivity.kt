package com.softwarica.sondr.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.softwarica.sondr.components.Loading
import com.softwarica.sondr.repository.PostRepositoryImpl
import com.softwarica.sondr.repository.UserRepositoryImpl
import com.softwarica.sondr.ui.theme.InterFont

class PostSnapshotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val photoUriString = intent.getStringExtra("photoUri")
        val photoUri = photoUriString?.let { Uri.parse(it) }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostSnapshotBody(photoUri)
        }
    }
}

@Composable
fun PostSnapshotBody(photoUri: Uri?) {
    var snapshotCaption by remember { mutableStateOf("") }
    var nsfw by remember { mutableStateOf(true) }
    var isPrivate by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val activity = context as? Activity

    val postRepo = PostRepositoryImpl(context)
    val userRepo = UserRepositoryImpl(context)


    Scaffold { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .background(color = Color(0xff121212))
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)

                ) {
                    Text(
                        text = "Snapshot",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterFont,
                        color = Color.White
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 20.sp,
                            fontFamily = InterFont,
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                activity?.let {
                                    it.finish() // Finish PostSnapshotActivity
                                    // Start NavigationActivity
                                    val intent = Intent(context, NavigationActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                }
                            }
                        )

                    }


            }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }

            item {
                Text(
                    text = "Ready to Share?",
                    fontSize = 24.sp,
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)

                ){
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(278.dp)
                            .padding(vertical = 12.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color.White)
                            .clickable { isFullscreen = true }
                    ) {
                        photoUri?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = "Captured Snapshot",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(278.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    OutlinedTextField(
                        value = snapshotCaption,
                        onValueChange = { input ->
                            if (input.length <= 70) {
                                snapshotCaption = input
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(92.dp)
                            .border(width = 2.dp, color = Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        placeholder = {
                            Text(
                                text = "Say something about this",
                                color = Color.White.copy(alpha = 0.5f),
                                fontFamily = InterFont,
                                fontSize = 20.sp
                            )
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = InterFont
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        suffix = {
                            Text(
                                text = "${snapshotCaption.length}/70",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 16.sp,
                                fontFamily = InterFont
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E1E1E),
                            unfocusedContainerColor = Color(0xFF1E1E1E),
                            focusedBorderColor = Color(0xFFFFFFFF)
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "NSFW post",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = InterFont
                        )

                        Switch(
                            checked = nsfw,
                            onCheckedChange = {
                                nsfw = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xff98CAE6),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xff313B4D),
                                uncheckedBorderColor = Color(0xff313B4D)
                            )
                        )

                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "Private post",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = InterFont
                        )
                        Switch(
                            checked = isPrivate,
                            onCheckedChange = {
                                isPrivate = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xff98CAE6),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xff313B4D),
                                uncheckedBorderColor = Color(0xff313B4D)
                            )
                        )

                    }


                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.3f)
                    )


                    Button(
                        onClick = {
                            activity?.finish()
                        },
                        modifier = Modifier
                            .width(144.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.4f)
                        )
                    ) {
                        Text(
                            text = "Retake",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontFamily = InterFont,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            loading = true
                            userRepo.getCurrentUserInfo { success, msg, user ->
                                if (success && user != null) {
                                    val postModel = com.softwarica.sondr.model.PostModel(
                                        authorID = user.userID,
                                        author = user.username,
                                        type = com.softwarica.sondr.model.PostType.SNAPSHOT,
                                        caption = snapshotCaption,
                                        likes = 0,
                                        nsfw = nsfw,
                                        isPrivate = isPrivate,
                                        mediaRes = photoUri?.toString()
                                    )

                                    postRepo.createPost(postModel) { postSuccess, postMessage ->
                                        if (postSuccess) {
                                            val intent = Intent(context, NavigationActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            context.startActivity(intent)
                                            (context as? Activity)?.finish()
                                            loading = false
                                            Toast.makeText(context, "Snapshot Posted!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, postMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },

                        modifier = Modifier
                            .width(144.dp)
                            .height(52.dp),

                        shape = RoundedCornerShape(8.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF98CAE6).copy(alpha = 0.6f)
                        )
                    ) {
                        Text(
                            text = "Post",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontFamily = InterFont,
                            fontWeight = FontWeight.Bold
                        )
                    }


                }


                }
            }
        if (isFullscreen && photoUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable { isFullscreen = false }, // 👈 Tap to exit fullscreen
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Full Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Optional padding
                    contentScale = ContentScale.Fit
                )
            }
        }

    }
    Loading(isLoading = loading, message = "Posting your snapshot...")
    }


@Preview(showBackground = true)
@Composable
fun PreviewPostSnapshot() {
    PostSnapshotBody(photoUri = null)
}