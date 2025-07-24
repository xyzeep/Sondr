package com.softwarica.sondr.view

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.ui.theme.LoraFont
import com.softwarica.sondr.utils.generateQrBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SondrCodeActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // get the sondr code from intent extras
        val sondrCode = intent.getStringExtra("sondr_code") ?: "UNKNOWN"

        // generate QR Bitmap
        val qrBitmap = generateQrBitmap(sondrCode)

        setContent {
            val context = this
            SondrCodeActivityBody(
                sondrCode = sondrCode,
                qrBitmap = qrBitmap,
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SondrCodeActivityBody(
    sondrCode: String,
    qrBitmap: ImageBitmap,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var canCopy by remember { mutableStateOf(true) }
    var showConfirmDialog by remember { mutableStateOf(false) }


    Scaffold(
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(Modifier.height(16.dp)) }

            item {
                Icon(
                    painter = painterResource(R.drawable.sondr_logo),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(120.dp)
                )
            }

            item {
                Text(
                    "Your Sondr Code.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = LoraFont
                )

                Text(
                    buildAnnotatedString {
                        append("Keep it ")

                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("safe")
                        }

                        append(". ")

                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append("Please.")
                        }
                    },
                    fontSize = 20.sp,
                    color = Color.LightGray,
                    fontFamily = LoraFont
                )

            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                var isVisible by remember { mutableStateOf(false) }

                val blurEffect = if (!isVisible) {
                    RenderEffect.createBlurEffect(28f, 28f, Shader.TileMode.CLAMP)
                        .asComposeRenderEffect()
                } else {
                    null
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { isVisible = !isVisible }) {
                        Icon(
                            painter = painterResource(
                                if (isVisible) R.drawable.baseline_visibility_24
                                else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = if (isVisible) "Visible" else "Hidden",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = sondrCode,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = LoraFont,
                        color = Color.White,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier
                            .graphicsLayer {
                                renderEffect = blurEffect
                            }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(    onClick = {
                        if (canCopy) {
                            val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Sondr Code", sondrCode))
                            Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()

                            canCopy = false
                            // Start cooldown
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(6000) // 6 seconds
                                canCopy = true
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_content_copy_24),
                            contentDescription = "Copy",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Copy")
                        }
                        append(" your code or")
                    },
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = LoraFont
                )

            }

            item { Spacer(Modifier.height(8.dp)) }

            item {
                Image(
                    bitmap = qrBitmap,
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Text(
                    buildAnnotatedString {
                        append("Take a ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Screenshot")
                        }
                    },
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = LoraFont
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                HorizontalDivider(thickness = 2.dp, color = Color.Gray.copy(alpha = 0.3f))
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "KEEP IN MIND THAT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = LoraFont
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        buildAnnotatedString {
                            append("1. This code is your ")
                            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                append("private key")
                            }
                            append(".")
                        },
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = LoraFont
                    )

                    Text(
                        "2. You’ll need it to log in to your account,",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = LoraFont
                    )
                    Text(
                        "    make posts, or prove who you are.",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = LoraFont
                    )
                    Text(
                        buildAnnotatedString {
                            append("3. ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Don’t share")
                            }
                            append(" it with anyone!")
                        },
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = LoraFont
                    )

                }
            }

            item { Spacer(modifier = Modifier.height(36.dp)) }

            item {
                Button(
                    onClick = {
                        showConfirmDialog = true  // show dialog on click
                    },
                    modifier = Modifier
                        .width(340.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Text(
                        text = "I understand.",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = LoraFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
    if (showConfirmDialog) {
        Box(
            modifier = Modifier
                .background(Color(0xFF121212))
                .border(
                    width = 1.5.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp) // small padding so border isn't cut off
        ) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = {
                    Text(
                        text = "Confirmation",
                        fontFamily = LoraFont,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                },
                text = {
                    Text(
                        "Are you sure you saved your sondr code?",
                        fontFamily = LoraFont,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            activity?.startActivity(Intent(activity, LoginActivity::class.java))
                            activity?.finish()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Yes I did",
                            color = Color(0xFF121212),
                            fontFamily = LoraFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showConfirmDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "No, wait!",
                            color = Color(0xFF121212),
                            fontFamily = LoraFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                containerColor = Color(0xFF121212),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun SondrCodeActivityPreview() {
    SondrCodeActivityBody(
        sondrCode = "FG7G5FN6",
        qrBitmap = ImageBitmap(200, 200) // Placeholder for preview
    )
}