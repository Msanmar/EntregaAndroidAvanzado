package io.keepcoding.eh_ho.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.domain.Topic
import io.keepcoding.eh_ho.data.repository.UserRepo
import io.keepcoding.eh_ho.domain.LatestPost
import io.keepcoding.eh_ho.latestposts.LatestPostsActivity
import io.keepcoding.eh_ho.latestposts.LatestPostsFragment
import io.keepcoding.eh_ho.login.LoginActivity
import io.keepcoding.eh_ho.posts.EXTRA_TOPIC_ID
import io.keepcoding.eh_ho.posts.EXTRA_TOPIC_TITLE
import io.keepcoding.eh_ho.posts.PostsActivity
import io.keepcoding.eh_ho.topics.CreateTopicFragment
import io.keepcoding.eh_ho.topics.TopicsFragment

const val EXTRA_TOPIC_ID = "topic_id"
const val EXTRA_TOPIC_TITLE = "topic_title"
const val TRANSACTION_CREATE_TOPIC = "create_topic"

class MainActivity : AppCompatActivity(), TopicsFragment.TopicsInteractionListener, CreateTopicFragment.CreateTopicInteractionListener, LatestPostsFragment.LatestPostInteractionListener  {



    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

    /*    var ctx = applicationContext
        DaggerApplicationGraph.builder()
            .utilsModule(UtilsModule(applicationContext)).build()
            .inject(this)*/

       // DaggerApplicationGraph.builder().utilsModule(UtilsModule(context)).build().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // val toolbar: Toolbar = findViewById(R.id.toolbar)
      //  setSupportActionBar(toolbar)

        Log.d("MainActivity", "drawer_layout, nav_view, nav_host_fragment")

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        Log.d("Vamos a configurar AppBAR","Vamos a configurar APP BAR")

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_topics, R.id.nav_posts), drawerLayout
        )

        Log.d("Asociamos navigation controller", "appBarConfiguracion")
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    //_______________Métodos interfaz TopicsInteractionListener
    override fun onTopicSelected(topic: Topic) {
        //Toast.makeText(this, topic.title, Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", "OnTopicSelected")
        goToPosts(topic)
    }

    override fun onGoToCreateTopic() {
        Log.d("MainActivity","_______OnGoToCreateTopic")
       // CreateTopicFragment()
      supportFragmentManager.beginTransaction()
            .replace(R.id.parentLayout, CreateTopicFragment())
          .addToBackStack(io.keepcoding.eh_ho.home.TRANSACTION_CREATE_TOPIC)
            .commit()


    }


    override fun onLogOut() {
        UserRepo.logOut(this)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onGoToLatestPosts() {
        val intent = Intent(this, LatestPostsActivity:: class.java)
        startActivity(intent)
        finish()
    }

    //_______________Métodos interfaz CreateTopicInteractionListener
    override fun onTopicCreated() {
        supportFragmentManager.popBackStack()
    }

    override fun onPostSelected(latestPost: LatestPost) {
        Log.d("LatestPostActivity", "...... OnPostSelected ${latestPost.topicId}")
        //Mostramos una vista de detalle del detalle de ese topic, con todos sus posts
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, latestPost.topicId.toString())
        intent.putExtra(EXTRA_TOPIC_TITLE, latestPost.topic_title)

        // Toast.makeText(this, "Vamos a post activity", Toast.LENGTH_SHORT).show()
        startActivity(intent)

    }


    private fun goToPosts(topic: Topic) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        intent.putExtra(EXTRA_TOPIC_TITLE, topic.title)

        // Toast.makeText(this, "Vamos a post activity", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }


}
