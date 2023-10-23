package com.saddam.storyapp.data.retrofit

import com.saddam.storyapp.data.response.LoginResponse
import com.saddam.storyapp.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String): Call<RegisterResponse>
}