package com.softwarica.sondr.repository

import com.softwarica.sondr.model.PostModel

interface PostRepository {
    fun createPost(post: PostModel, callback: (Boolean, String) -> Unit)
    fun getPostById(postId: String, callback: (Boolean, String, PostModel?) -> Unit)
    fun getAllPosts(callback: (Boolean, String, List<PostModel>) -> Unit)
    fun deletePost(postId: String, callback: (Boolean, String) -> Unit)
    fun getAllPostsOfUser(userId: String, callback: (Boolean, String, List<PostModel>) -> Unit)
    fun getPostsCountForUser(username: String, callback: (Boolean, String, Int) -> Unit)

}