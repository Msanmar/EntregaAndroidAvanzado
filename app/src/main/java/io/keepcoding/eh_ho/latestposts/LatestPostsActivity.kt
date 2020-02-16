package io.keepcoding.eh_ho.latestposts


import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.keepcoding.eh_ho.*
import io.keepcoding.eh_ho.di.DaggerApplicationGraph
import io.keepcoding.eh_ho.di.PostsModule
import io.keepcoding.eh_ho.di.UtilsModule
import io.keepcoding.eh_ho.domain.LatestPost


class LatestPostsActivity : AppCompatActivity(), LatestPostsFragment.LatestPostInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {

        //DaggerApplicationGraph.builder().utilsModule(UtilsModule(applicationContext)).build().inject(this)

        super.onCreate(savedInstanceState)

        Log.d("LATEST POST ACTIVITY","______________****************************************")
        setContentView(R.layout.latest_posts_activity)
        this.title = "Eh-Ho:       Latest Posts"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, LatestPostsFragment())
                .commit()
        }
    }


    override fun onPostSelected(latestPost: LatestPost) {

Log.d("LatestPostActivity", "...... OnPostSelected ${latestPost.post_title}")
    }

}