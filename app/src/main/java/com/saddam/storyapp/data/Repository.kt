package com.saddam.storyapp.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saddam.storyapp.data.pref.UserModel
import com.saddam.storyapp.data.pref.UserPreference
import com.saddam.storyapp.data.response.DetailResponse
import com.saddam.storyapp.data.response.FileUploadResponse
import com.saddam.storyapp.data.response.LoginResponse
import com.saddam.storyapp.data.response.RegisterResponse
import com.saddam.storyapp.data.response.StoryResponse
import com.saddam.storyapp.data.retrofit.ApiService
import com.saddam.storyapp.helper.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    companion object{
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }

        fun clearInstance(){
            instance = null
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>>{
        val result = MutableLiveData<Result<LoginResponse>>()
        result.value = Result.Loading

        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    Log.i(TAG, "onResponse: login berhasil")
                    result.value = Result.Success(responseBody)
                }else{
                    Log.e(TAG, "onResponse: ${response.message()}", )
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Repository", "onFailure: ${t.message}", )
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>>{
        val result = MutableLiveData<Result<RegisterResponse>>()
        result.value = Result.Loading

        val client = apiService.register(name, email, password)
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody)
                }else{
                    Log.e(TAG, "onResponse: ${response.message()}", )
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Repository", "onFailure: ${t.message}", )
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun getAllstories() : LiveData<Result<StoryResponse>>{
        val result = MutableLiveData<Result<StoryResponse>>()
        result.value = Result.Loading

        apiService.getAllStories().enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody)
                    Log.i(TAG, "onSuccess : ${responseBody.listStory}")
                }else{
                    Log.e("getAllStory", "onResponse: ${response.message()}", )
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("getAllStory", "onFailure: $t", )
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun getDetail(id: String) : LiveData<Result<DetailResponse>>{
        val result = MutableLiveData<Result<DetailResponse>>()
        result.value = Result.Loading

        val client = apiService.getDetail(id)
        client.enqueue(object : Callback<DetailResponse>{
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody)
                    Log.i("Detail", "onResponse: $responseBody")
                }else{
                    Log.e("Detail", "error: ${response.message()}", )
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    fun sendStory(file: MultipartBody.Part, description: RequestBody) : LiveData<Result<FileUploadResponse>>{
        val result = MutableLiveData<Result<FileUploadResponse>>()
        result.value = Result.Loading

        val client = apiService.addStory(file, description)
        client.enqueue(object: Callback<FileUploadResponse>{
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody)
                }else{
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    fun getStoryLocation(): LiveData<Result<StoryResponse>>{
        val result = MutableLiveData<Result<StoryResponse>>()
        result.value = Result.Loading

        apiService.getStoryLocation().enqueue( object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
}