package io.keepcoding.eh_ho.data

import io.keepcoding.eh_ho.data.SignInModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface AuthenticationService {

    @GET("users/{username}.json")
    fun loginUser(@Path("username") username: String): Call<SignInModel>

    @GET("users/{username}.json")
    suspend fun loginUserWithCoroutines(@Path("username") username: String): Response<SignInModel>

}

