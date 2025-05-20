package com.softwarica.sondr

import android.app.Activity
import android.os.Bundle
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.FontResourcesParserCompat

// Font Lora
val LoraFont = FontFamily(
    Font(R.font.lora)
)

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            LoginBody()

        }
    }
}

@Composable
fun LoginBody() {

    var sondrCode by remember { mutableStateOf("") }
    var registerUsername by remember { mutableStateOf("") }
    var sondrCodVisibility by remember {
        mutableStateOf(false)
    }
    var rememberMe by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

//    val activity = context as Activity
//
//    val coroutineScope = rememberCoroutineScope()

    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = Color(0xff121212)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.sondor),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .padding(top = 20.dp)
            )

            Text(
                text = "Sondr.",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = LoraFont,
                color = Color.White
            )
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

            Column (
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .width(340.dp)
            ){
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .width(340.dp)
                ){
                    //password
                    OutlinedTextField(
                        value = sondrCode,
                        onValueChange = { input ->
                            sondrCode = input
                        },
                        modifier = Modifier
                            .width(216.dp)
                            .height(54.dp)
                            .border(width = 2.dp, color = Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        suffix = {
                            Icon(
                                painter = painterResource(
                                    if (sondrCodVisibility) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
                                ),
                                contentDescription = "eye_icon",
                                modifier = Modifier.clickable {
                                    sondrCodVisibility = !sondrCodVisibility
                                }
                            )
                        },
                        //            minLines = 4,
                        visualTransformation = if (sondrCodVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = {
                            Text(
                                text = "Sondr Code",
                                color = Color.White.copy(alpha = 0.5f),
                                fontFamily = LoraFont,
                                fontSize = 20.sp
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        //            minLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E1E1E),
                            unfocusedContainerColor = Color(0xFF1E1E1E),
                            focusedBorderColor = Color(0xFFFFFFFF)
                        )

                    )
                    Button(
                        onClick = {
                            // TODO: handle login
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
                        .padding(vertical = 16 .dp),
                    thickness = 2.dp,
                    color = Color.White.copy(alpha = 0.3f)
                )
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
                        registerUsername = input
                    },
                    modifier = Modifier
                        .width(340.dp)
                        .height(54.dp)
                        .border(width = 2.dp, color = Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(
                            text = "Username",
                            color = Color.White.copy(alpha = 0.5f),
                            fontFamily = LoraFont,
                            fontSize = 20.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E1E1E),
                        unfocusedContainerColor = Color(0xFF1E1E1E),
                        focusedBorderColor = Color(0xFFFFFFFF)
                    )
                )
                
                Spacer(Modifier.height(16.dp))
                // register button
                Button(
                    onClick = {
                        // TODO: handle register
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
                        text = "Generate Sondr code",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = LoraFont,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(text = "Sondr code is a secret key that is unique for everyone. You will need this code for pretty much anything related to Sondr. Please, keep it safe." ,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = LoraFont
                )






            }
//
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 10.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//
//                ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Checkbox(
//                        checked = rememberMe,
//                        onCheckedChange = {
//                            rememberMe = it
//                        },
//                        colors = CheckboxDefaults.colors(
//                            checkedColor = Color.Green,
//                            checkmarkColor = Color.White
//                        )
//                    )
//                    Text("Remember me")
//                }
//
//                Text("Forget Password?", modifier = Modifier.clickable {
//
//                })
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Gray
//                ),
//                onClick = {
//
//                }) {
//                Text("Login")
//            }

        }
    }
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}