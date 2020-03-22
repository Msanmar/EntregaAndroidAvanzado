package io.keepcoding.eh_ho.data.service

import android.net.Uri
import io.keepcoding.eh_ho.BuildConfig

object ApiRoutes {

    fun signIn(username: String) =
        uriBuilder()
            .appendPath("users")
            .appendPath("${username}.json")
            .build()
            .toString()

    fun signUp() =
        uriBuilder()
            .appendPath("users")
            .build()
            .toString()

    fun getTopics() =
        uriBuilder()
            .appendPath("latest.json")
            .build()
            .toString()

    fun getFilteredTopics(term: String, include_blurbs: Boolean) =
        uriBuilder()
           // .appendPath("search/query.json?term=${term}&include_blurbs=true")
            .appendPath("search")
            .appendPath("query.json")
            .appendQueryParameter("term",term)
            .appendQueryParameter("include_blurbs", "true")
            .build()
            .toString()




   // https://mdiscourse.keepcoding.io/search/query?term=creado&include_blurbs=true

    //https://discourse.example.com/posts.json

    fun getLatestPosts() =
        uriBuilder()
            .appendPath("posts.json")
            .build()
            .toString()

    //https://docs.discourse.org/#tag/Topics/paths/~1t~1{id}.json/get
    //https://discourse.example.com/t/{id}/posts.json
   fun getPosts(topicId: Int) =
        uriBuilder()
            .appendPath("t")
            .appendPath("${topicId.toString()}")
            .appendPath("posts.json")
            .build()
            .toString()

    fun createTopic() =
        uriBuilder()
            .appendPath("posts.json")
            .build()
            .toString()

    fun createPost() =
        uriBuilder()
            .appendPath("posts.json")
            .build()
            .toString()

    private fun uriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority(BuildConfig.DiscourseDomain)


}