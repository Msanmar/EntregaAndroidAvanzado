package io.keepcoding.eh_ho.data

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.room.Room
import io.keepcoding.eh_ho.data.PostsDatabase
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.ServerError
import io.keepcoding.eh_ho.R
import org.json.JSONObject
import kotlin.concurrent.thread


object PostsRepo  {


    lateinit var db: PostsDatabase
    lateinit var ctx: Context

    fun getLatestPosts(
        context: Context,
        onSuccess: (List<LatestPost>) -> Unit,
        onError: (RequestError) -> Unit
    ) {

        Log.d("GET LATEST POSTS", "...... POSTS REPO.................")

     /*   var db : PostsDatabase = Room.databaseBuilder(
            context, PostsDatabase::class.java, "posts_database"
        ).build()*/

        val username = UserRepo.getUsername(context)

        val request = UserRequest(
            username,
            Request.Method.GET,
            ApiRoutes.getLatestPosts(),
            null,
            {
                it?.let {

                    onSuccess.invoke(LatestPost.parseLatestPosts(it))
                    thread {
                       db.postDao().insertAll(LatestPost.parseLatestPosts(it).toEntity())
                        Log.d("GUARDADOS POSTS EN BBBD", "................. OK GUARDADOS")
                    }
                }

                if (it == null)
                    onError.invoke(RequestError(messageResId = R.string.error_invalid_response))
            },
            {
                it.printStackTrace()
                if (it is NetworkError)
                    onError.invoke(RequestError(messageResId = R.string.error_network))
                else
                    onError.invoke(RequestError(it))
            })

        ApiRequestQueue.getRequestQueue(context)
            .add(request)
    }



    fun getPosts(
        context: Context,
        topicId: Int,
        onSuccess: (List<Post>) -> Unit,
        onError: (RequestError) -> Unit
    ) {
        val username = UserRepo.getUsername(context)
        val request = UserRequest(
            username,
            Request.Method.GET,
            ApiRoutes.getPosts(topicId),
            null,
            {
                it?.let {

                    onSuccess.invoke(Post.parsePosts(it))
                }

                if (it == null)
                    onError.invoke(RequestError(messageResId = R.string.error_invalid_response))
            },
            {
                it.printStackTrace()
                if (it is NetworkError)
                    onError.invoke(RequestError(messageResId = R.string.error_network))
                else
                    onError.invoke(RequestError(it))
            })

        ApiRequestQueue.getRequestQueue(context)
            .add(request)
    }



    fun createPost(
        context: Context,
        model: CreatePostModel,
        onSuccess: (CreatePostModel) -> Unit,
        onError: (RequestError) -> Unit
    ) {
        val username = UserRepo.getUsername(context)
        val request = UserRequest(
            username,
            Request.Method.POST,
            ApiRoutes.createPost(),
            model.toJson(),
            {
                it?.let {
                    onSuccess.invoke(model)
                }

                if (it == null)
                    onError.invoke(RequestError(messageResId = R.string.error_invalid_response))
            },
            {
                it.printStackTrace()

                if (it is ServerError && it.networkResponse.statusCode == 422) {
                    val body = String(it.networkResponse.data, Charsets.UTF_8)
                    val jsonError = JSONObject(body)
                    val errors = jsonError.getJSONArray("errors")
                    var errorMessage = ""

                    for (i in 0 until errors.length()) {
                        errorMessage += "${errors[i]} "
                    }

                    onError.invoke(RequestError(it, message = errorMessage))

                } else if (it is NetworkError)
                    onError.invoke(RequestError(it, messageResId = R.string.error_network))
                else
                    onError.invoke(RequestError(it))
            }
        )

        ApiRequestQueue.getRequestQueue(context)
            .add(request)
    }



}

private fun List<LatestPost>.toEntity(): List<LatestPostEntity> = map { it.toEntity() }

private fun LatestPost.toEntity(): LatestPostEntity = LatestPostEntity(

    topic_title = topic_title,
    topicId = topicId,
    topic_slug = topic_slug,
    post_number = post_number,
    post_title = post_title,
    author = author,
   date = date.toString()
)

