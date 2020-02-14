package io.keepcoding.eh_ho.di

import dagger.Component
import io.keepcoding.eh_ho.data.PostsRepo
import io.keepcoding.eh_ho.home.MainActivity
import io.keepcoding.eh_ho.latestposts.LatestPostsActivity
import io.keepcoding.eh_ho.latestposts.LatestPostsFragment

import javax.inject.Singleton

// @Component makes Dagger create a graph of dependencies
@Singleton
@Component(modules = [PostsModule::class, UtilsModule::class])
interface ApplicationGraph {
    // Add functions whose return value indicate what can be provided from this container

    // Add here as well functions whose input argument is the entity in which Dagger can add any
    // dependency you want

 //   fun inject(latestPostsFragment: LatestPostsFragment)
    //fun inject(latestPostsActivity: LatestPostsActivity)
    fun inject (mainActivity: MainActivity)
}