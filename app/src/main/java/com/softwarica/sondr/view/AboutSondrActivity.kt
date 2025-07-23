package com.softwarica.sondr.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.ui.theme.LoraFont

class AboutSondrActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutSondrActivityBody()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AboutSondrActivityBody() {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }


            item{Image(
                painter = painterResource(R.drawable.sondr_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .padding(top = 20.dp)
            )}


            // Sondr text branding
            item { Text(
                text = "Sondr.",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = LoraFont,
                color = Color.White
            )}


            // slogan
            item {
                Text(
                    text = "Whisper the moment.",
                    fontSize = 14.sp,
                    fontFamily = LoraFont,
                    color = Color.White,
                    modifier = Modifier
                        .width(268.dp)
                        .wrapContentWidth(Alignment.End)
                )
                Spacer(Modifier.height(15.dp))
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    text = "What is Sondr?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = InterFont
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append("Sondr is a social space where you can post anonymously, in the moment, without pressure or identity. It\u2019s built to give people a space to express themselves freely, with authenticity and care. \n\n")
                    },
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = InterFont
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                HorizontalDivider(thickness = 2.dp, color = Color.Gray.copy(alpha = 0.3f))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    text = "Built with love by:",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = LoraFont
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    val devs = listOf(
                        Triple("Pawan Acharya", "Scrum Master & Developer", R.drawable.pawan),
                        Triple("Prabin Babu Kattel", "Backend Developer & Database", R.drawable.prabin),
                        Triple("Radu Bhattarai", "Frontend Developer", R.drawable.roman),
                        Triple("Nishan BK", "Testing & QA", R.drawable.nishan)
                    )
                    devs.forEach { (name, role, imageRes) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "$name's photo",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = name,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LoraFont
                                )
                                Text(
                                    text = role,
                                    fontSize = 16.sp,
                                    color = Color.LightGray,
                                    fontFamily = InterFont
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(36.dp)) }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview
fun AboutSondrPreview() {
    AboutSondrActivityBody()
}