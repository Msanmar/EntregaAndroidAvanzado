package io.keepcoding.eh_ho.latestposts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.PostsRepo
import io.keepcoding.eh_ho.data.RequestError
import io.keepcoding.eh_ho.data.TopicsRepo
import kotlinx.android.synthetic.main.fragment_topics.*


class LatestPostsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.latest_posts_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


    override fun onResume() {
        super.onResume()
        loadLatestPosts()
    }


    private fun loadLatestPosts() {
       // enableLoading(true)

Log.d("LOAD LATEST POSTS..........", "LOAD")

        context?.let {
            PostsRepo.getLatestPosts(it,
                {
                   // enableLoading(false)
                    Log.d("éxito.................", "LOAD")
                   // adapter.setTopics(it)
                },
                {
                    //enableLoading(false)
                    Log.d("fallo................", "LOAD")
                  //handleRequestError(it)
                })
        }
    }


    // ______________________________________LOAD LATEST POSTS____________________________________________

   /* private fun enableLoading(enabled: Boolean) {
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
*/

}