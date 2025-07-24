package com.softwarica.sondr.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.softwarica.sondr.R
import com.softwarica.sondr.repository.UserRepositoryImpl
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.utils.getLoggedInUsername
import androidx.core.content.edit
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.FragmentActivity
import androidx.core.net.toUri


class SettingsActivity : FragmentActivity() {
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

    val userRepo = UserRepositoryImpl(context)
    val userID = getLoggedInUsername(context) ?: return

    var showDeleteAccDialog by remember { mutableStateOf(false) }
    var sondrCodeInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val executor = ContextCompat.getMainExecutor(context)


    // sondrCode is a remembered mutableState so it is accessible everywhere inside this composable
    var sondrCode by remember { mutableStateOf("") }

    // loading the current user info once, update sondrCode when data arrives
    LaunchedEffect(Unit) {
        userRepo.getCurrentUserInfo { success, _, userModel ->
            if (success && userModel != null) {
                sondrCode = userModel.sondrCode
            } else {
                sondrCode = ""
            }
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
                        .clickable {
                            val biometricPrompt = BiometricPrompt(
                                activity as FragmentActivity,
                                executor,
                                object : BiometricPrompt.AuthenticationCallback() {
                                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                        super.onAuthenticationSucceeded(result)
                                        val intent = Intent(context, SondrCodeActivity::class.java)
                                        intent.putExtra("sondr_code", sondrCode)
                                        context.startActivity(intent)
                                    }

                                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                                        super.onAuthenticationError(errorCode, errString)
                                        Toast.makeText(context, "$errString", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onAuthenticationFailed() {
                                        super.onAuthenticationFailed()
                                        Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )

                            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                .setTitle("Verify Identity")
                                .setSubtitle("Use fingerprint or device credentials to view your Sondr Code")
                                .setAllowedAuthenticators(
                                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                            BiometricManager.Authenticators.DEVICE_CREDENTIAL
                                )
                                .build()

                            biometricPrompt.authenticate(promptInfo)
                        }
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
                        text = "My Sondr Code",
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
                        .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:".toUri() // only email apps should handle this
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@sondrapp.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Help & Support Request")
                            putExtra(Intent.EXTRA_TEXT, "Hi Sondr Team,\n\nI need help with...")
                        }

                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                        }
                    }

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
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = "https://docs.google.com/document/d/1E5Bxt4aMD0O7mv4Lh1hGHY56zW06ffyBxGX_20e5wco/preview".toUri()
                            }
                            context.startActivity(intent)
                        }
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
                        .clickable {showDeleteAccDialog = true}
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

        if (showDeleteAccDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E1E1E))
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sorry to see you go",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = InterFont
                        )

                        Text(
                            text = "Are you sure you want to delete your account?",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = InterFont,
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = sondrCodeInput,
                            onValueChange = { sondrCodeInput = it },
                            label = { Text("Enter your Sondr Code", color = Color.White) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Oops, no",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontFamily = InterFont,
                                modifier = Modifier.clickable {
                                    showDeleteAccDialog = false
                                    sondrCodeInput = ""
                                    errorMessage = ""
                                }
                            )

                            Text(
                                text = "I'm sure!",
                                color = Color(0xFFFF4C4C),
                                fontSize = 16.sp,
                                fontFamily = InterFont,
                                modifier = Modifier.clickable {
                                    userRepo.deleteAccount(userID, sondrCodeInput.trim()) { deleted, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                                        if (deleted) {
                                            val sharedPreferences = context.getSharedPreferences("sondr_prefs", android.content.Context.MODE_PRIVATE)
                                            sharedPreferences.edit() { clear() }
                                            context.startActivity(Intent(context, LoginActivity::class.java))
                                            activity?.finish()
                                        }
                                    }
                                }

                            )
                        }
                    }
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