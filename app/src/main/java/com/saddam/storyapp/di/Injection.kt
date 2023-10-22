package com.saddam.storyapp.di

import android.content.Context
import com.saddam.storyapp.data.Repository
import com.saddam.storyapp.data.pref.UserPreference
import com.saddam.storyapp.data.pref.dataStore
import com.saddam.storyapp.data.retrofit.ApiConfig
import com.saddam.storyapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): Repository{
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        val pref = UserPreference.getInstance(context.dataStore)
        return Repository.getInstance(apiService, pref, appExecutors)
    }
}