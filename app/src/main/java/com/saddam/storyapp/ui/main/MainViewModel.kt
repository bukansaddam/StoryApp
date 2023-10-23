package com.saddam.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.saddam.storyapp.data.Repository

class MainViewModel(private val repository: Repository): ViewModel() {

    fun getAllStories() = repository.getAllstories()
}