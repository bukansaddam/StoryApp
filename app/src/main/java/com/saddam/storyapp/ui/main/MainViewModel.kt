package com.saddam.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saddam.storyapp.data.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel() {

    fun getAllStories() = repository.getAllstories()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}