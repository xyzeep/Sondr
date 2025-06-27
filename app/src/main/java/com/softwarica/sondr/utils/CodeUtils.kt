package com.softwarica.sondr.utils


import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

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
