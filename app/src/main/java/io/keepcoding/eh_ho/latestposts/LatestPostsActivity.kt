package io.keepcoding.eh_ho.latestposts


import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.keepcoding.eh_ho.*




class LatestPostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.latest_posts_activity)
        this.title = "Eh-Ho:       Latest Posts"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, LatestPostsFragment())
                .commit()
        }
    }

}