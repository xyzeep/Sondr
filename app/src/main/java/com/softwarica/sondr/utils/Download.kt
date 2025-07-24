package com.softwarica.sondr.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri

fun downloadFile(
    context: Context,
    fileUrl: String,
    fileName: String,
    mimeType: String,
    subdirectory: String = Environment.DIRECTORY_DOWNLOADS
) {
    val request = DownloadManager.Request(fileUrl.toUri())
        .setTitle("Downloading file")
        .setDescription("Sondr download")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(subdirectory, fileName)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setMimeType(mimeType)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}
