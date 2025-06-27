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
                                    callback(true, "Login successful")
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
        // check if the username already exists
        usersRef.orderByChild("username").equalTo(username).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // username already taken
                    callback(false, "Username already exists", "")
                } else {
                    // else generate a unique Sondr code
                    generateSondrCode { code ->
                        // generate a unique user ID
                        val userID = usersRef.push().key ?: run {
                            callback(false, "Failed to generate user ID", "")
                            return@generateSondrCode
                        }

                        // create a new user model
                        val userModel = UserModel(
                            userID = userID,
                            username = username,
                            name = username, // default name is the username
                            sondrCode = code
                        )

                        // save user to database
                        usersRef.child(userID).setValue(userModel)
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
                // if checking username fails
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
}

