package com.softwarica.sondr.view
import androidx.camera.core.AspectRatio
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
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.geometry.Offset
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
import com.softwarica.sondr.utils.takePhoto

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
    val aspectRatio = remember { mutableStateOf("3:4") }

    val cameraAspectRatio = remember { mutableStateOf(AspectRatio.RATIO_4_3) }


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
//                    .fillMaxWidth()
                    .background(Color(0xff121212)),
                contentAlignment = Alignment.Center
            ) {
                // Inner box with fixed aspect ratio for the preview and grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(
                            when (aspectRatio.value) {
                                "3:4" -> 3f / 4f
                                "9:16" -> 9f / 16f
                                else -> 3f / 4f
                            }
                        )
                ) {
                    CameraPreview(
                        isFrontCamera = isFrontCamera.value,
                        aspectRatio = aspectRatio.value
                    ) // this is where the camera preview is shown

                    // Grid overlay
                    GridOverlay()
                }
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
                        .clickable {
                            // Toggle aspect ratio on each click
                            if (aspectRatio.value == "3:4") {
                                aspectRatio.value = "9:16"
                                cameraAspectRatio.value = AspectRatio.RATIO_16_9
                            } else {
                                aspectRatio.value = "3:4"
                                cameraAspectRatio.value = AspectRatio.RATIO_4_3
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = aspectRatio.value,
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
                        .clickable {
                            takePhoto(context) { uri ->
                                if (uri != null) {
                                   // got the photo
                                    val intent = Intent(context, PostSnapshotActivity::class.java).apply {
                                        putExtra("photoUri", uri.toString())
                                    }
                                    context.startActivity(intent)

                                } else {
                                    Toast.makeText(context, "Failed to take photo", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
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

@Composable
fun GridOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val thirdWidth = width / 3
        val thirdHeight = height / 3

        val lineColor = Color.White.copy(alpha = 0.3f)
        val strokeWidth = 1.dp.toPx()

        // Vertical lines
        drawLine(
            color = lineColor,
            start = Offset(thirdWidth, 0f),
            end = Offset(thirdWidth, height),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = lineColor,
            start = Offset(2 * thirdWidth, 0f),
            end = Offset(2 * thirdWidth, height),
            strokeWidth = strokeWidth
        )

        // Horizontal lines
        drawLine(
            color = lineColor,
            start = Offset(0f, thirdHeight),
            end = Offset(width, thirdHeight),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = lineColor,
            start = Offset(0f, 2 * thirdHeight),
            end = Offset(width, 2 * thirdHeight),
            strokeWidth = strokeWidth
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TakeSnapshotPreview() {
    TakeSnapshotBody()
}