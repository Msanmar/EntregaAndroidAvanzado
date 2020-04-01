package io.keepcoding.eh_ho.topics


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.repository.TopicsRepo
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.Topic
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.android.synthetic.main.view_retry.*


class TopicsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var listener: TopicsInteractionListener? = null
    //var mLoading : Boolean = true
    var lastPage : Boolean = false
    var page : Int = 0
    var maxPageSize : Int = 30

    lateinit var adapter: TopicsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("............TopicsFragment", "__________!!!!!!!! on Attach")
        if (context is TopicsInteractionListener)
            listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(".............TopicsFragment", "__________!!!!!!!! onCreate")
        adapter = TopicsAdapter {

            goToPosts(it)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_topics, menu)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("............TopicsFragment", "__________onCreateView")


        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("..............TopicsFragment", "__________onViewCreated")
        listTopics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listTopics.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        //listTopics.removeAllViews()
        listTopics.adapter = adapter


        //Mover floating según el scroll
        listTopics.addOnScrollListener(object : RecyclerView.OnScrollListener(){


            var mPreviousTotal : Int = 0
            var page: Int = 1

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.d("Al inicio de onScrolled.....Page:", page.toString() )

                val visibleItemCount = listTopics.childCount
                Log.d("Scroll Listener, visibleItemCount",visibleItemCount.toString())
                val totalItemCount = (listTopics.layoutManager as LinearLayoutManager).itemCount
                Log.d("Scroll Listener, totalItemCount", totalItemCount.toString())
                val firstVisibleItemPosition = (listTopics.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                Log.d("Scroll Listener, firstVisibleItemPosition", firstVisibleItemPosition.toString())

               /* if (mLoading) {
                    if (totalItemCount > mPreviousTotal) {
                        mLoading = false
                        mPreviousTotal = totalItemCount

                    }
                }*/

                val visibleThreshold = 0 //margen extra de elementos visibles

                if ( !lastPage && (totalItemCount-visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {

                    Log.d("On Scrolled cumple condiciones para cargar más.......Page:", page.toString() )
                    maxPageSize = maxPageSize + 30
                    loadMoreTopics(page)
                    page++

                }


                //Mostramos o ocultamos botón según el scroll
                if (dy<0) { //Scrolling down
                    buttonCreate.show()
                }else if (dy>0) { //Scrolling up
                    buttonCreate.hide()
                }
            }

            /*

                int visibleItemCount = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();
    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
    if (!isLoading() && !isLastPage()) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        loadMoreItems();
      }
    }


             */




        })







        //Button Create Topic
        buttonCreate.setOnClickListener {
            Log.d("..............TopicsFragment","_______--goToCreateTopic")
            goToCreateTopic()
        }

        //Button Retry
        buttonRetry.setOnClickListener {
            loadTopics()
        }

        //Refresh
        swiperefresh.setOnRefreshListener {
            Log.v("SWIPEEEEEEEE........", "Aquí")
            loadTopics()
            swiperefresh.isRefreshing = false
        }


    } //onViewCreated


    fun getFilteredText(text: String){
        Log.d(".............Topics Fragment",".. get FILTERED TEXT")
        loadFilteredTopics(text)

    }



    override fun onRefresh() {
        loadTopics()
    }

   override fun onResume() {
        super.onResume()
        Log.d("............TopicsFragment", "__________onResume, vamos a loadTopics()")


      loadTopics()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_log_out -> listener?.onLogOut()
            // R.id.action_create_post -> listenerCreatePost?.onCreatePostFromTopics()
            R.id.action_latest_posts -> listener?.onGoToLatestPosts()

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


    // ______________________________________LOAD MORE TOPICS____________________________________________

    private fun loadMoreTopics(page: Int) {
        enableLoading(true)

        context?.let {
            TopicsRepo.getMoreTopics(page, it,
                {
                    enableLoading(false)
                    if (it.count()<30) {
                        Log.d("______________fIN", "Se recuperan menos de 30 topics")
                        lastPage = true
                    }
                    adapter.addMoreTopics(it)

                },
                {
                    enableLoading(false)
                    handleRequestError(it)
                })
        }


    }



    // ______________________________________LOAD FILTERED TOPICS____________________________________________


    private fun loadFilteredTopics(text: String) {
      // enableLoading(true)
    Log.d("..............TopicsFragment","LoadFilteredTopics, vamos a Topics Repo.getFilteredText")

        //listTopics.removeAllViews()


        context?.let {
            TopicsRepo.getFilteredTopics(text, it,
                {
                    enableLoading(false)


                  val topics = mutableListOf<Topic>()
                    //var filteredTopics : List<Topic>

                  //  val topics : MutableList<Topic> = ArrayList()


                    var filteredTopic : Topic

                    for (i in 0 until (it.size)-1) {

                        var filteredTopic = Topic(it[i].id,it[i].title,it[i].date,it[i].posts,0)
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
        fun onGoToLatestPosts()
    }


}
