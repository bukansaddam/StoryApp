package com.saddam.storyapp.data.retrofit

import com.saddam.storyapp.data.response.DetailResponse
import com.saddam.storyapp.data.response.LoginResponse
import com.saddam.storyapp.data.response.RegisterResponse
import com.saddam.storyapp.data.response.StoryResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String): Call<RegisterResponse>

    @GET("stories")
    fun getAllStories(): Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetail(@Path("id") id: String): Call<DetailResponse>
}