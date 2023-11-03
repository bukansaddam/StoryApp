package com.saddam.storyapp.data.retrofit

import com.saddam.storyapp.data.response.DetailResponse
import com.saddam.storyapp.data.response.FileUploadResponse
import com.saddam.storyapp.data.response.LoginResponse
import com.saddam.storyapp.data.response.RegisterResponse
import com.saddam.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories/{id}")
    fun getDetail(
        @Path("id") id: String
    ): Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): Call<FileUploadResponse>

    @GET("stories")
    fun getStoryLocation(
        @Query("location") location: Int = 1,
    ): Call<StoryResponse>
}