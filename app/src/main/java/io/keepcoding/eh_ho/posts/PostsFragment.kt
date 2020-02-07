package io.keepcoding.eh_ho.posts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.keepcoding.eh_ho.R
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.data.Post
import io.keepcoding.eh_ho.data.PostsRepo
import io.keepcoding.eh_ho.data.RequestError
import kotlinx.android.synthetic.main.fragment_posts.*
import kotlinx.android.synthetic.main.fragment_posts.buttonCreate
import kotlinx.android.synthetic.main.fragment_posts.parentLayout


class PostsFragment(topicId: Int, topicTitle: String): Fragment(), SwipeRefreshLayout.OnRefreshListener{

    var topicID = topicId
    var topicTitle = topicTitle

    var listener: PostsInteractionListener? = null
    lateinit var adapter: PostsAdapter



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PostsInteractionListener)
            listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setHasOptionsMenu(true)

        adapter = PostsAdapter {
            detailPost(it)
        }

    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_post,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    listPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    listPosts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
       // adapter.setPosts(PostsRepo.posts)
    listPosts.adapter = adapter

    Log.d("SCROOOOOOOOLLLLLLL", listPosts.scrollState.toString())


       listPosts.addOnScrollListener(object : RecyclerView.OnScrollListener(){

           override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
               super.onScrolled(recyclerView, dx, dy)
               if (dy<0) {
                buttonCreate.show()
               }else if (dy>0) {
                buttonCreate.hide()
               }
           }

       })



    buttonCreate.setOnClickListener {
        goToCreatePost(topicID, topicTitle)
    }



        swiperefresh.setOnRefreshListener {
            Log.v("SWIPEEEEEEEE........", "Aquí")
            loadPosts()
            swiperefresh.isRefreshing = false
        }
}



    override fun onRefresh() {
        loadPosts()
    }



override fun onResume() {
    super.onResume()
    loadPosts()
}

    // ______________________________________LOAD POSTS____________________________________________

    private fun loadPosts() {
       // enableLoading(true)

       // adapter.setPosts(PostsRepo.posts)

        context?.let {
            PostsRepo.getPosts(it, topicID,
                {
                    adapter.setPosts(it)
                    //adapter.setPosts(PostsRepo.posts)

                },
                {
                    handleRequestError(it)
                })

        }


    }

    private fun detailPost(post: Post){
    Log.d("Post detail", post.title)
    }

    private fun handleRequestError(requestError: RequestError) {
        listPosts.visibility = View.INVISIBLE
       // viewRetry.visibility = View.VISIBLE

        val message = if (requestError.messageResId != null)
            getString(requestError.messageResId)
        else if (requestError.message != null)
            requestError.message
        else
            getString(R.string.error_request_default)

        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun goToCreatePost(topicID: Int, topicTitle: String) {
        listener?.onGoToCreatePost(topicID, topicTitle)
    }

//Definición de los métodos de la interfaz
    interface PostsInteractionListener {
        fun onGoToCreatePost(topicID: Int, topicTitle: String)
        fun onPostSelected()
    }
}