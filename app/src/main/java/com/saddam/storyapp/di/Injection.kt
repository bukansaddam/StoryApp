package com.saddam.storyapp.di

import android.content.Context
import com.saddam.storyapp.data.Repository
import com.saddam.storyapp.data.pref.UserPreference
import com.saddam.storyapp.data.pref.dataStore
import com.saddam.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(apiService, pref)
    }
}