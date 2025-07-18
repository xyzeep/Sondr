package com.softwarica.sondr.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.ui.theme.LoraFont
import com.softwarica.sondr.utils.generateQrBitmap
import com.softwarica.sondr.view.ui.theme.SondrTheme

class SondrCodeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Example: Get the Sondr code from intent extras
        val sondrCode = intent.getStringExtra("sondr_code") ?: "UNKNOWN"

        // Generate QR Bitmap
        val qrBitmap = generateQrBitmap(sondrCode)

        setContent {
            SondrCodeActivityBody(
                sondrCode = sondrCode,
                qrBitmap = qrBitmap,
                onCopyClicked = {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Sondr Code", sondrCode))
                    Toast.makeText(this, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                },
                onUnderstandClicked = {
                    startActivity(Intent(this, LoginActivity::class.java)) // Replace with actual next activity
                    finish()
                }
            )
        }
    }
}


@Composable
fun SondrCodeActivityBody(
    sondrCode: String,
    qrBitmap: ImageBitmap,
    onCopyClicked: () -> Unit,
    onUnderstandClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
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
            Text("Your Sondr Code.", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = LoraFont)

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                   painter = painterResource(R.drawable.baseline_visibility_off_24),
                    contentDescription = "Hidden",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = sondrCode,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFont,
                    color = Color.White,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = onCopyClicked) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_content_copy_24),
                        contentDescription = "Copy",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
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
                color = Color.LightGray,
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
                color = Color.LightGray,
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
                Text("KEEP IN MIND THAT", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White, fontFamily = LoraFont)
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

                Text("2. You’ll need it to log in to your account,", fontSize = 16.sp, color = Color.White, fontFamily = LoraFont)
                Text("    make posts, or prove who you are.", fontSize = 16.sp, color = Color.White, fontFamily = LoraFont)
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

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            Button(
                onClick = {
                    // TODO
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

@Preview(showBackground = true)
@Composable
fun SondrCodeActivityPreview() {
    SondrCodeActivityBody(
        sondrCode = "ABC123XYZ",
        qrBitmap = ImageBitmap(200, 200), // Dummy bitmap
        onCopyClicked = {},
        onUnderstandClicked = {}
    )
}