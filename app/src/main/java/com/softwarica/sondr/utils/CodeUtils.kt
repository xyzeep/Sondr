package com.softwarica.sondr.utils


import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*

fun generateSondrCode(callback: (String) -> Unit){
    val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
    val code = StringBuilder()

    for (i in 1..8) {
        val randomChar = chars[Random.nextInt(chars.length)]
        code.append(randomChar)
    }

    checkIfSondrCodeExists(code.toString()) { exists ->
        if (exists) {
            generateSondrCode(callback)
        } else {
            callback(code.toString())
        }
    }
}

fun checkIfSondrCodeExists(
    sondrCode: String,
    callback: (exists: Boolean) -> Unit
) {
    val database = FirebaseDatabase.getInstance().reference.child("users")

    // query users where sondrCode equals the generated code
    database.orderByChild("sondrCode").equalTo(sondrCode).get()
        .addOnSuccessListener { snapshot ->
            callback(snapshot.exists()) // returns true or false based on existence of the code
        }
        .addOnFailureListener {
            callback(false) // say that it doesn't exist
        }
}


fun getTimeAgo(createdAt: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - createdAt

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} minutes ago"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hours ago"
        diff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
        else -> {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            "$days days ago"
        }
    }
}


fun formatTimestampToDate(timestamp: Long, pattern: String = "MMM dd, yyyy"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}


fun generateQrBitmap(data: String, size: Int = 512): ImageBitmap {
    val hints = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.MARGIN to 1 // Reduce white border
    )

    val bitMatrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }

    return bmp.asImageBitmap()
}