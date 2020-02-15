package io.keepcoding.eh_ho.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.data.PostsRepo
import io.keepcoding.eh_ho.data.PostsDatabase
import javax.inject.Singleton

@Module
class PostsModule {

    @Singleton
    @Provides

    fun providePostRepo(context: Context, postDatabase: PostsDatabase): PostsRepo {
        PostsRepo.ctx = context
        PostsRepo.db = postDatabase
        return PostsRepo
    }

}
