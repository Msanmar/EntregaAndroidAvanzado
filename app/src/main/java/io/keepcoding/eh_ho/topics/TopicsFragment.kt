package io.keepcoding.eh_ho.topics


import android.content.Context
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.view.*
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.RequestError
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.data.TopicsRepo
import io.keepcoding.eh_ho.posts.CreatePostFragment
import kotlinx.android.synthetic.main.fragment_posts.*
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.android.synthetic.main.fragment_topics.buttonCreate
import kotlinx.android.synthetic.main.fragment_topics.parentLayout
import kotlinx.android.synthetic.main.fragment_topics.swiperefresh
import kotlinx.android.synthetic.main.view_retry.*

class TopicsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var listener: TopicsInteractionListener? = null


    lateinit var adapter: TopicsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopicsInteractionListener)
            listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = TopicsAdapter {
        goToPosts(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_topics, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listTopics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listTopics.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listTopics.adapter = adapter



        listTopics.addOnScrollListener(object : RecyclerView.OnScrollListener(){

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
            goToCreateTopic()
        }

        buttonRetry.setOnClickListener {
            loadTopics()
        }

        swiperefresh.setOnRefreshListener {
            Log.v("SWIPEEEEEEEE........", "Aquí")
            loadTopics()
            swiperefresh.isRefreshing = false
        }


    }

    override fun onRefresh() {
        loadTopics()
    }

    override fun onResume() {
        super.onResume()
        loadTopics()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_log_out -> listener?.onLogOut()
            // R.id.action_create_post -> listenerCreatePost?.onCreatePostFromTopics()

        }
        return super.onOptionsItemSelected(item)
    }

    // ______________________________________LOAD TOPICS____________________________________________

    private fun loadTopics() {
        enableLoading(true)



        context?.let {
            TopicsRepo.getTopics(it,
                {
                    enableLoading(false)

                    adapter.setTopics(it)
                },
                {
                    enableLoading(false)
                    handleRequestError(it)
                })
        }
    }

    // ______________________________________LOAD TOPICS____________________________________________

    private fun enableLoading(enabled: Boolean) {
        viewRetry.visibility = View.INVISIBLE

        if (enabled) {
            listTopics.visibility = View.INVISIBLE
            buttonCreate.hide()
            viewLoading.visibility = View.VISIBLE
        } else {
            listTopics.visibility = View.VISIBLE
            buttonCreate.show()
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun handleRequestError(requestError: RequestError) {
        listTopics.visibility = View.INVISIBLE
        viewRetry.visibility = View.VISIBLE

        val message = if (requestError.messageResId != null)
            getString(requestError.messageResId)
        else if (requestError.message != null)
            requestError.message
        else
            getString(R.string.error_request_default)

        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun goToCreateTopic() {
        listener?.onGoToCreateTopic()
    }



    private fun goToPosts(it: Topic) {

        listener?.onTopicSelected(it)
    }

    interface TopicsInteractionListener {
            fun onTopicSelected(topic: Topic)
            fun onGoToCreateTopic()
            fun onLogOut()
    }


}
