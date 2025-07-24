package com.softwarica.sondr.view

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.softwarica.sondr.components.Loading
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.softwarica.sondr.ui.theme.LoraFont
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.repository.UserRepositoryImpl
import com.softwarica.sondr.utils.saveLoggedInUsername
import androidx.core.net.toUri


// main class
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("sondr_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // User is already logged in, go to NavigationActivity
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody() {

    // user input for sonder code
    var sondrCode by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity
    val sharedPreferences = context.getSharedPreferences("sondr_prefs", Context.MODE_PRIVATE)
    var loading by remember { mutableStateOf(false) }
    var loadingMessage by remember { mutableStateOf("Loading...") }

    // user input for username while register
    var registerUsername by remember { mutableStateOf("") }

    // user input for username while login
    var loginUsername by remember { mutableStateOf("") }

    // boolean for toggling  sondr code visibility
    var sondrCodVisibility by remember {
        mutableStateOf(false)
    }

    // for terms and conditions
    var termsAndCondition by remember {
        mutableStateOf(false)
    }

    val userRepository = remember { UserRepositoryImpl(context) }



    Scaffold(
    ) { innerPadding ->
            LazyColumn (
                modifier = Modifier
                    .background(color = Color(0xff121212))
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sondr Logo
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
                
                // main elements column
                item{
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .width(340.dp)
                    ) {
                        // Login
                        Text(
                            text = "Login",
                            fontSize = 32.sp,
                            fontFamily = LoraFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Text(
                            text = "If you have your sondr code",
                            fontSize = 16.sp,
                            fontFamily = LoraFont,
                            color = Color.White,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )
                        OutlinedTextField(
                            value = loginUsername,
                            onValueChange = { input ->
                                if (input.length <= 30) {
                                    loginUsername = input
                                }
                            },
                            modifier = Modifier
                                .width(340.dp)
                                .height(54.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Username",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontFamily = LoraFont,
                                    fontSize = 20.sp
                                )
                            },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = LoraFont
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E),
                                focusedBorderColor = Color(0xFFFFFFFF)
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        // row for login entry and button
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .width(340.dp)
                        ) {
                            // sondr code
                            OutlinedTextField(
                                value = sondrCode,
                                onValueChange = { input ->
                                    if (input.length <= 8) {
                                        sondrCode = input
                                    }

                                },

                                modifier = Modifier
                                    .width(226.dp)
                                    .height(54.dp)
                                    .padding(top = 0.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Color.White.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                suffix = {
                                    Icon(
                                        painter = painterResource(
                                            if (sondrCodVisibility) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
                                        ),
                                        contentDescription = "eye_icon",
                                        modifier = Modifier.clickable {
                                            sondrCodVisibility = !sondrCodVisibility
                                        },
                                        tint = Color.White
                                    )
                                },
                                visualTransformation = if (sondrCodVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                placeholder = {
                                    Text(
                                        text = "Sondr Code",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontFamily = LoraFont,
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    )
                                },
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = LoraFont
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF1E1E1E),
                                    unfocusedContainerColor = Color(0xFF1E1E1E),
                                    focusedBorderColor = Color(0xFFFFFFFF)
                                )

                            )
                            Button(
                                onClick = {
                                    if (loginUsername.isBlank()) {
                                        Toast.makeText(context, "Username cannot be empty.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (sondrCode.isBlank()) {
                                        Toast.makeText(context, "Sondr Code cannot be empty", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    loadingMessage = "Logging in..."
                                    loading = true  // show loading spinner

                                    userRepository.login(loginUsername, sondrCode) { success, message ->
                                        loading = false  // hide loading spinner
                                        if (success) {
                                            sharedPreferences.edit().apply {
                                                putString("currentUsername", loginUsername)   // store the username
                                                putBoolean("isLoggedIn", true) // store login state
                                                apply()
                                            }

                                            saveLoggedInUsername(context, loginUsername)

                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                            val intent = Intent(context, NavigationActivity::class.java)
                                            // optionally pass user data with intent
                                            intent.putExtra("username", loginUsername)
                                            context.startActivity(intent)
                                            activity?.finish()
                                        } else {
                                            Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                },

                                modifier = Modifier
                                    .width(104.dp)
                                    .height(54.dp),

                                shape = RoundedCornerShape(8.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFFFFF)
                                )
                            ) {
                                Text(
                                    text = "Login",
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontFamily = LoraFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }


                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            thickness = 2.dp,
                            color = Color.White.copy(alpha = 0.3f)
                        )

                        // Register
                        Text(
                            text = "Register",
                            fontSize = 32.sp,
                            fontFamily = LoraFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Text(
                            text = "Create an anonymous account.",
                            fontSize = 16.sp,
                            fontFamily = LoraFont,
                            color = Color.White,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )
                        // register username
                        OutlinedTextField(
                            value = registerUsername,
                            onValueChange = { input ->
                                if (input.length <= 12) {
                                    registerUsername = input
                                }
                            },
                            modifier = Modifier
                                .width(340.dp)
                                .height(54.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.White.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Username",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontFamily = LoraFont,
                                    fontSize = 20.sp
                                )
                            },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = LoraFont
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            suffix = {
                                Text(
                                    text = "${registerUsername.length}/30",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 16.sp,
                                    fontFamily = LoraFont
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E),
                                focusedBorderColor = Color(0xFFFFFFFF)
                            )
                        )

                        Spacer(Modifier.height(12.dp))
                        // register button
                        Button(
                            onClick = {
                                if (registerUsername.isBlank()) {
                                    Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                loadingMessage = "Registering..."
                                loading = true // show loading


                                userRepository.register(registerUsername) { success, message, code ->
                                    if (success) {
                                        Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_LONG).show()
                                        registerUsername = ""
                                        val intent = Intent(context, SondrCodeActivity::class.java)
                                        intent.putExtra("sondr_code", code) // Pass the code here
                                        context.startActivity(intent)
                                        activity?.finish() // optional: closes LoginActivity
                                    } else {
                                        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                                    }
                                    loading = false
                                }
                            },
                            enabled = termsAndCondition,
                            modifier = Modifier
                                .width(340.dp)
                                .height(54.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                disabledContainerColor = Color.White.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = "Generate Sondr code",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = LoraFont,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Sondr code is a secret key that is unique for everyone. You will need this code for pretty much anything related to Sondr. Please, keep it safe.",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = LoraFont
                        )

                        Spacer(Modifier.height(32.dp))

                        // terms and condition
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(340.dp)
                                .fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = termsAndCondition,
                                onCheckedChange = { termsAndCondition = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.White,
                                    checkmarkColor = Color(0xff121212),
                                    uncheckedColor = Color.White
                                )
                            )

                            val annotatedText = buildAnnotatedString {
                                append("I agree to the ")

                                pushStringAnnotation(tag = "TOS", annotation = "https://docs.google.com/document/d/1E5Bxt4aMD0O7mv4Lh1hGHY56zW06ffyBxGX_20e5wco/preview") // replace with your link
                                withStyle(style = SpanStyle(
                                    color = Color.White,
                                    textDecoration = TextDecoration.Underline,
                                    fontFamily = LoraFont
                                )) {
                                    append("Terms and Conditions")
                                }
                                pop()
                            }

                            ClickableText(
                                text = annotatedText,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontFamily = LoraFont
                                ),
                                onClick = { offset ->
                                    annotatedText.getStringAnnotations(tag = "TOS", start = offset, end = offset)
                                        .firstOrNull()?.let { annotation ->
                                            // Handle click here. For example:
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                annotation.item.toUri())
                                            context.startActivity(intent)
                                        }
                                }
                            )
                        }

                        Spacer(Modifier.height(32.dp))


                    }
                }
   }

        Loading(isLoading = loading, message = loadingMessage)

    }
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}