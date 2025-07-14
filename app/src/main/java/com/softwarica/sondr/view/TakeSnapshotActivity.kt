package com.softwarica.sondr.view

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.utils.CameraPreview

class TakeSnapshotActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, show your camera UI
                setContent { TakeSnapshotBody() }
            } else {
                // Permission denied, close the activity or show a message
                finish()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // Already have permission
                setContent { TakeSnapshotBody() }
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

}

@Composable
fun TakeSnapshotBody(
) {

    val isFrontCamera = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    val aspectRatio by remember { mutableStateOf("3:4") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .background(color = Color(0xff121212))
                .padding(innerPadding)
                .fillMaxSize(),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)

            ) {
                Text(
                    text = "Take Snapshot",
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

            // Camera preview area (gray area)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.DarkGray)
            ) {
                CameraPreview(isFrontCamera = isFrontCamera.value)  // This is where the camera preview will be displayed
            }

            // Bottom bar with 3 buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Aspect Ratio button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color.White, shape = RectangleShape)
                        .clickable {  },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = aspectRatio,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Capture button - big white circle with black border
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(4.dp, Color.White, shape = CircleShape)
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    // Inner smaller black circle
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape),
                    )
                }

                // flip camera button - use icon
                Icon(
                    painter = painterResource(R.drawable.baseline_flip_camera_android_24),
                    contentDescription = "Flip Camera",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            isFrontCamera.value = !isFrontCamera.value
                        }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun TakeSnapshotPreview() {
    TakeSnapshotBody()
}