package com.softwarica.sondr.repository


import com.softwarica.sondr.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    fun login(
        username: String,
        sondrCode: String, callback: (Boolean, String) -> Unit
    )

    fun register(
        username: String,
        callback: (Boolean, String, String) -> Unit
    )

    fun addUserToDatabase(
        userID: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    )


    fun deleteAccount(
        userID: String,
        callback: (Boolean, String) -> Unit
    )

    fun editProfile(
        userID: String, data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun getCurrentUser(): FirebaseUser?

    fun getUserById(
        userID: String,
        callback: (Boolean, String, UserModel?) -> Unit
    )

    fun logout(callback: (Boolean, String) -> Unit)

    fun getCurrentUserInfo(callback: (Boolean, String, UserModel?) -> Unit)


}