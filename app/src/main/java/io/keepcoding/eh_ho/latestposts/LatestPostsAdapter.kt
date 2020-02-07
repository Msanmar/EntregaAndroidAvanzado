package io.keepcoding.eh_ho.latestposts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_post.view.*
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.LatestPost
import io.keepcoding.eh_ho.data.Post
import kotlinx.android.synthetic.main.item_post.view.labelDatePost
import kotlinx.android.synthetic.main.item_post.view.labelTitlePost
import kotlinx.android.synthetic.main.item_post.view.labelTopicID

class LatestPostsAdapter (val postClickListener: ((LatestPost) -> Unit)? = null): RecyclerView.Adapter<LatestPostsAdapter.PostHolder>() {
    private val posts = mutableListOf<LatestPost>()

    private var listener : ((View) -> Unit) = {
        val post = it.tag as LatestPost
        postClickListener?.invoke(post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_latest_post,parent,false)

    return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        holder.post = post
        holder.itemView.setOnClickListener(listener)
        //Set on click
    }

    fun setPosts(posts: List<LatestPost>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var post: LatestPost? = null

        set (value) {
            field = value

            with(itemView) {
                tag = field
                field?.let{
                    //labelTitlePost.text = it.title
                   // imageUser = it.
                    var lon = it.topic_title.length

                    labelTitlePost.text = it.topic_title.substring(3, lon-4)
                    //labelContentPost.text = it.content
                    labelDatePost.text = it.date.toString()
                    labelTopicID.text = "TopicID: " + it.topicId.toString()
                    labelUsername.text = it.author
                }

            }

        }


    }//LatestPostHolder



}




