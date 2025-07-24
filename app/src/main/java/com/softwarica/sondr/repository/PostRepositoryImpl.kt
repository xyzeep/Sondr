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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.softwarica.sondr.model.PostType
import com.softwarica.sondr.utils.CloudinaryService

class PostRepositoryImpl(
    private val context: Context
) : PostRepository {

    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.reference.child("posts")
    private val cloudinaryService = CloudinaryService.getInstance(context)

    override fun createPost(post: PostModel, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Only upload if it's a media post with a local URI
                val uploadedUrl = if (post.type == PostType.SNAPSHOT || post.type == PostType.WHISPR) {
                    post.mediaRes?.let { uriString ->
                        withContext(Dispatchers.IO) {
                            uploadMedia(uriString.toUri())
                        }
                    }
                } else null

                val newPostId = postsRef.push().key
                if (newPostId == null) {
                    callback(false, "Failed to generate post ID")
                    return@launch
                }

                val postToSave = post.copy(
                    postID = newPostId,
                    mediaRes = uploadedUrl ?: post.mediaRes // use Cloudinary URL if uploaded
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
            CloudinaryService.getInstance(context).uploadMedia(context, uri)
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

    override fun getAllPostsOfUser(
        userId: String,
        callback: (Boolean, String, List<PostModel>) -> Unit
    ) {
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

    override fun getPostsCountForUser(
        username: String,
        callback: (Boolean, String, Int) -> Unit
    ) {
        postsRef.orderByChild("author").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount.toInt()
                    callback(true, "Posts count fetched", count)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, 0)
                }
            })
    }

    override fun likePost(postId: String, userId: String, callback: (Boolean, String) -> Unit) {
        val postRef = postsRef.child(postId)
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post =
                    currentData.getValue(PostModel::class.java) ?: return Transaction.success(
                        currentData
                    )

                // updating likes count
                val updatedLikes = post.likes + 1

                // updating likedBy list
                val updatedLikedBy = post.likedBy?.toMutableList() ?: mutableListOf()
                if (!updatedLikedBy.contains(userId)) {
                    updatedLikedBy.add(userId)
                }

                // setting updated values back
                currentData.child("likes").value = updatedLikes
                currentData.child("likedBy").value = updatedLikedBy.distinct()

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    callback(false, error.message)
                } else {
                    callback(true, "Post liked")
                }
            }
        })
    }

    override fun unlikePost(postId: String, userId: String, callback: (Boolean, String) -> Unit) {
        val postRef = postsRef.child(postId)
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post =
                    currentData.getValue(PostModel::class.java) ?: return Transaction.success(
                        currentData
                    )

                // updating likes count safely
                val updatedLikes = if (post.likes > 0) post.likes - 1 else 0

                // updating likedBy list
                val updatedLikedBy = post.likedBy?.toMutableList() ?: mutableListOf()
                updatedLikedBy.remove(userId)

                // Setting updated values back
                currentData.child("likes").value = updatedLikes
                currentData.child("likedBy").value = updatedLikedBy.distinct()

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    callback(false, error.message ?: "Unknown error")
                } else {
                    callback(true, "Post unliked")
                }
            }
        })
    }
}
