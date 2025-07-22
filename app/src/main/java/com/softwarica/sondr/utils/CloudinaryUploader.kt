package com.softwarica.sondr.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Replace "YOUR_CLOUD_NAME", "YOUR_API_KEY", and "YOUR_API_SECRET" with your Cloudinary credentials.

class CloudinaryService internal constructor(context: Context) {
    companion object {
        @Volatile
        private var instance: CloudinaryService? = null

        fun getInstance(context: Context): CloudinaryService {
            return instance ?: synchronized(this) {
                instance ?: CloudinaryService(context.applicationContext).also { instance = it }
            }
        }
    }

    init {
        try {
            val config = HashMap<String, String>()
            config["cloud_name"] = "ddp9rhsim"
            config["api_key"] = "661111219668513"
            config["api_secret"] = "e0dTxIq2lPipTZ7WP7i4mYdVHqM"
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // Ignore if already initialized
        }
    }

    suspend fun uploadMedia(uri: Uri): String = suspendCancellableCoroutine { cont ->
        val deferred = CompletableDeferred<String>()

        MediaManager.get().upload(uri)
            .option("resource_type", "video")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    if (url != null) {
                        // log
                        Log.d("CloudinaryService", "Upload success, URL: $url")  // <-- Add this log
                        if (cont.isActive) cont.resume(url)
                    } else {
                        // log
                        Log.e("CloudinaryService", "Upload success but no URL returned") // <-- Add this log
                        if (cont.isActive) cont.resumeWithException(Exception("No URL returned"))
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    // log
                    Log.e("CloudinaryService", "Upload error: ${error.description}")  // <-- Add this log
                    if (cont.isActive) cont.resumeWithException(Exception(error.description))
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Optional: handle reschedule if needed
                }
            }).dispatch()

        cont.invokeOnCancellation {
            // Optional: cancel upload if needed
        }
    }
}
