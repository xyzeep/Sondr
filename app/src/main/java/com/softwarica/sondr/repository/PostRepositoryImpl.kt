package com.softwarica.sondr.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.softwarica.sondr.model.PostModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.net.toUri
import com.softwarica.sondr.utils.CloudinaryService

class PostRepositoryImpl(
    private val context: Context
) : PostRepository {

    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.reference.child("posts")
    private val cloudinaryService = CloudinaryService.getInstance(context)

    override fun createPost(post: PostModel, callback: (Boolean, String) -> Unit) {
        // We expect mediaRes to be a Uri string if present
        val mediaUriString = post.mediaRes

        // Launch a coroutine for async upload + save
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val uploadedUrl = mediaUriString?.let { uriString ->
                    withContext(Dispatchers.IO) {
                        uploadMedia(uriString.toUri())
                    }
                }

                // Create new post with uploaded media URL (or null if none)
                val newPostId = postsRef.push().key
                if (newPostId == null) {
                    callback(false, "Failed to generate post ID")
                    return@launch
                }

                val postToSave = post.copy(
                    postID = newPostId,
                    mediaRes = uploadedUrl ?: post.mediaRes // replace with Cloudinary URL if uploaded
                )

                postsRef.child(newPostId).setValue(postToSave)
                    .addOnSuccessListener {
                        callback(true, "Post created successfully")
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message ?: "Failed to save post")
                    }
            } catch (e: Exception) {
                callback(false, e.message ?: "Upload or post creation failed")
            }
        }
    }

    private suspend fun uploadMedia(uri: Uri): String? {
        return try {
            cloudinaryService.uploadMedia(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getPostById(postId: String, callback: (Boolean, String, PostModel?) -> Unit) {
        postsRef.child(postId).get()
            .addOnSuccessListener { snapshot ->
                val post = snapshot.getValue(PostModel::class.java)
                if (post != null) {
                    callback(true, "Post found", post)
                } else {
                    callback(false, "Post not found", null)
                }
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Error fetching post", null)
            }
    }

    override fun getAllPosts(callback: (Boolean, String, List<PostModel>) -> Unit) {
        postsRef.get()
            .addOnSuccessListener { snapshot ->
                val posts = mutableListOf<PostModel>()
                for (child in snapshot.children) {
                    child.getValue(PostModel::class.java)?.let { posts.add(it) }
                }
                callback(true, "Posts retrieved", posts)
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to get posts", emptyList())
            }
    }

    override fun deletePost(postId: String, callback: (Boolean, String) -> Unit) {
        postsRef.child(postId).removeValue()
            .addOnSuccessListener {
                callback(true, "Post deleted successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to delete post")
            }
    }

    override fun getAllPostsOfUser(userId: String, callback: (Boolean, String, List<PostModel>) -> Unit) {
        postsRef.orderByChild("authorID").equalTo(userId).get()
            .addOnSuccessListener { snapshot ->
                val posts = mutableListOf<PostModel>()
                for (child in snapshot.children) {
                    child.getValue(PostModel::class.java)?.let { posts.add(it) }
                }
                callback(true, "User posts retrieved", posts)
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to get user posts", emptyList())
            }
    }
}
