package com.softwarica.sondr.repository


import com.softwarica.sondr.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.softwarica.sondr.utils.generateSondrCode
import com.google.firebase.database.DatabaseReference


class UserRepositoryImpl : UserRepository {

    private val database: FirebaseDatabase  = FirebaseDatabase.getInstance()
    val usersRef: DatabaseReference = database.reference.child("users")

    override fun login(
        username: String,
        sondrCode: String,
        callback: (Boolean, String) -> Unit
    ) {
        // query users where username equals given username
        usersRef.orderByChild("username").equalTo(username).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    if (snapshot.exists()) {
                        // loop through matching users (should be just 1)
                        for (userSnap in snapshot.children) {
                            val user = userSnap.getValue(UserModel::class.java)
                            if (user != null) {
                                if (user.sondrCode == sondrCode) {
                                    // ✅ Sign in anonymously to activate FirebaseAuth
                                    com.google.firebase.auth.FirebaseAuth.getInstance().signInAnonymously()
                                        .addOnSuccessListener {
                                            callback(true, "Login successful")
                                        }
                                        .addOnFailureListener {
                                            callback(false, "Login succeeded, but FirebaseAuth sign-in failed")
                                        }
                                    return@addOnCompleteListener
                                } else {
                                    callback(false, "Incorrect Sondr Code")
                                    return@addOnCompleteListener
                                }
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

        // Check if username exists first
        usersRef.orderByChild("username").equalTo(username).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(false, "Username already exists", "")
                } else {
                    // Sign in anonymously to get FirebaseAuth UID
                    com.google.firebase.auth.FirebaseAuth.getInstance().signInAnonymously()
                        .addOnSuccessListener { authResult ->
                            val firebaseUID = authResult.user?.uid ?: run {
                                callback(false, "Failed to get Firebase UID", "")
                                return@addOnSuccessListener
                            }

                            // Generate unique sondr code
                            generateSondrCode { code ->

                                // Create user model with FirebaseAuth UID as userID
                                val userModel = UserModel(
                                    userID = firebaseUID,
                                    username = username,
                                    name = username,
                                    sondrCode = code
                                )

                                // Save user in DB using FirebaseAuth UID as key
                                usersRef.child(firebaseUID).setValue(userModel)
                                    .addOnSuccessListener {
                                        callback(true, "User registered successfully", code)
                                    }
                                    .addOnFailureListener { error ->
                                        callback(false, error.message ?: "Unknown error", "")
                                    }
                            }
                        }
                        .addOnFailureListener {
                            callback(false, "FirebaseAuth anonymous sign-in failed", "")
                        }
                }
            }
            .addOnFailureListener { error ->
                callback(false, error.message ?: "Error checking username", "")
            }
    }


    override fun addUserToDatabase(
        userID: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(
        userID: String,
        callback: (Boolean, String) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun editProfile(
        userID: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): FirebaseUser? {
        TODO("Not yet implemented")
    }

    override fun getUserById(
        userID: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUserInfo(callback: (Boolean, String, UserModel?) -> Unit) {
        val firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            callback(false, "No user is currently logged in", null)
            return
        }

        val userID = firebaseUser.uid
        usersRef.child(userID).get()
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

}

