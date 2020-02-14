package io.keepcoding.eh_ho.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.data.Post
import io.keepcoding.eh_ho.data.PostsDatabase
import javax.inject.Singleton

@Module
class UtilsModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Context = context

    @Singleton
    @Provides
    fun providePostDb(ctx: Context): PostsDatabase = Room.databaseBuilder(
        context, PostsDatabase::class.java, "posts_database"
    ).build()

   // fun providePostDb() : PostsDatabase = Room.databaseBuilder(context, PostsDatabase::class.java, "posts_database").build()

}