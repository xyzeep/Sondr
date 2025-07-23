package com.softwarica.sondr.repository

import android.content.Context
import com.softwarica.sondr.model.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.softwarica.sondr.utils.generateSondrCode
import com.softwarica.sondr.utils.getLoggedInUsername

class UserRepositoryImpl(private val context: Context) : UserRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val usersRef: DatabaseReference = database.reference.child("users")

    val defaultBios = listOf(
        "how do you fight the feeling",
        "doing my ting",
        "watch me fall from these castle walls",
        "say no to overengineering",
        "walking 'round the mall",
        "an angel just a human in disguise",
        "walk away as the door slams",
        "feels like we're worlds away",
        "how can i forget that face",
        "can't find anyone that's like me at this latitude"
    )

    override fun login(
        username: String,
        sondrCode: String,
        callback: (Boolean, String) -> Unit
    ) {
        usersRef.orderByChild("username").equalTo(username).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    if (snapshot.exists()) {
                        for (userSnap in snapshot.children) {
                            val user = userSnap.getValue(UserModel::class.java)
                            if (user != null) {
                                if (user.sondrCode == sondrCode) {
                                    callback(true, "Login successful")
                                } else {
                                    callback(false, "Incorrect Sondr Code")
                                }
                                return@addOnCompleteListener
                            }
                        }
                        callback(false, "User data corrupted")
                    } else {
                        callback(false, "Username not found")
                    }
                } else {
                    callback(false, task.exception?.message ?: "Login failed due to an unexpected error")
                }
            }
    }

    override fun register(
        username: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        println("Register called with username: $username")

        usersRef.orderByChild("username").equalTo(username).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(false, "Username already exists", "")
                } else {
                    generateSondrCode { code ->
                        val userModel = UserModel(
                            userID = username,
                            username = username,
                            bio = defaultBios.random(),
                            sondrCode = code
                        )

                        usersRef.child(username).setValue(userModel)
                            .addOnSuccessListener {
                                callback(true, "User registered successfully", code)
                            }
                            .addOnFailureListener { error ->
                                callback(false, error.message ?: "Unknown error", "")
                            }
                    }
                }
            }
            .addOnFailureListener { error ->
                callback(false, error.message ?: "Error checking username", "")
            }
    }

    override fun getCurrentUserInfo(callback: (Boolean, String, UserModel?) -> Unit) {
        val currentUsername = getLoggedInUsername(context)

        if (currentUsername == null) {
            callback(false, "No user is currently logged in", null)
            return
        }

        usersRef.child(currentUsername).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    callback(true, "User data fetched", userModel)
                } else {
                    callback(false, "User not found in database", null)
                }
            }
            .addOnFailureListener { error ->
                callback(false, error.message ?: "Failed to fetch user", null)
            }
    }

    // These are still unimplemented
    override fun addUserToDatabase(userID: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(userID: String, sondrCode: String, callback: (Boolean, String) -> Unit) {
        usersRef.child(userID).get()
            .addOnSuccessListener { snapshot ->
                val actualCode = snapshot.child("sondrCode").value as? String

                if (actualCode == null) {
                    callback(false, "Something went wrong. Try again.")
                } else if (actualCode != sondrCode) {
                    callback(false, "Incorrect Sondr Code.")
                } else {
                    // Code matches, delete account
                    usersRef.child(userID).removeValue()
                        .addOnSuccessListener {
                            val sharedPreferences = context.getSharedPreferences("sondr_prefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit().clear().apply()

                            callback(true, "Account deleted successfully")
                        }
                        .addOnFailureListener { error ->
                            callback(false, error.message ?: "Failed to delete account")
                        }
                }
            }
            .addOnFailureListener { error ->
                callback(false, error.message ?: "Could not verify Sondr Code")
            }
    }



    override fun editProfile(userID: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser() = null // Not using FirebaseAuth

    override fun getUserById(userID: String, callback: (Boolean, String, UserModel?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        TODO("Not yet implemented")
    }
}
