package io.keepcoding.eh_ho.data

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class Topic(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: Date = Date(),
    val posts: Int = 0,
    val views: Int = 0
) {
    companion object {

        fun parseTopics(response: JSONObject): List<Topic> {
            val jsonTopics = response.getJSONObject("topic_list")
                .getJSONArray("topics")

            val topics = mutableListOf<Topic>()


            for (i in 0 until jsonTopics.length()) {
                val parsedTopic = parseTopic(jsonTopics.getJSONObject(i))
                Log.d("Topic---------------------",parsedTopic.title)
                topics.add(parsedTopic)
            }

            return topics
        }

        fun parseTopic(jsonObject: JSONObject): Topic {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()

            return Topic(
                jsonObject.getInt("id").toString(),
                jsonObject.getString("title"),
                dateFormatted,
                jsonObject.getInt("posts_count"),
                jsonObject.getInt("views")
            )
        }

        const val MINUTES_MILLIS = 1000L * 60
        const val HOUR_MILLIS = MINUTES_MILLIS * 60
        const val DAY_MILLIS = HOUR_MILLIS * 24
        const val MONTH_MILLIS = DAY_MILLIS * 30
        const val YEAR_MILLIS = MONTH_MILLIS * 12
    }

    data class TimeOffset(val amount: Int, val unit: Int)

    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {
        val current = dateToCompare.time
        val diff = current - date.time

        val years = diff / YEAR_MILLIS
        if (years > 0) return TimeOffset(years.toInt(), Calendar.YEAR)

        val month = diff / MONTH_MILLIS
        if (month > 0) return TimeOffset(month.toInt(), Calendar.MONTH)

        val days = diff / DAY_MILLIS
        if (days > 0) return TimeOffset(days.toInt(), Calendar.DAY_OF_MONTH)

        val hours = diff / HOUR_MILLIS
        if (hours > 0) return TimeOffset(hours.toInt(), Calendar.HOUR)

        val minutes = diff / MINUTES_MILLIS
        if (minutes > 0) return TimeOffset(minutes.toInt(), Calendar.MINUTE)

        return TimeOffset(0, Calendar.MINUTE)
    }
}

data class Post (
    val title: String,
    val content: String,
    val author: String,
    val topicId: Int,
    val avatar: String,
    val date: Date = Date()
){
    companion object {

        fun parsePosts(response: JSONObject): List<Post> {


            val jsonPosts = response.getJSONObject("post_stream")
                .getJSONArray("posts")

            val posts = mutableListOf<Post>()


            for (i in 0 until jsonPosts.length()) {
                val parsedPost = parsePost(jsonPosts.getJSONObject(i))
                Log.d("Post---------------------",parsedPost.title)
                Log.d("Post---------------------",parsedPost.author)
                Log.d("Post AVATAR---------------------",parsedPost.avatar)
                posts.add(parsedPost)
        }


            return posts
        }

        fun parsePost(jsonObject: JSONObject): Post {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()


            return Post(
                //jsonObject.getInt("id").toString(),
                jsonObject.getString("cooked"),
                jsonObject.getString("topic_slug"),
                jsonObject.getString("username"),
                jsonObject.getInt("topic_id"),
                jsonObject.getString("avatar_template"),
                dateFormatted
            )
        }

        const val MINUTES_MILLIS = 1000L * 60
        const val HOUR_MILLIS = MINUTES_MILLIS * 60
        const val DAY_MILLIS = HOUR_MILLIS * 24
        const val MONTH_MILLIS = DAY_MILLIS * 30
        const val YEAR_MILLIS = MONTH_MILLIS * 12
    }

    data class TimeOffset(val amount: Int, val unit: Int)

    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {
        val current = dateToCompare.time
        val diff = current - date.time

        val years = diff / YEAR_MILLIS
        if (years > 0) return TimeOffset(years.toInt(), Calendar.YEAR)

        val month = diff / MONTH_MILLIS
        if (month > 0) return TimeOffset(month.toInt(), Calendar.MONTH)

        val days = diff / DAY_MILLIS
        if (days > 0) return TimeOffset(days.toInt(), Calendar.DAY_OF_MONTH)

        val hours = diff / HOUR_MILLIS
        if (hours > 0) return TimeOffset(hours.toInt(), Calendar.HOUR)

        val minutes = diff / MINUTES_MILLIS
        if (minutes > 0) return TimeOffset(minutes.toInt(), Calendar.MINUTE)

        return TimeOffset(0, Calendar.MINUTE)
    }
}

//LatestPost

data class LatestPost(
    val topic_title: String,
    val topic_slug: String,
    val author: String,
    val topicId: Int,
    val score: Double,
    val post_number: Int,
    val post_title: String,
    val date: Date = Date()
){
    companion object {

        fun parseLatestPosts(response: JSONObject): List<LatestPost> {


            //val jsonLatestPosts = response.getJSONObject("latest_posts")
            //
            //    .getJSONArray("posts")

            val jsonLatestPosts = response.getJSONArray("latest_posts")

            val posts = mutableListOf<LatestPost>()


            for (i in 0 until jsonLatestPosts.length()) {
                val parsedPost = parseLatestPost(jsonLatestPosts.getJSONObject(i))
                //Log.d("Post: topic slug --------------------",parsedPost.topic_slug)
                Log.d("Post: topic_title -------------------",parsedPost.topic_title)
                Log.d("Post number--------------------------",parsedPost.post_number.toString())
                Log.d("Post title---------------------------", parsedPost.post_title)
                posts.add(parsedPost)
            }


            return posts
        }

        fun parseLatestPost(jsonObject: JSONObject): LatestPost {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()


            return LatestPost(
                //jsonObject.getInt("id").toString(),
                jsonObject.getString("topic_title"),
                jsonObject.getString("topic_slug"),
                jsonObject.getString("username"),
                jsonObject.getInt("topic_id"),
                jsonObject.getDouble("score"),
                jsonObject.getInt("post_number"),
                jsonObject.getString("raw"),
                dateFormatted
            )
        }

        const val MINUTES_MILLIS = 1000L * 60
        const val HOUR_MILLIS = MINUTES_MILLIS * 60
        const val DAY_MILLIS = HOUR_MILLIS * 24
        const val MONTH_MILLIS = DAY_MILLIS * 30
        const val YEAR_MILLIS = MONTH_MILLIS * 12
    }

    data class TimeOffset(val amount: Int, val unit: Int)

    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {
        val current = dateToCompare.time
        val diff = current - date.time

        val years = diff / YEAR_MILLIS
        if (years > 0) return TimeOffset(years.toInt(), Calendar.YEAR)

        val month = diff / MONTH_MILLIS
        if (month > 0) return TimeOffset(month.toInt(), Calendar.MONTH)

        val days = diff / DAY_MILLIS
        if (days > 0) return TimeOffset(days.toInt(), Calendar.DAY_OF_MONTH)

        val hours = diff / HOUR_MILLIS
        if (hours > 0) return TimeOffset(hours.toInt(), Calendar.HOUR)

        val minutes = diff / MINUTES_MILLIS
        if (minutes > 0) return TimeOffset(minutes.toInt(), Calendar.MINUTE)

        return TimeOffset(0, Calendar.MINUTE)
    }
}