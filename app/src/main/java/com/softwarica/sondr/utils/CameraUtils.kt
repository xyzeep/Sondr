package com.softwarica.sondr.utils

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.camera.core.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

private var imageCapture: ImageCapture? = null

fun takePhoto(context: Context, onPhotoCaptured: (Uri?) -> Unit) {
    val imageCapture = imageCapture ?: return

    val photoFile = File(
        context.externalCacheDir,
        "snapshot_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                onPhotoCaptured(null)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoCaptured(Uri.fromFile(photoFile))
            }
        }
    )
}

@Composable
fun CameraPreview(
    isFrontCamera: Boolean, // passed from parent
    aspectRatio: String // pass the string like "3:4"
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val ratio = when (aspectRatio) {
        "3:4" -> 3f / 4f
        "9:16" -> 9f / 16f
        else -> 3f / 4f
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio)
    ) {
        androidx.compose.runtime.key(isFrontCamera) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .setTargetResolution(
                                when (aspectRatio) {
                                    "3:4" -> android.util.Size(720, 960)
                                    "9:16" -> android.util.Size(720, 1280)
                                    else -> android.util.Size(720, 960)
                                }
                            )
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        imageCapture = ImageCapture.Builder()
                            .setTargetResolution(
                                when (aspectRatio) {
                                    "3:4" -> android.util.Size(720, 960)
                                    "9:16" -> android.util.Size(720, 1280)
                                    else -> android.util.Size(720, 960)
                                }
                            )
                            .build()

                        val cameraSelector = if (isFrontCamera) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture!!
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
