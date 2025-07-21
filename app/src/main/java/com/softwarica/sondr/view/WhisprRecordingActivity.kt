package com.softwarica.sondr.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.Manifest
import android.util.Log

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.softwarica.sondr.R
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.utils.AudioRecorder
import com.softwarica.sondr.utils.takePhoto



class WhisprRecordingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1001
            )
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhisprRecordingBody()
        }
    }
}

@Composable
fun WhisprRecordingBody() {

    val context = LocalContext.current
    val activity = context as? Activity

    val isRecording = remember { mutableStateOf(false) }
    val isFinished = remember { mutableStateOf(false) }
    val showConfirmDialog = remember { mutableStateOf(false) }

    val audioRecorder = remember { AudioRecorder(context) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }


    LaunchedEffect(isFinished.value) {
        if (isFinished.value) {
            showConfirmDialog.value = true
        }
    }


    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(color = Color(0xff121212))
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)

                ) {
                    Text(
                        text = "Record Whispr",
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
                            modifier = Modifier
                                .clickable {
                                    activity?.finish()
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
                Spacer(Modifier.height(140.dp))
            }
            item {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isRecording.value) "Click again to stop" else "Click to start recording",
                        fontFamily = InterFont,
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(28.dp))
                    Box(
                        modifier = Modifier
                            .size(158.dp) // overall size of the circle
                            .clip(CircleShape)
                            .background(
                                if (isRecording.value) Color(0XFF313B4D).copy(alpha = 0.2f) else Color.Gray.copy(
                                    alpha = 0.2f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        GlowingMicButton(
                            isRecording = isRecording,
                             onClick = {
                                if (isRecording.value) {
                                    isFinished.value = true
                                    try {
                                        audioRecorder.stopRecording()
                                        // log
                                        Log.d("WhisprDebug", "Recording saved at: $audioUri")

                                        audioUri = audioRecorder.getOutputUri()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    audioUri = audioRecorder.getOutputUri()
                                }
                                 else {
                                    audioUri = audioRecorder.startRecording()

                                    // log
                                    Log.d("WhisprDebug", "Recording started at: $audioUri")

                                }
                                isRecording.value = !isRecording.value
                            },
                            isEnabled = !isFinished.value
                        )
                    }
                    Spacer(Modifier.height(24.dp))

                    Text(
                        buildAnnotatedString {
                            append("Record up to ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("2 minutes")
                            }
                            append(" of audio at a time.")
                        },
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        color = Color.White
                    )


                    Spacer(Modifier.height(60.dp))

                    // Show confirmation dialog if recording is finished
                    if (showConfirmDialog.value) {
                        Column(
                            modifier = Modifier
                                .background(Color(0XFF313B4D).copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Do you want to proceed with this recording or retake?",
                                fontSize = 22.sp,
                                color = Color.White
                            )
                            Spacer(Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Button(
                                    onClick = {
                                        isFinished.value = false
                                        showConfirmDialog.value = false
                                        isRecording.value = false
                                    },
                                    modifier = Modifier
                                        .height(52.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0XFF98CAE6).copy(alpha = 0.5f)
                                    )
                                ) {
                                    Text(
                                        text = "Retake",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontFamily = InterFont,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(context, PostWhisprActivity::class.java).apply {
                                            putExtra("audioUri", audioUri.toString())
                                        }
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier
                                        .height(52.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0XFF98E69C).copy(alpha = 0.8f)
                                    )
                                ) {
                                    Text(
                                        text = "Proceed",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontFamily = InterFont,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                            }

                        }
                }



                }

            }

        }

    }
}

@Composable
fun GlowingMicButton(
    isRecording: MutableState<Boolean>,
    onClick: () -> Unit,
    isEnabled: Boolean,
) {
    // Animate pulsing size
    val pulseSize by animateDpAsState(
        targetValue = if (isRecording.value) 140.dp else 112.dp,
        animationSpec = if (isRecording.value) {
            infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            tween(durationMillis = 200) // fast shrink back
        },
        label = "PulseSize"
    )

    Box(
        modifier = Modifier
            .size(pulseSize)
            .clip(CircleShape)
            .background(if (isRecording.value) Color(0XFF313B4D).copy(alpha = 1f) else Color.DarkGray)
            .clickable(enabled = isEnabled) { // 👈 use it here
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.microphone), // Replace with your mic vector
            contentDescription = "Mic Icon",
            tint = Color.White,
            modifier = Modifier.size(64.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WhisprRecordingPreview() {
    WhisprRecordingBody()
}