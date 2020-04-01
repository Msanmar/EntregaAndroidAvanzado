package io.keepcoding.eh_ho.topics

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.repository.TopicsRepo
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.Topic
import io.keepcoding.eh_ho.home.EXTRA_QUERY_TEXT
import kotlinx.android.synthetic.main.activity_filter_topics.*


class FilterTopicsActivity : AppCompatActivity() {

    lateinit var adapter: TopicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FILTER TOPICS_ACTIVITY","______________****************************************")
        setContentView(R.layout.activity_filter_topics)
        this.title = "Eh-Ho:      Filtering topics"


        val textQuery = intent.getStringExtra(EXTRA_QUERY_TEXT)
        Log.d("TextQuery",".........$textQuery")

        if(textQuery != null) {
            this.title = "Eh-Ho       " + textQuery
        } else {
            throw IllegalArgumentException("You should provide an id for the topic")
        }


        adapter = TopicsAdapter {

        }



        listFilterTopics.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listFilterTopics.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        //listTopics.removeAllViews()
        listFilterTopics.adapter = adapter


    loadFilteredTopics(textQuery)


        swiperefresh.setOnRefreshListener {
            Log.v("SWIPEEEEEEEE........", "Aquí")
            loadFilteredTopics(textQuery)
            swiperefresh.isRefreshing = false
        }


    }


    private fun loadFilteredTopics(text: String) {
        // enableLoading(true)
        Log.d("..............FilterTopicsActivity","....loadFilteredTopics")

        //listTopics.removeAllViews()


        this.let {
            TopicsRepo.getFilteredTopics(text, it,
                {
                    enableLoading(false)


                    val topics = mutableListOf<Topic>()
                    //var filteredTopics : List<Topic>

                    //  val topics : MutableList<Topic> = ArrayList()


                    var filteredTopic : Topic

                    for (i in 0 until (it.size)-1) {

                        var filteredTopic = Topic(it[i].id,it[i].title,it[i].date,it[i].posts,0)

                        /* filteredTopic.id = it[i].id
                         filteredTopic.posts = it[i].posts
                         filteredTopic.title = it[i].title
                         filteredTopic.date = it[i].date
                         filteredTopic.views = 0*/

                        topics.add(filteredTopic)

                    }

                    //listTopics.removeAllViews()

                    adapter.setTopics(topics)



                },
                {
                    enableLoading(false)
                    handleRequestError(it)
                })
        }
    }



    private fun enableLoading(enabled: Boolean) {
        viewRetry.visibility = View.INVISIBLE

        if (enabled) {
            listFilterTopics.visibility = View.INVISIBLE

            viewLoading.visibility = View.VISIBLE
        } else {
            listFilterTopics.visibility = View.VISIBLE

            viewLoading.visibility = View.INVISIBLE
        }
    }



    private fun handleRequestError(requestError: RequestError) {
        listFilterTopics.visibility = View.INVISIBLE
        viewRetry.visibility = View.VISIBLE

        val message = if (requestError.messageResId != null)
            getString(requestError.messageResId)
        else if (requestError.message != null)
            requestError.message
        else
            getString(R.string.error_request_default)

        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }


}