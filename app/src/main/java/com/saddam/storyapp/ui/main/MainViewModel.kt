package com.saddam.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.saddam.storyapp.data.Repository
import com.saddam.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel() {

    fun getAllStories() = repository.getAllstories()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}