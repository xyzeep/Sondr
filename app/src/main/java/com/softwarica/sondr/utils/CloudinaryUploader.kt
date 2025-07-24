package com.softwarica.sondr.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
            // ignoring if already initialized
        }
    }

    suspend fun uploadMedia(context: Context, uri: Uri): String = suspendCancellableCoroutine { cont ->
        val contentResolver = context.contentResolver
        val type = contentResolver.getType(uri)
        val resourceType = when {
            type?.startsWith("image") == true -> "image"
            type?.startsWith("video") == true || type?.startsWith("audio") == true -> "video"
            else -> "auto" // fallback
        }

        MediaManager.get().upload(uri)
            .option("resource_type", resourceType)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    if (url != null) {
                        if (cont.isActive) cont.resume(url)
                    } else {
                        if (cont.isActive) cont.resumeWithException(Exception("No URL returned"))
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    if (cont.isActive) cont.resumeWithException(Exception(error.description))
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            }).dispatch()

        cont.invokeOnCancellation { /* Optional cleanup */ }
    }

}
