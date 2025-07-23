package com.softwarica.sondr.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.view.ui.theme.SondrTheme
import okhttp3.internal.http2.Settings

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsBody()
        }
    }
}

@Composable
fun SettingsBody() {
    val context = LocalContext.current
    val activity = context as? Activity


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
                        .padding(horizontal = 16.dp, vertical = 8.dp)

                ) {
                    Text(
                        text = "Settings",
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
                            text = "Back",
                            fontSize = 20.sp,
                            fontFamily = InterFont,
                            color = Color(0XFF0088FF),
                            modifier = Modifier.clickable {
                                activity?.finish() // Finish PostSnapshotActivity
//                                    // Start NavigationActivity
//                                    val intent = Intent(context, NavigationActivity::class.java)
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    context.startActivity(intent)

                            }
                        )

                    }


                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* handle click */ }
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                ) {
                   Icon(
                       painter = painterResource(R.drawable.sondr_logo),
                       contentDescription = null,
                       tint = Color.White,
                       modifier = Modifier.padding(end = 8.dp)
                           .size(40.dp)
                   )

                    Text(
                        text = "Sondr Code",
                        fontSize = 22.sp,
                        fontFamily = InterFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* handle click */ }
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_help_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                            .size(40.dp)

                    )

                    Text(
                        text = "Help and Support",
                        fontSize = 22.sp,
                        fontFamily = InterFont,
                        color = Color.White
                    )
                }

            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* handle click */ }
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_text_snippet_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                            .size(40.dp)

                    )

                    Text(
                        text = "Terms and Conditions",
                        fontSize = 22.sp,
                        fontFamily = InterFont,
                        color = Color.White
                    )
                }

            }

            item {
                Spacer(Modifier.height(485.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Reset isLoggedIn in SharedPreferences
                            val sharedPreferences = context.getSharedPreferences("sondr_prefs", android.content.Context.MODE_PRIVATE)
                            sharedPreferences.edit().apply {
                                putBoolean("isLoggedIn", false)
                                apply()
                            }
                            var intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            activity?.finish()
                        }
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_logout_24),
                        contentDescription = null,
                        tint = Color(0xFFEF4339),
                        modifier = Modifier.padding(end = 8.dp)
                            .size(40.dp)

                    )

                    Text(
                        text = "Logout",
                        fontSize = 22.sp,
                        fontFamily = InterFont,
                        color = Color(0xFFEF4339)
                    )
                }

            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* handle click */ }
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_forever_24),
                        contentDescription = null,
                        tint = Color(0xFFEF4339),
                        modifier = Modifier.padding(end = 8.dp)
                            .size(40.dp)

                    )

                    Text(
                        text = "Delete Account",
                        fontSize = 22.sp,
                        fontFamily = InterFont,
                        color = Color(0xFFEF4339)
                    )
                }

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsBody()
}