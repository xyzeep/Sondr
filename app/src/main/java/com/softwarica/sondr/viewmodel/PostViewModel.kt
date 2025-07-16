package com.softwarica.sondr.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwarica.sondr.model.PostModel
import com.softwarica.sondr.repository.PostRepositoryImpl
import com.softwarica.sondr.utils.CloudinaryService
import kotlinx.coroutines.launch

class PostViewModel(context: Context) : ViewModel() {

    val cloudinaryService = CloudinaryService(context)
    private val postRepository = PostRepositoryImpl(context)

    fun createPost(post: PostModel, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            postRepository.createPost(post) { success, message ->
                onResult(success, message)
            }
        }
    }
}
