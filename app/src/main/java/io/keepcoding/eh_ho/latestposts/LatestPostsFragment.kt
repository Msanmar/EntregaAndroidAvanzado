package io.keepcoding.eh_ho.latestposts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.repository.PostsRepo
import io.keepcoding.eh_ho.di.DaggerApplicationGraph
import io.keepcoding.eh_ho.di.UtilsModule
import io.keepcoding.eh_ho.domain.LatestPost

import kotlinx.android.synthetic.main.latest_posts_fragment.*
import javax.inject.Inject


class LatestPostsFragment () : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    var listener: LatestPostInteractionListener? = null
    lateinit var adapter: LatestPostsAdapter

    @Inject
    lateinit var injectedPostsRepo: PostsRepo

    override fun onAttach(context: Context) {
      //  Log.d("LATEST POST FRAGMENT ","ON ATTACH______________****************************************")

  //   DaggerApplicationGraph.builder().utilsModule(UtilsModule(context)).build().inject(this)
        super.onAttach(context)

        if (context is LatestPostInteractionListener)
            listener = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("LATEST POST FRAGMENT ","ON CREATE______________****************************************")
     // DaggerApplicationGraph.builder().utilsModule(UtilsModule(appcontext)).build().inject(this)

        super.onCreate(savedInstanceState)
        //  setHasOptionsMenu(true)

        adapter = LatestPostsAdapter {
          goToTopic(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.latest_posts_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("LATEST POST FRAGMENT ","On activity created______________******")

    var ctx = context!!
      DaggerApplicationGraph.builder()
            .utilsModule(UtilsModule(ctx)).build()
            .inject(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listLatestPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        listLatestPosts.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        listLatestPosts.adapter = adapter
        swipeRefreshLatest.setOnRefreshListener {
            Log.v("SWIPEEEEEEEE........", "Aquí")
            loadLatestPosts()
            swipeRefreshLatest.isRefreshing = false
        }
    }


    override fun onRefresh() {
        loadLatestPosts()
    }


    override fun onResume() {
        super.onResume()
        loadLatestPosts()
    }


    private fun loadLatestPosts() {
       // enableLoading(true)


Log.d("LOAD LATEST POSTS..........", "LOAD")


        context?.let {
            injectedPostsRepo.getLatestPosts(it,
                {
                   // enableLoading(false)
                    Log.d("éxito.................", "LOAD")
                   adapter.setPosts(it)
                },
                {
                    //enableLoading(false)
                    Log.d("fallo................", "LOAD")
                  //handleRequestError(it)
                })
        }

        Log.d("LOAD LATEST POSTS..........", "fin load")

    }

private fun goToTopic(it: LatestPost) {
    listener?.onPostSelected(it)
}


    interface LatestPostInteractionListener {
        fun onPostSelected(latestPost: LatestPost)

    }


}
