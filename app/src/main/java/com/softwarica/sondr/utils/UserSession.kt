package com.softwarica.sondr.utils // or your package name

import android.content.Context
import androidx.core.content.edit

fun saveLoggedInUsername(context: Context, username: String) {
    val sharedPref = context.getSharedPreferences("SondrPrefs", Context.MODE_PRIVATE)
    sharedPref.edit() { putString("loggedInUsername", username) }
}

fun getLoggedInUsername(context: Context): String? {
    val sharedPref = context.getSharedPreferences("SondrPrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("loggedInUsername", null)
}