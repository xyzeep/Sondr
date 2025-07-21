package com.softwarica.sondr.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

fun downloadImage(context: Context, imageUrl: String, fileName: String = "sondr_image.jpg") {
    val request = DownloadManager.Request(Uri.parse(imageUrl))
        .setTitle("Downloading image")
        .setDescription("Sondr image download")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}
