package com.saddam.storyapp.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saddam.storyapp.data.pref.UserModel
import com.saddam.storyapp.data.pref.UserPreference
import com.saddam.storyapp.data.response.LoginResponse
import com.saddam.storyapp.data.response.RegisterResponse
import com.saddam.storyapp.data.response.StoryResponse
import com.saddam.storyapp.data.retrofit.ApiService
import com.saddam.storyapp.helper.Result
import com.saddam.storyapp.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val appExecutors: AppExecutors
){
    companion object{
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            appExecutors: AppExecutors
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userPreference, appExecutors)
            }.also { instance = it }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>>{
        val result = MutableLiveData<Result<LoginResponse>>()
        result.value = Result.Loading

        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    val token = responseBody.loginResult?.token
                    val user = UserModel(email, token.toString())
                    Log.i(TAG, "token: ${token.toString()}")
                    val coroutine = CoroutineScope(Dispatchers.IO)
                    coroutine.launch { saveSession(user) }
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

        val client = apiService.getAllStories()
        client.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody)
                }else{
                    Log.e("getAllStory", "onResponse: ${response.message()}", )
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("getAllStory", "onFailure: ${t.toString()}", )
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