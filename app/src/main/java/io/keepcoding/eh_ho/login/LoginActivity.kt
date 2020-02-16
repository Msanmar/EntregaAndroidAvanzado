package io.keepcoding.eh_ho.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.home.MainActivity
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.RequestError
import io.keepcoding.eh_ho.data.SignInModel
import io.keepcoding.eh_ho.data.SignUpModel
import io.keepcoding.eh_ho.data.UserRepo
import retrofit2.Retrofit
import retrofit2.Response
import io.keepcoding.eh_ho.topics.TopicsActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoginActivity : AppCompatActivity(),
    SignInFragment.SignInInteractionListener,
    SignUpFragment.SignUpInteractionListener,  CoroutineScope{

    val signInFragment: SignInFragment =
        SignInFragment()
    val signUpFragment: SignUpFragment =
        SignUpFragment()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            checkSession()
        }
    }

    private fun checkSession() {
        if (UserRepo.isLogged(this)) {
           // launchTopicsActivity()

            Log.d("CheckSessión en Login Activity", "Vamos a MainActiivty")
            launchHomeActivity()
        }
        else {
            onGoToSignIn()
        }
    }

    override fun onGoToSignUp() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signUpFragment)
            .commit()
    }

    override fun onGoToSignIn() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signInFragment)
            .commit()
    }

    override fun onSignIn(signInModel: SignInModel) {
        enableLoading(true)
        UserRepo.signIn(this, signInModel,
            {
                enableLoading(false)
                //launchTopicsActivity()
                launchHomeActivity()
            },
            {
                enableLoading(false)
                handleRequestError(it)
            })
    }

    //Retrofit
    override fun onSignInWithRetrofitAsync(model: SignInModel) {

        Log.d("______________","ON SIGNIN WITH RETROFIT ASYNC ________")
          enableLoading(true)
//            userRepo.signIn(model,
//            userRepo.signInWithRetrofitSynchronously(model,
        UserRepo.signInWithRetrofitAsynchronously(this,model,
                {
                    UserRepo.saveSession(this,username = model.username)
                    enableLoading(false)
                   // launchTopicsActivity()
                    launchHomeActivity()
                },
                {
                    enableLoading(false)
                    handleRequestError(it)
                })

    }

    override fun onSignInWithRetrofitSyncCoroutines(model: SignInModel) {

Log.d("LoginActivity________","___________onSignINWithRetrofitSyncCoroutines")
            enableLoading(true)

            val job = async {
                val a = UserRepo.signInWithRetrofitSynchronouslyWithinCoroutines(signInModel = model)
                println("Done async")
                a
            }

            launch(Dispatchers.Main) {
                val response: Response<SignInModel> = job.await()
                println("Done await")

            enableLoading(false)
                if (response.isSuccessful) {
                    response.body().takeIf { it != null }
                        ?.let {
                            UserRepo.saveSession(this@LoginActivity,username = model.username)
                          launchHomeActivity()
                        }
                        ?: run { handleRequestError(RequestError(message = "Body is null")) }
                } else {
                    handleRequestError(RequestError(message = response.errorBody()?.toString()))
                }
                println("Lanzado")
            }
            println("Hecho")



    }




    override fun onSignUp(signUpModel: SignUpModel) {
        enableLoading(true)
        UserRepo.signUp(this,
            signUpModel,
            {
                enableLoading(false)
                //launchTopicsActivity()
                launchHomeActivity()
            },
            {
                enableLoading(false)
                handleRequestError(it)
            })
    }

    private fun handleRequestError(requestError: RequestError) {
        val message = if (requestError.messageResId != null)
            getString(requestError.messageResId)
        else if (requestError.message != null)
            requestError.message
        else
            getString(R.string.error_request_default)

        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun enableLoading(enabled: Boolean) {
        if (enabled) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun launchTopicsActivity() {
        val intent = Intent(this, TopicsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchHomeActivity() {
        Log.d("LaunchHomeActivity....", "Vamos a MainActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
