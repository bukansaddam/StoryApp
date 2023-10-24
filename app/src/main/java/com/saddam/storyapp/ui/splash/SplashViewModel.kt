package com.saddam.storyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.saddam.storyapp.data.Repository
import com.saddam.storyapp.data.pref.UserModel

class SplashViewModel(private val repository: Repository): ViewModel() {

    fun getUser(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}