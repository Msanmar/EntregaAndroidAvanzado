package io.keepcoding.eh_ho.posts



import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.LoadingDialogFragment
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.CreatePostModel
import io.keepcoding.eh_ho.data.RequestError
import io.keepcoding.eh_ho.data.TopicsRepo
import io.keepcoding.eh_ho.data.PostsRepo
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.android.synthetic.main.fragment_create_post.inputContent
import kotlinx.android.synthetic.main.fragment_create_post.inputTitle
import kotlinx.android.synthetic.main.fragment_create_post.parentLayout
import kotlinx.android.synthetic.main.fragment_create_topic.*

const val TAG_LOADING_DIALOG = "loading_dialog"

class CreatePostFragment(topicId: Int, topicTitle: String) : Fragment() {

var listener: CreatePostInteractionListener? = null
    lateinit var loadingDialog: LoadingDialogFragment

    var topicID: Int = topicId
    var topicTitle: String = topicTitle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CreatePostInteractionListener)
            listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        loadingDialog = LoadingDialogFragment.newInstance("Create post")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //textTopicID.text = topicID.toString()
        textTopicID.text = topicTitle
        intTopicID.text = topicID.toString()

         //= topicID.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_post, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> createPost()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createPost() {
        if (isFormValid()) {
            postPost()
        } else {
            showErrors()
        }

    }

    //Método postPost
    private fun postPost() {

      val model = CreatePostModel(
          inputTitle.text.toString(),
          inputContent.text.toString(),
            topicID.toString()
      )


        context?.let{
            enableLoadingDialog(true)
            PostsRepo.createPost(it,
                model,
                {
                    enableLoadingDialog(false)
                    listener?.onPostCreated()
                },
                {
                    enableLoadingDialog(false)
                    handleError(it)
                }
            )
        }

    }
//TODO CreatePostModel

    //TODO PostRepo, añadir el .createPost



    private fun enableLoadingDialog(enabled: Boolean) {
        if (enabled)
            loadingDialog.show(childFragmentManager, io.keepcoding.eh_ho.topics.TAG_LOADING_DIALOG)
        else
            loadingDialog.dismiss()
    }

    private fun handleError(requestError: RequestError) {
        val message = if (requestError.messageResId != null)
            getString(requestError.messageResId)
        else if (requestError.message != null)
            requestError.message
        else
            getString(R.string.error_request_default)

        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showErrors() {
        if (inputTitle.text?.isEmpty() == true)
            inputTitle.error = getString(R.string.error_empty)
        if (inputContent.text?.isEmpty() == true)
            inputContent.error = getString(R.string.error_empty)
    }

    private fun isFormValid() =
        inputTitle.text?.isNotEmpty() ?: false &&
                inputContent.text?.isNotEmpty() ?: false



    interface CreatePostInteractionListener {
        fun onPostCreated()
    }

}