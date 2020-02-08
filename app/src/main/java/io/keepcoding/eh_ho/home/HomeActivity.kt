package io.keepcoding.eh_ho.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.latestposts.LatestPostsActivity
import io.keepcoding.eh_ho.topics.TopicsActivity
import kotlinx.android.synthetic.main.activity_home2.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        Log.d("HOME", "HOMEEEEEE")
        //this.title = "Eh-Ho:       Latest Topics"


            buttonTopics.setOnClickListener {
                launchTopicsActivity()
            }

            buttonPosts.setOnClickListener {
                launchLatestPostsActivity()
            }



    }


    private fun launchTopicsActivity() {
        val intent = Intent(this, TopicsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchLatestPostsActivity() {
        val intent = Intent(this, LatestPostsActivity::class.java)
        startActivity(intent)
        finish()
    }




}